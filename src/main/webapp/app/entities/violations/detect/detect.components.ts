import { Component, NgZone, OnInit, inject, ViewChild, ElementRef, QueryList, ViewChildren } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Data, ParamMap, Router, RouterModule } from '@angular/router';
import { Observable, Subscription, combineLatest, filter, tap } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { SortByDirective, SortDirective, SortService, type SortState, sortStateSignal } from 'app/shared/sort';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { ItemCountComponent } from 'app/shared/pagination';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITEMS_PER_PAGE, PAGE_HEADER, TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import { DEFAULT_SORT_DATA, ITEM_DELETED_EVENT, SORT } from 'app/config/navigation.constants';
import { NewViolations, IViolations } from '../violations.model';
import { EntityArrayResponseType, ViolationsService } from '../service/violations.service';
import { ViolationsDeleteDialogComponent } from '../delete/violations-delete-dialog.component';
import dayjs, { Dayjs } from 'dayjs/esm';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ITypeViolation } from '../../type-violation/type-violation.model';
import { map } from 'rxjs/operators';
import { TypeViolationService } from '../../type-violation/service/type-violation.service';
import { IVehicleRegistrations } from '../../vehicle-registrations/vehicle-registrations.model';

@Component({
  standalone: true,
  selector: 'jhi-violations',
  templateUrl: './detect.component.html',
  imports: [
    RouterModule,
    FormsModule,
    SharedModule,
    SortDirective,
    SortByDirective,
    DurationPipe,
    FormatMediumDatetimePipe,
    FormatMediumDatePipe,
    ItemCountComponent,
    ReactiveFormsModule,
  ],
})
export class DetectComponent implements OnInit {
  @ViewChildren('location') location!: QueryList<ElementRef<HTMLSelectElement>>;
  @ViewChildren('img') img!: QueryList<ElementRef<HTMLTableCellElement>>;
  @ViewChildren('typeViolation') typeViolation!: QueryList<ElementRef<HTMLSelectElement>>;
  @ViewChildren('license_plate') license_plate!: QueryList<ElementRef<HTMLTableCellElement>>;
  subscription: Subscription | null = null;
  violationstmp?: IViolations;
  violations?: IViolations[];
  isLoading = false;
  sortState = sortStateSignal({});

  itemsPerPage = ITEMS_PER_PAGE;
  totalItems = 0;
  page = 1;
  responseData: any;
  data: any;

  typeViolationsSharedCollection: ITypeViolation[] = [];
  vehicleRegistrationsSharedCollection: IVehicleRegistrations[] = [];
  public router = inject(Router);
  protected violationsService = inject(ViolationsService);
  protected activatedRoute = inject(ActivatedRoute);
  protected sortService = inject(SortService);
  protected modalService = inject(NgbModal);
  protected ngZone = inject(NgZone);
  protected typeViolationService = inject(TypeViolationService);
  protected provinces = ['Hà Nội', 'Hồ Chí Minh', 'Đà Nẵng', 'Cần Thơ', 'An Giang', 'Bà Rịa-Vũng Tàu', 'Bắc Giang', 'Bắc Kạn'];
  trackId = (item: IViolations): number => this.violationsService.getViolationsIdentifier(item);

