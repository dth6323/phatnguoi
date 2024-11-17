import dayjs from 'dayjs/esm';

import { ICCCD, NewCCCD } from './cccd.model';

export const sampleWithRequiredData: ICCCD = {
  id: 2190,
};

export const sampleWithPartialData: ICCCD = {
  id: 12998,
  dateBirth: dayjs('2024-11-17'),
};

export const sampleWithFullData: ICCCD = {
  id: 9252,
  fullName: 'heartache',
  dateBirth: dayjs('2024-11-16'),
  sex: 'despite deceivingly',
  nationality: 'unto outlandish draw',
  placeOrigin: 'chatter',
  placeResidence: 'scotch until storyboard',
  dateIssue: dayjs('2024-11-17'),
  dateExpiry: dayjs('2024-11-16'),
  personalIdentification: 'circa sans',
};

export const sampleWithNewData: NewCCCD = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
