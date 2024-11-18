import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IVehicleRegistrations, NewVehicleRegistrations } from '../vehicle-registrations.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IVehicleRegistrations for edit and NewVehicleRegistrationsFormGroupInput for create.
 */
type VehicleRegistrationsFormGroupInput = IVehicleRegistrations | PartialWithRequiredKeyOf<NewVehicleRegistrations>;

type VehicleRegistrationsFormDefaults = Pick<NewVehicleRegistrations, 'id'>;

type VehicleRegistrationsFormGroupContent = {
  id: FormControl<IVehicleRegistrations['id'] | NewVehicleRegistrations['id']>;
  vehicleNumber: FormControl<IVehicleRegistrations['vehicleNumber']>;
  ownerName: FormControl<IVehicleRegistrations['ownerName']>;
  engineNum: FormControl<IVehicleRegistrations['engineNum']>;
  chassisNum: FormControl<IVehicleRegistrations['chassisNum']>;
  vehicleType: FormControl<IVehicleRegistrations['vehicleType']>;
  brand: FormControl<IVehicleRegistrations['brand']>;
  modelCode: FormControl<IVehicleRegistrations['modelCode']>;
  color: FormControl<IVehicleRegistrations['color']>;
  capacity: FormControl<IVehicleRegistrations['capacity']>;
  registrationDate: FormControl<IVehicleRegistrations['registrationDate']>;
  expirationDate: FormControl<IVehicleRegistrations['expirationDate']>;
  issuedBy: FormControl<IVehicleRegistrations['issuedBy']>;
  cCCD: FormControl<IVehicleRegistrations['cccd']>;
};

export type VehicleRegistrationsFormGroup = FormGroup<VehicleRegistrationsFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class VehicleRegistrationsFormService {
  createVehicleRegistrationsFormGroup(
    vehicleRegistrations: VehicleRegistrationsFormGroupInput = { id: null },
  ): VehicleRegistrationsFormGroup {
    const vehicleRegistrationsRawValue = {
      ...this.getFormDefaults(),
      ...vehicleRegistrations,
    };
    return new FormGroup<VehicleRegistrationsFormGroupContent>({
      id: new FormControl(
        { value: vehicleRegistrationsRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      vehicleNumber: new FormControl(vehicleRegistrationsRawValue.vehicleNumber),
      ownerName: new FormControl(vehicleRegistrationsRawValue.ownerName),
      engineNum: new FormControl(vehicleRegistrationsRawValue.engineNum),
      chassisNum: new FormControl(vehicleRegistrationsRawValue.chassisNum),
      vehicleType: new FormControl(vehicleRegistrationsRawValue.vehicleType),
      brand: new FormControl(vehicleRegistrationsRawValue.brand),
      modelCode: new FormControl(vehicleRegistrationsRawValue.modelCode),
      color: new FormControl(vehicleRegistrationsRawValue.color),
      capacity: new FormControl(vehicleRegistrationsRawValue.capacity),
      registrationDate: new FormControl(vehicleRegistrationsRawValue.registrationDate),
      expirationDate: new FormControl(vehicleRegistrationsRawValue.expirationDate),
      issuedBy: new FormControl(vehicleRegistrationsRawValue.issuedBy),
      cCCD: new FormControl(vehicleRegistrationsRawValue.cccd),
    });
  }

  getVehicleRegistrations(form: VehicleRegistrationsFormGroup): IVehicleRegistrations | NewVehicleRegistrations {
    return form.getRawValue() as IVehicleRegistrations | NewVehicleRegistrations;
  }

  resetForm(form: VehicleRegistrationsFormGroup, vehicleRegistrations: VehicleRegistrationsFormGroupInput): void {
    const vehicleRegistrationsRawValue = { ...this.getFormDefaults(), ...vehicleRegistrations };
    form.reset(
      {
        ...vehicleRegistrationsRawValue,
        id: { value: vehicleRegistrationsRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): VehicleRegistrationsFormDefaults {
    return {
      id: null,
    };
  }
}
