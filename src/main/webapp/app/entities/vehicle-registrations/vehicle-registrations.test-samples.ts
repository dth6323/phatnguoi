import dayjs from 'dayjs/esm';

import { IVehicleRegistrations, NewVehicleRegistrations } from './vehicle-registrations.model';

export const sampleWithRequiredData: IVehicleRegistrations = {
  id: 3447,
};

export const sampleWithPartialData: IVehicleRegistrations = {
  id: 31664,
  engineNum: 'webbed',
  vehicleType: 'usefully',
  brand: 'gah',
  color: 'indigo',
  capacity: 'obvious',
  registrationDate: dayjs('2024-11-17'),
  expirationDate: dayjs('2024-11-17'),
};

export const sampleWithFullData: IVehicleRegistrations = {
  id: 16931,
  vehicleNumber: 'down contradict',
  ownerName: 'aha huzzah phooey',
  engineNum: 'but',
  chassisNum: 'amid minus',
  vehicleType: 'subsidy and',
  brand: 'yahoo',
  modelCode: 'superb',
  color: 'teal',
  capacity: 'cow round',
  registrationDate: dayjs('2024-11-17'),
  expirationDate: dayjs('2024-11-17'),
  issuedBy: 'fraudster cruel',
};

export const sampleWithNewData: NewVehicleRegistrations = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
