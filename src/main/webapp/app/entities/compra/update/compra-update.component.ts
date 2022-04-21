import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ICompra, Compra } from '../compra.model';
import { CompraService } from '../service/compra.service';
import { ICurso } from 'app/entities/curso/curso.model';
import { CursoService } from 'app/entities/curso/service/curso.service';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { UsuarioService } from 'app/entities/usuario/service/usuario.service';
import { Pagamento } from 'app/entities/enumerations/pagamento.model';
import { EstadoTransacao } from 'app/entities/enumerations/estado-transacao.model';

@Component({
  selector: 'jhi-compra-update',
  templateUrl: './compra-update.component.html',
})
export class CompraUpdateComponent implements OnInit {
  isSaving = false;
  pagamentoValues = Object.keys(Pagamento);
  estadoTransacaoValues = Object.keys(EstadoTransacao);

  cursosSharedCollection: ICurso[] = [];
  usuariosSharedCollection: IUsuario[] = [];

  editForm = this.fb.group({
    id: [],
    percentualDesconto: [],
    valorFinal: [],
    dataCriacao: [],
    formaPagamento: [],
    estado: [],
    curso: [],
    usuario: [],
  });

  constructor(
    protected compraService: CompraService,
    protected cursoService: CursoService,
    protected usuarioService: UsuarioService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ compra }) => {
      if (compra.id === undefined) {
        const today = dayjs().startOf('day');
        compra.dataCriacao = today;
      }

      this.updateForm(compra);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const compra = this.createFromForm();
    if (compra.id !== undefined) {
      this.subscribeToSaveResponse(this.compraService.update(compra));
    } else {
      this.subscribeToSaveResponse(this.compraService.create(compra));
    }
  }

  trackCursoById(_index: number, item: ICurso): number {
    return item.id!;
  }

  trackUsuarioById(_index: number, item: IUsuario): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICompra>>): void {
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

  protected updateForm(compra: ICompra): void {
    this.editForm.patchValue({
      id: compra.id,
      percentualDesconto: compra.percentualDesconto,
      valorFinal: compra.valorFinal,
      dataCriacao: compra.dataCriacao ? compra.dataCriacao.format(DATE_TIME_FORMAT) : null,
      formaPagamento: compra.formaPagamento,
      estado: compra.estado,
      curso: compra.curso,
      usuario: compra.usuario,
    });

    this.cursosSharedCollection = this.cursoService.addCursoToCollectionIfMissing(this.cursosSharedCollection, compra.curso);
    this.usuariosSharedCollection = this.usuarioService.addUsuarioToCollectionIfMissing(this.usuariosSharedCollection, compra.usuario);
  }

  protected loadRelationshipsOptions(): void {
    this.cursoService
      .query()
      .pipe(map((res: HttpResponse<ICurso[]>) => res.body ?? []))
      .pipe(map((cursos: ICurso[]) => this.cursoService.addCursoToCollectionIfMissing(cursos, this.editForm.get('curso')!.value)))
      .subscribe((cursos: ICurso[]) => (this.cursosSharedCollection = cursos));

    this.usuarioService
      .query()
      .pipe(map((res: HttpResponse<IUsuario[]>) => res.body ?? []))
      .pipe(
        map((usuarios: IUsuario[]) => this.usuarioService.addUsuarioToCollectionIfMissing(usuarios, this.editForm.get('usuario')!.value))
      )
      .subscribe((usuarios: IUsuario[]) => (this.usuariosSharedCollection = usuarios));
  }

  protected createFromForm(): ICompra {
    return {
      ...new Compra(),
      id: this.editForm.get(['id'])!.value,
      percentualDesconto: this.editForm.get(['percentualDesconto'])!.value,
      valorFinal: this.editForm.get(['valorFinal'])!.value,
      dataCriacao: this.editForm.get(['dataCriacao'])!.value
        ? dayjs(this.editForm.get(['dataCriacao'])!.value, DATE_TIME_FORMAT)
        : undefined,
      formaPagamento: this.editForm.get(['formaPagamento'])!.value,
      estado: this.editForm.get(['estado'])!.value,
      curso: this.editForm.get(['curso'])!.value,
      usuario: this.editForm.get(['usuario'])!.value,
    };
  }
}
