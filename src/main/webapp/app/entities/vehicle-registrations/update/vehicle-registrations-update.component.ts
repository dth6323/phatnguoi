import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICCCD } from 'app/entities/cccd/cccd.model';
import { CCCDService } from 'app/entities/cccd/service/cccd.service';
import { IVehicleRegistrations } from '../vehicle-registrations.model';
import { VehicleRegistrationsService } from '../service/vehicle-registrations.service';
import { VehicleRegistrationsFormGroup, VehicleRegistrationsFormService } from './vehicle-registrations-form.service';

@Component({
  standalone: true,
  selector: 'jhi-vehicle-registrations-update',
  templateUrl: './vehicle-registrations-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class VehicleRegistrationsUpdateComponent implements OnInit {
  isSaving = false;
  vehicleRegistrations: IVehicleRegistrations | null = null;

  cCCDSSharedCollection: ICCCD[] = [];

  protected vehicleRegistrationsService = inject(VehicleRegistrationsService);
  protected vehicleRegistrationsFormService = inject(VehicleRegistrationsFormService);
  protected cCCDService = inject(CCCDService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: VehicleRegistrationsFormGroup = this.vehicleRegistrationsFormService.createVehicleRegistrationsFormGroup();

  compareCCCD = (o1: ICCCD | null, o2: ICCCD | null): boolean => this.cCCDService.compareCCCD(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vehicleRegistrations }) => {
      this.vehicleRegistrations = vehicleRegistrations;
      if (vehicleRegistrations) {
        this.updateForm(vehicleRegistrations);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const vehicleRegistrations = this.vehicleRegistrationsFormService.getVehicleRegistrations(this.editForm);
    if (vehicleRegistrations.id !== null) {
      this.subscribeToSaveResponse(this.vehicleRegistrationsService.update(vehicleRegistrations));
    } else {
      this.subscribeToSaveResponse(this.vehicleRegistrationsService.create(vehicleRegistrations));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVehicleRegistrations>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(vehicleRegistrations: IVehicleRegistrations): void {
    this.vehicleRegistrations = vehicleRegistrations;
    this.vehicleRegistrationsFormService.resetForm(this.editForm, vehicleRegistrations);

    this.cCCDSSharedCollection = this.cCCDService.addCCCDToCollectionIfMissing<ICCCD>(
      this.cCCDSSharedCollection,
      vehicleRegistrations.cccd,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.cCCDService
      .query()
      .pipe(map((res: HttpResponse<ICCCD[]>) => res.body ?? []))
      .pipe(map((cCCDS: ICCCD[]) => this.cCCDService.addCCCDToCollectionIfMissing<ICCCD>(cCCDS, this.vehicleRegistrations?.cccd)))
      .subscribe((cCCDS: ICCCD[]) => (this.cCCDSSharedCollection = cCCDS));
  }
}
