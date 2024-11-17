import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: '28555ac1-87b0-4da6-8fc9-20676650e6e6',
};

export const sampleWithPartialData: IAuthority = {
  name: 'fa5b66dc-d092-4eb4-a152-8580dfa24792',
};

export const sampleWithFullData: IAuthority = {
  name: '67aa5432-e609-4ab0-85d3-f590e71be149',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
