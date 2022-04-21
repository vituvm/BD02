package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Curso} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.CursoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /cursos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class CursoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter titulo;

    private DoubleFilter duracaoCH;

    private StringFilter descricao;

    private DoubleFilter valor;

    private ZonedDateTimeFilter criacao;

    private LongFilter professorId;

    private LongFilter alunoId;

    private Boolean distinct;

    public CursoCriteria() {}

    public CursoCriteria(CursoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.titulo = other.titulo == null ? null : other.titulo.copy();
        this.duracaoCH = other.duracaoCH == null ? null : other.duracaoCH.copy();
        this.descricao = other.descricao == null ? null : other.descricao.copy();
        this.valor = other.valor == null ? null : other.valor.copy();
        this.criacao = other.criacao == null ? null : other.criacao.copy();
        this.professorId = other.professorId == null ? null : other.professorId.copy();
        this.alunoId = other.alunoId == null ? null : other.alunoId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CursoCriteria copy() {
        return new CursoCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTitulo() {
        return titulo;
    }

    public StringFilter titulo() {
        if (titulo == null) {
            titulo = new StringFilter();
        }
        return titulo;
    }

    public void setTitulo(StringFilter titulo) {
        this.titulo = titulo;
    }

    public DoubleFilter getDuracaoCH() {
        return duracaoCH;
    }

    public DoubleFilter duracaoCH() {
        if (duracaoCH == null) {
            duracaoCH = new DoubleFilter();
        }
        return duracaoCH;
    }

    public void setDuracaoCH(DoubleFilter duracaoCH) {
        this.duracaoCH = duracaoCH;
    }

    public StringFilter getDescricao() {
        return descricao;
    }

    public StringFilter descricao() {
        if (descricao == null) {
            descricao = new StringFilter();
        }
        return descricao;
    }

    public void setDescricao(StringFilter descricao) {
        this.descricao = descricao;
    }

    public DoubleFilter getValor() {
        return valor;
    }

    public DoubleFilter valor() {
        if (valor == null) {
            valor = new DoubleFilter();
        }
        return valor;
    }

    public void setValor(DoubleFilter valor) {
        this.valor = valor;
    }

    public ZonedDateTimeFilter getCriacao() {
        return criacao;
    }

    public ZonedDateTimeFilter criacao() {
        if (criacao == null) {
            criacao = new ZonedDateTimeFilter();
        }
        return criacao;
    }

    public void setCriacao(ZonedDateTimeFilter criacao) {
        this.criacao = criacao;
    }

    public LongFilter getProfessorId() {
        return professorId;
    }

    public LongFilter professorId() {
        if (professorId == null) {
            professorId = new LongFilter();
        }
        return professorId;
    }

    public void setProfessorId(LongFilter professorId) {
        this.professorId = professorId;
    }

    public LongFilter getAlunoId() {
        return alunoId;
    }

    public LongFilter alunoId() {
        if (alunoId == null) {
            alunoId = new LongFilter();
        }
        return alunoId;
    }

    public void setAlunoId(LongFilter alunoId) {
        this.alunoId = alunoId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CursoCriteria that = (CursoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(titulo, that.titulo) &&
            Objects.equals(duracaoCH, that.duracaoCH) &&
            Objects.equals(descricao, that.descricao) &&
            Objects.equals(valor, that.valor) &&
            Objects.equals(criacao, that.criacao) &&
            Objects.equals(professorId, that.professorId) &&
            Objects.equals(alunoId, that.alunoId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, titulo, duracaoCH, descricao, valor, criacao, professorId, alunoId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CursoCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (titulo != null ? "titulo=" + titulo + ", " : "") +
            (duracaoCH != null ? "duracaoCH=" + duracaoCH + ", " : "") +
            (descricao != null ? "descricao=" + descricao + ", " : "") +
            (valor != null ? "valor=" + valor + ", " : "") +
            (criacao != null ? "criacao=" + criacao + ", " : "") +
            (professorId != null ? "professorId=" + professorId + ", " : "") +
            (alunoId != null ? "alunoId=" + alunoId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
