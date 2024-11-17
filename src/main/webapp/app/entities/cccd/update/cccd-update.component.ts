import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICCCD } from '../cccd.model';
import { CCCDService } from '../service/cccd.service';
import { CCCDFormGroup, CCCDFormService } from './cccd-form.service';

@Component({
  standalone: true,
  selector: 'jhi-cccd-update',
  templateUrl: './cccd-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CCCDUpdateComponent implements OnInit {
  isSaving = false;
  cCCD: ICCCD | null = null;

  protected cCCDService = inject(CCCDService);
  protected cCCDFormService = inject(CCCDFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CCCDFormGroup = this.cCCDFormService.createCCCDFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cCCD }) => {
      this.cCCD = cCCD;
      if (cCCD) {
        this.updateForm(cCCD);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cCCD = this.cCCDFormService.getCCCD(this.editForm);
    if (cCCD.id !== null) {
      this.subscribeToSaveResponse(this.cCCDService.update(cCCD));
    } else {
      this.subscribeToSaveResponse(this.cCCDService.create(cCCD));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICCCD>>): void {
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

  protected updateForm(cCCD: ICCCD): void {
    this.cCCD = cCCD;
    this.cCCDFormService.resetForm(this.editForm, cCCD);
  }
}
