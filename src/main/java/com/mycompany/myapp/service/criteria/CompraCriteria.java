package com.mycompany.myapp.service.criteria;

import com.mycompany.myapp.domain.enumeration.EstadoTransacao;
import com.mycompany.myapp.domain.enumeration.Pagamento;
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
 * Criteria class for the {@link com.mycompany.myapp.domain.Compra} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.CompraResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /compras?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class CompraCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Pagamento
     */
    public static class PagamentoFilter extends Filter<Pagamento> {

        public PagamentoFilter() {}

        public PagamentoFilter(PagamentoFilter filter) {
            super(filter);
        }

        @Override
        public PagamentoFilter copy() {
            return new PagamentoFilter(this);
        }
    }

    /**
     * Class for filtering EstadoTransacao
     */
    public static class EstadoTransacaoFilter extends Filter<EstadoTransacao> {

        public EstadoTransacaoFilter() {}

        public EstadoTransacaoFilter(EstadoTransacaoFilter filter) {
            super(filter);
        }

        @Override
        public EstadoTransacaoFilter copy() {
            return new EstadoTransacaoFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private DoubleFilter percentualDesconto;

    private DoubleFilter valorFinal;

    private ZonedDateTimeFilter dataCriacao;

    private PagamentoFilter formaPagamento;

    private EstadoTransacaoFilter estado;

    private LongFilter cursoId;

    private LongFilter usuarioId;

    private Boolean distinct;

    public CompraCriteria() {}

    public CompraCriteria(CompraCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.percentualDesconto = other.percentualDesconto == null ? null : other.percentualDesconto.copy();
        this.valorFinal = other.valorFinal == null ? null : other.valorFinal.copy();
        this.dataCriacao = other.dataCriacao == null ? null : other.dataCriacao.copy();
        this.formaPagamento = other.formaPagamento == null ? null : other.formaPagamento.copy();
        this.estado = other.estado == null ? null : other.estado.copy();
        this.cursoId = other.cursoId == null ? null : other.cursoId.copy();
        this.usuarioId = other.usuarioId == null ? null : other.usuarioId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CompraCriteria copy() {
        return new CompraCriteria(this);
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

    public DoubleFilter getPercentualDesconto() {
        return percentualDesconto;
    }

    public DoubleFilter percentualDesconto() {
        if (percentualDesconto == null) {
            percentualDesconto = new DoubleFilter();
        }
        return percentualDesconto;
    }

    public void setPercentualDesconto(DoubleFilter percentualDesconto) {
        this.percentualDesconto = percentualDesconto;
    }

    public DoubleFilter getValorFinal() {
        return valorFinal;
    }

    public DoubleFilter valorFinal() {
        if (valorFinal == null) {
            valorFinal = new DoubleFilter();
        }
        return valorFinal;
    }

    public void setValorFinal(DoubleFilter valorFinal) {
        this.valorFinal = valorFinal;
    }

    public ZonedDateTimeFilter getDataCriacao() {
        return dataCriacao;
    }

    public ZonedDateTimeFilter dataCriacao() {
        if (dataCriacao == null) {
            dataCriacao = new ZonedDateTimeFilter();
        }
        return dataCriacao;
    }

    public void setDataCriacao(ZonedDateTimeFilter dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public PagamentoFilter getFormaPagamento() {
        return formaPagamento;
    }

    public PagamentoFilter formaPagamento() {
        if (formaPagamento == null) {
            formaPagamento = new PagamentoFilter();
        }
        return formaPagamento;
    }

    public void setFormaPagamento(PagamentoFilter formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public EstadoTransacaoFilter getEstado() {
        return estado;
    }

    public EstadoTransacaoFilter estado() {
        if (estado == null) {
            estado = new EstadoTransacaoFilter();
        }
        return estado;
    }

    public void setEstado(EstadoTransacaoFilter estado) {
        this.estado = estado;
    }

    public LongFilter getCursoId() {
        return cursoId;
    }

    public LongFilter cursoId() {
        if (cursoId == null) {
            cursoId = new LongFilter();
        }
        return cursoId;
    }

    public void setCursoId(LongFilter cursoId) {
        this.cursoId = cursoId;
    }

    public LongFilter getUsuarioId() {
        return usuarioId;
    }

    public LongFilter usuarioId() {
        if (usuarioId == null) {
            usuarioId = new LongFilter();
        }
        return usuarioId;
    }

    public void setUsuarioId(LongFilter usuarioId) {
        this.usuarioId = usuarioId;
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
        final CompraCriteria that = (CompraCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(percentualDesconto, that.percentualDesconto) &&
            Objects.equals(valorFinal, that.valorFinal) &&
            Objects.equals(dataCriacao, that.dataCriacao) &&
            Objects.equals(formaPagamento, that.formaPagamento) &&
            Objects.equals(estado, that.estado) &&
            Objects.equals(cursoId, that.cursoId) &&
            Objects.equals(usuarioId, that.usuarioId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, percentualDesconto, valorFinal, dataCriacao, formaPagamento, estado, cursoId, usuarioId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompraCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (percentualDesconto != null ? "percentualDesconto=" + percentualDesconto + ", " : "") +
            (valorFinal != null ? "valorFinal=" + valorFinal + ", " : "") +
            (dataCriacao != null ? "dataCriacao=" + dataCriacao + ", " : "") +
            (formaPagamento != null ? "formaPagamento=" + formaPagamento + ", " : "") +
            (estado != null ? "estado=" + estado + ", " : "") +
            (cursoId != null ? "cursoId=" + cursoId + ", " : "") +
            (usuarioId != null ? "usuarioId=" + usuarioId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
