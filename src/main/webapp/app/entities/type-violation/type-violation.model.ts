export interface ITypeViolation {
  id: number;
  violationName?: string | null;
  fineAmount?: number | null;
}

export type NewTypeViolation = Omit<ITypeViolation, 'id'> & { id: null };
