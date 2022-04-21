import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CompraService } from '../service/compra.service';
import { ICompra, Compra } from '../compra.model';
import { ICurso } from 'app/entities/curso/curso.model';
import { CursoService } from 'app/entities/curso/service/curso.service';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { UsuarioService } from 'app/entities/usuario/service/usuario.service';

import { CompraUpdateComponent } from './compra-update.component';

describe('Compra Management Update Component', () => {
  let comp: CompraUpdateComponent;
  let fixture: ComponentFixture<CompraUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let compraService: CompraService;
  let cursoService: CursoService;
  let usuarioService: UsuarioService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CompraUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(CompraUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CompraUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    compraService = TestBed.inject(CompraService);
    cursoService = TestBed.inject(CursoService);
    usuarioService = TestBed.inject(UsuarioService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Curso query and add missing value', () => {
      const compra: ICompra = { id: 456 };
      const curso: ICurso = { id: 92806 };
      compra.curso = curso;

      const cursoCollection: ICurso[] = [{ id: 36742 }];
      jest.spyOn(cursoService, 'query').mockReturnValue(of(new HttpResponse({ body: cursoCollection })));
      const additionalCursos = [curso];
      const expectedCollection: ICurso[] = [...additionalCursos, ...cursoCollection];
      jest.spyOn(cursoService, 'addCursoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ compra });
      comp.ngOnInit();

      expect(cursoService.query).toHaveBeenCalled();
      expect(cursoService.addCursoToCollectionIfMissing).toHaveBeenCalledWith(cursoCollection, ...additionalCursos);
      expect(comp.cursosSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Usuario query and add missing value', () => {
      const compra: ICompra = { id: 456 };
      const usuario: IUsuario = { id: 15801 };
      compra.usuario = usuario;

      const usuarioCollection: IUsuario[] = [{ id: 42785 }];
      jest.spyOn(usuarioService, 'query').mockReturnValue(of(new HttpResponse({ body: usuarioCollection })));
      const additionalUsuarios = [usuario];
      const expectedCollection: IUsuario[] = [...additionalUsuarios, ...usuarioCollection];
      jest.spyOn(usuarioService, 'addUsuarioToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ compra });
      comp.ngOnInit();

      expect(usuarioService.query).toHaveBeenCalled();
      expect(usuarioService.addUsuarioToCollectionIfMissing).toHaveBeenCalledWith(usuarioCollection, ...additionalUsuarios);
      expect(comp.usuariosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const compra: ICompra = { id: 456 };
      const curso: ICurso = { id: 93125 };
      compra.curso = curso;
      const usuario: IUsuario = { id: 2253 };
      compra.usuario = usuario;

      activatedRoute.data = of({ compra });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(compra));
      expect(comp.cursosSharedCollection).toContain(curso);
      expect(comp.usuariosSharedCollection).toContain(usuario);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Compra>>();
      const compra = { id: 123 };
      jest.spyOn(compraService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ compra });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: compra }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(compraService.update).toHaveBeenCalledWith(compra);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Compra>>();
      const compra = new Compra();
      jest.spyOn(compraService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ compra });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: compra }));
      saveSubject.complete();

      // THEN
      expect(compraService.create).toHaveBeenCalledWith(compra);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Compra>>();
      const compra = { id: 123 };
      jest.spyOn(compraService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ compra });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(compraService.update).toHaveBeenCalledWith(compra);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackCursoById', () => {
      it('Should return tracked Curso primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCursoById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackUsuarioById', () => {
      it('Should return tracked Usuario primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackUsuarioById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
