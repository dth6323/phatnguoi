import dayjs from 'dayjs/esm';

export interface ICCCD {
  id: number;
  fullName?: string | null;
  dateBirth?: dayjs.Dayjs | null;
  sex?: string | null;
  nationality?: string | null;
  placeOrigin?: string | null;
  placeResidence?: string | null;
  dateIssue?: dayjs.Dayjs | null;
  dateExpiry?: dayjs.Dayjs | null;
  personalIdentification?: string | null;
}

export type NewCCCD = Omit<ICCCD, 'id'> & { id: null };
