import { Component, NgZone, OnInit, inject } from '@angular/core';
import { HttpHeaders } from '@angular/common/http';
import { ActivatedRoute, Data, ParamMap, Router, RouterModule } from '@angular/router';
import { Observable, Subscription, combineLatest, filter, tap } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { SortByDirective, SortDirective, SortService, type SortState, sortStateSignal } from 'app/shared/sort';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { ItemCountComponent } from 'app/shared/pagination';
import { FormsModule } from '@angular/forms';

import { ITEMS_PER_PAGE, PAGE_HEADER, TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import { DEFAULT_SORT_DATA, ITEM_DELETED_EVENT, SORT } from 'app/config/navigation.constants';
import { IViolations } from '../violations.model';
import { EntityArrayResponseType, ViolationsService } from '../service/violations.service';
import { ViolationsDeleteDialogComponent } from '../delete/violations-delete-dialog.component';
import dayjs, { Dayjs } from 'dayjs/esm';
import { DATE_FORMAT } from 'app/config/input.constants';

@Component({
  standalone: true,
  selector: 'jhi-violations',
  templateUrl: './violations.component.html',
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
  ],
})
export class ViolationsComponent implements OnInit {
  subscription: Subscription | null = null;
  violations?: IViolations[];
  isLoading = false;
  startDate: Dayjs = dayjs('2000-01-01');
  endDate: Dayjs = dayjs('2029-01-01');
  startAge = '';
  endAge = '';
  sortState = sortStateSignal({});

  responseData: any;
  itemsPerPage = ITEMS_PER_PAGE;
  totalItems = 0;
  page = 1;

  public router = inject(Router);
  protected violationsService = inject(ViolationsService);
  protected activatedRoute = inject(ActivatedRoute);
  protected sortService = inject(SortService);
  protected modalService = inject(NgbModal);
  protected ngZone = inject(NgZone);

  trackId = (item: IViolations): number => this.violationsService.getViolationsIdentifier(item);

  ngOnInit(): void {
    this.subscription = combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data])
      .pipe(
        tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
        tap(() => this.load()),
      )
      .subscribe();
  }
  loadStatical(): void {
    if (!this.startAge || !this.endAge) return;
    this.isLoading = true;
    this.violationsService.statical(dayjs(this.startDate), dayjs(this.endDate), this.startAge, this.endAge).subscribe({
      next: data => {
        this.violations = data.body ?? undefined;
        this.isLoading = false;
      },
      error: err => {
        console.error('Error fetching violations:', err);
        this.isLoading = false;
      },
    });
  }
  onStartAgeChange(value: string): void {
    this.startDate = dayjs(value); // Cập nhật startAge
  }

  onEndAgeChange(value: string): void {
    this.endDate = dayjs(value); // Cập nhật endAge
  }
  delete(violations: IViolations): void {
    const modalRef = this.modalService.open(ViolationsDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.violations = violations;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        tap(() => this.load()),
      )
      .subscribe();
  }

  load(): void {
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
