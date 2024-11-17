import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { TypeViolationService } from '../service/type-violation.service';
import { ITypeViolation } from '../type-violation.model';
import { TypeViolationFormService } from './type-violation-form.service';

import { TypeViolationUpdateComponent } from './type-violation-update.component';

describe('TypeViolation Management Update Component', () => {
  let comp: TypeViolationUpdateComponent;
  let fixture: ComponentFixture<TypeViolationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let typeViolationFormService: TypeViolationFormService;
  let typeViolationService: TypeViolationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [TypeViolationUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(TypeViolationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TypeViolationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    typeViolationFormService = TestBed.inject(TypeViolationFormService);
    typeViolationService = TestBed.inject(TypeViolationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const typeViolation: ITypeViolation = { id: 456 };

      activatedRoute.data = of({ typeViolation });
      comp.ngOnInit();

      expect(comp.typeViolation).toEqual(typeViolation);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITypeViolation>>();
      const typeViolation = { id: 123 };
      jest.spyOn(typeViolationFormService, 'getTypeViolation').mockReturnValue(typeViolation);
      jest.spyOn(typeViolationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeViolation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: typeViolation }));
      saveSubject.complete();

      // THEN
      expect(typeViolationFormService.getTypeViolation).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(typeViolationService.update).toHaveBeenCalledWith(expect.objectContaining(typeViolation));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITypeViolation>>();
      const typeViolation = { id: 123 };
      jest.spyOn(typeViolationFormService, 'getTypeViolation').mockReturnValue({ id: null });
      jest.spyOn(typeViolationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeViolation: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: typeViolation }));
      saveSubject.complete();

      // THEN
      expect(typeViolationFormService.getTypeViolation).toHaveBeenCalled();
      expect(typeViolationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITypeViolation>>();
      const typeViolation = { id: 123 };
      jest.spyOn(typeViolationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeViolation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(typeViolationService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
