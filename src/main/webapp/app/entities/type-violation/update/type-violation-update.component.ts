import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITypeViolation } from '../type-violation.model';
import { TypeViolationService } from '../service/type-violation.service';
import { TypeViolationFormGroup, TypeViolationFormService } from './type-violation-form.service';

@Component({
  standalone: true,
  selector: 'jhi-type-violation-update',
  templateUrl: './type-violation-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TypeViolationUpdateComponent implements OnInit {
  isSaving = false;
  typeViolation: ITypeViolation | null = null;

  protected typeViolationService = inject(TypeViolationService);
  protected typeViolationFormService = inject(TypeViolationFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TypeViolationFormGroup = this.typeViolationFormService.createTypeViolationFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ typeViolation }) => {
      this.typeViolation = typeViolation;
      if (typeViolation) {
        this.updateForm(typeViolation);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const typeViolation = this.typeViolationFormService.getTypeViolation(this.editForm);
    if (typeViolation.id !== null) {
      this.subscribeToSaveResponse(this.typeViolationService.update(typeViolation));
    } else {
      this.subscribeToSaveResponse(this.typeViolationService.create(typeViolation));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITypeViolation>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(typeViolation: ITypeViolation): void {
    this.typeViolation = typeViolation;
    this.typeViolationFormService.resetForm(this.editForm, typeViolation);
  }
}
