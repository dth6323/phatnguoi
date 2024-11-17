import dayjs from 'dayjs/esm';

import { IViolations, NewViolations } from './violations.model';

export const sampleWithRequiredData: IViolations = {
  id: 26025,
};

export const sampleWithPartialData: IViolations = {
  id: 12590,
  violationTime: dayjs('2024-11-17'),
};

export const sampleWithFullData: IViolations = {
  id: 14653,
  violationTime: dayjs('2024-11-17'),
  location: 'canter nautical finally',
  status: 'proud jumbo ah',
  evidenceImage: 'quirkily including',
  createdAt: dayjs('2024-11-17'),
};

export const sampleWithNewData: NewViolations = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