  ngOnInit(): void {
    this.loadRelationshipsOptions();
    const navigation = this.router.getCurrentNavigation();
    this.data = history.state['data'];
    if (!this.data) {
      console.error('Không nhận được dữ liệu từ API.');
    }
  }
  delete(i: number): void {
    this.data = this.data.filter((_: any, index: number) => index !== i);
  }
  save(i: number): void {
    let tmp = '';
    const data = {
      location: '',
      img: '',
      licensePlate: {} as IVehicleRegistrations,
      typeViolation: {} as ITypeViolation,
    };

    this.location.forEach((cel, index) => {
      if (i === index) {
        data.location = cel.nativeElement.value;
      }
    });
    this.typeViolation.forEach((cel, index) => {
      if (i === index) {
        const selectedTypeViolation = cel.nativeElement.value;
        data.typeViolation = this.typeViolationsSharedCollection.find(tv => tv.id.toString() === selectedTypeViolation) as ITypeViolation;
      }
    });
    this.img.forEach((cel, index) => {
      if (i === index) {
        data.img = cel.nativeElement.innerText;
      }
    });
    this.license_plate.forEach((cel, index) => {
      if (i === index) {
        tmp = cel.nativeElement.innerText;
      }
    });
    this.violationsService.checkvr(tmp).subscribe({
      next: response => {
        if (response) {
          data.licensePlate.id = response;
          const violation: NewViolations = {
            id: null,
            violationTime: null,
            location: data.location,
            status: 'procesing',
            evidenceImage: data.img,
            createdAt: dayjs(),
            typeViolation: data.typeViolation,
            vehicleRegistrations: data.licensePlate,
          };

          // eslint-disable-next-line no-console
          console.log(violation);
          this.violationsService.create(violation).subscribe({
            next: () => {
              // eslint-disable-next-line no-console
              console.log('Tạo vi phạm thành công');
              this.data = this.data.filter((_: any, index: number) => index !== i);
              this.load();
            },
          });
        } else {
          console.error('Không tìm thấy bản ghi VehicleRegistrations với biển số này.');
        }
      },
    });
  }
  load(): void {
    this.loadRelationshipsOptions();
    this.queryBackend().subscribe({
      next: (res: EntityArrayResponseType) => {
        this.onResponseSuccess(res);
      },
    });
  }

  navigateToWithComponentValues(event: SortState): void {
    this.handleNavigation(this.page, event);
  }

  navigateToPage(page: number): void {
    this.handleNavigation(page, this.sortState());
  }

  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
    const page = params.get(PAGE_HEADER);
    this.page = +(page ?? 1);
    this.sortState.set(this.sortService.parseSortParam(params.get(SORT) ?? data[DEFAULT_SORT_DATA]));
  }

  protected onResponseSuccess(response: EntityArrayResponseType): void {
    this.fillComponentAttributesFromResponseHeader(response.headers);
    const dataFromBody = this.fillComponentAttributesFromResponseBody(response.body);
    this.violations = dataFromBody;
  }

  protected fillComponentAttributesFromResponseBody(data: IViolations[] | null): IViolations[] {
    return data ?? [];
  }

  protected fillComponentAttributesFromResponseHeader(headers: HttpHeaders): void {
    this.totalItems = Number(headers.get(TOTAL_COUNT_RESPONSE_HEADER));
  }

  protected queryBackend(): Observable<EntityArrayResponseType> {
    const { page } = this;

    this.isLoading = true;
    const pageToLoad: number = page;
    const queryObject: any = {
      page: pageToLoad - 1,
      size: this.itemsPerPage,
      sort: this.sortService.buildSortParam(this.sortState()),
    };
    return this.violationsService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
  }
  protected loadRelationshipsOptions(): void {
    this.typeViolationService
      .query()
      .pipe(map((res: HttpResponse<ITypeViolation[]>) => res.body ?? []))
      .pipe(
        map((typeViolations: ITypeViolation[]) =>
          this.typeViolationService.addTypeViolationToCollectionIfMissing<ITypeViolation>(
            typeViolations,
            this.violationstmp?.typeViolation,
          ),
        ),
      )
      .subscribe((typeViolations: ITypeViolation[]) => (this.typeViolationsSharedCollection = typeViolations));
  }
  protected handleNavigation(page: number, sortState: SortState): void {
    const queryParamsObj = {
      page,
      size: this.itemsPerPage,
      sort: this.sortService.buildSortParam(sortState),
    };

    this.ngZone.run(() => {
      this.router.navigate(['./'], {
        relativeTo: this.activatedRoute,
        queryParams: queryParamsObj,
      });
    });
  }
}
