import dayjs from 'dayjs/esm';
import { ICCCD } from 'app/entities/cccd/cccd.model';

export interface IVehicleRegistrations {
  id: number;
  vehicleNumber?: string | null;
  ownerName?: string | null;
  engineNum?: string | null;
  chassisNum?: string | null;
  vehicleType?: string | null;
  brand?: string | null;
  modelCode?: string | null;
  color?: string | null;
  capacity?: string | null;
  registrationDate?: dayjs.Dayjs | null;
  expirationDate?: dayjs.Dayjs | null;
  issuedBy?: string | null;
  cCCD?: ICCCD | null;
}

export type NewVehicleRegistrations = Omit<IVehicleRegistrations, 'id'> & { id: null };
