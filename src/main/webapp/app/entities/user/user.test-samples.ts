import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 435,
  login: 'v_',
};

export const sampleWithPartialData: IUser = {
  id: 13002,
  login: 'NMKXz@zn\\,1XtFuk\\wF\\ys',
};

export const sampleWithFullData: IUser = {
  id: 29641,
  login: 'B',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
