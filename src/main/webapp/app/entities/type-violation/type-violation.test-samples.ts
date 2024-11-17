import { ITypeViolation, NewTypeViolation } from './type-violation.model';

export const sampleWithRequiredData: ITypeViolation = {
  id: 1181,
};

export const sampleWithPartialData: ITypeViolation = {
  id: 24951,
  violationName: 'proliferate sure-footed airport',
};

export const sampleWithFullData: ITypeViolation = {
  id: 11466,
  violationName: 'which',
  fineAmount: 13706.77,
};

export const sampleWithNewData: NewTypeViolation = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
