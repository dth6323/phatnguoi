import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IViolations, NewViolations } from '../violations.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IViolations for edit and NewViolationsFormGroupInput for create.
 */
type ViolationsFormGroupInput = IViolations | PartialWithRequiredKeyOf<NewViolations>;

type ViolationsFormDefaults = Pick<NewViolations, 'id'>;

type ViolationsFormGroupContent = {
  id: FormControl<IViolations['id'] | NewViolations['id']>;
  violationTime: FormControl<IViolations['violationTime']>;
  location: FormControl<IViolations['location']>;
  status: FormControl<IViolations['status']>;
  evidenceImage: FormControl<IViolations['evidenceImage']>;
  createdAt: FormControl<IViolations['createdAt']>;
  typeViolation: FormControl<IViolations['typeViolation']>;
  vehicleRegistrations: FormControl<IViolations['vehicleRegistrations']>;
};

export type ViolationsFormGroup = FormGroup<ViolationsFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ViolationsFormService {
  createViolationsFormGroup(violations: ViolationsFormGroupInput = { id: null }): ViolationsFormGroup {
    const violationsRawValue = {
      ...this.getFormDefaults(),
      ...violations,
    };
    return new FormGroup<ViolationsFormGroupContent>({
      id: new FormControl(
        { value: violationsRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      violationTime: new FormControl(violationsRawValue.violationTime),
      location: new FormControl(violationsRawValue.location),
      status: new FormControl(violationsRawValue.status),
      evidenceImage: new FormControl(violationsRawValue.evidenceImage),
      createdAt: new FormControl(violationsRawValue.createdAt),
      typeViolation: new FormControl(violationsRawValue.typeViolation),
      vehicleRegistrations: new FormControl(violationsRawValue.vehicleRegistrations),
    });
  }

  getViolations(form: ViolationsFormGroup): IViolations | NewViolations {
    return form.getRawValue() as IViolations | NewViolations;
  }

  resetForm(form: ViolationsFormGroup, violations: ViolationsFormGroupInput): void {
    const violationsRawValue = { ...this.getFormDefaults(), ...violations };
    form.reset(
      {
        ...violationsRawValue,
        id: { value: violationsRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ViolationsFormDefaults {
    return {
      id: null,
    };
  }
}
