import dayjs from 'dayjs/esm';
import { ITypeViolation } from 'app/entities/type-violation/type-violation.model';
import { IVehicleRegistrations } from 'app/entities/vehicle-registrations/vehicle-registrations.model';

export interface IViolations {
  id: number;
  violationTime?: dayjs.Dayjs | null;
  location?: string | null;
  status?: string | null;
  evidenceImage?: string | null;
  createdAt?: dayjs.Dayjs | null;
  typeViolation?: ITypeViolation | null;
  vehicleRegistrations?: IVehicleRegistrations | null;
}

export type NewViolations = Omit<IViolations, 'id'> & { id: null };
