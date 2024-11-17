import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ICCCD, NewCCCD } from '../cccd.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICCCD for edit and NewCCCDFormGroupInput for create.
 */
type CCCDFormGroupInput = ICCCD | PartialWithRequiredKeyOf<NewCCCD>;

type CCCDFormDefaults = Pick<NewCCCD, 'id'>;

type CCCDFormGroupContent = {
  id: FormControl<ICCCD['id'] | NewCCCD['id']>;
  fullName: FormControl<ICCCD['fullName']>;
  dateBirth: FormControl<ICCCD['dateBirth']>;
  sex: FormControl<ICCCD['sex']>;
  nationality: FormControl<ICCCD['nationality']>;
  placeOrigin: FormControl<ICCCD['placeOrigin']>;
  placeResidence: FormControl<ICCCD['placeResidence']>;
  dateIssue: FormControl<ICCCD['dateIssue']>;
  dateExpiry: FormControl<ICCCD['dateExpiry']>;
  personalIdentification: FormControl<ICCCD['personalIdentification']>;
};

export type CCCDFormGroup = FormGroup<CCCDFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CCCDFormService {
  createCCCDFormGroup(cCCD: CCCDFormGroupInput = { id: null }): CCCDFormGroup {
    const cCCDRawValue = {
      ...this.getFormDefaults(),
      ...cCCD,
    };
    return new FormGroup<CCCDFormGroupContent>({
      id: new FormControl(
        { value: cCCDRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      fullName: new FormControl(cCCDRawValue.fullName),
      dateBirth: new FormControl(cCCDRawValue.dateBirth),
      sex: new FormControl(cCCDRawValue.sex),
      nationality: new FormControl(cCCDRawValue.nationality),
      placeOrigin: new FormControl(cCCDRawValue.placeOrigin),
      placeResidence: new FormControl(cCCDRawValue.placeResidence),
      dateIssue: new FormControl(cCCDRawValue.dateIssue),
      dateExpiry: new FormControl(cCCDRawValue.dateExpiry),
      personalIdentification: new FormControl(cCCDRawValue.personalIdentification),
    });
  }

  getCCCD(form: CCCDFormGroup): ICCCD | NewCCCD {
    return form.getRawValue() as ICCCD | NewCCCD;
  }

  resetForm(form: CCCDFormGroup, cCCD: CCCDFormGroupInput): void {
    const cCCDRawValue = { ...this.getFormDefaults(), ...cCCD };
    form.reset(
      {
        ...cCCDRawValue,
        id: { value: cCCDRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CCCDFormDefaults {
    return {
      id: null,
    };
  }
}
