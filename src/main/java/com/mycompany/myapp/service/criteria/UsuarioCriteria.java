package com.mycompany.myapp.service.criteria;

import com.mycompany.myapp.domain.enumeration.TipoUsuario;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Usuario} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.UsuarioResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /usuarios?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class UsuarioCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TipoUsuario
     */
    public static class TipoUsuarioFilter extends Filter<TipoUsuario> {

        public TipoUsuarioFilter() {}

        public TipoUsuarioFilter(TipoUsuarioFilter filter) {
            super(filter);
        }

        @Override
        public TipoUsuarioFilter copy() {
            return new TipoUsuarioFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nome;

    private StringFilter cpf;

    private LocalDateFilter dataNascimento;

    private TipoUsuarioFilter tipo;

    private ZonedDateTimeFilter criacao;

    private Boolean distinct;

    public UsuarioCriteria() {}

    public UsuarioCriteria(UsuarioCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nome = other.nome == null ? null : other.nome.copy();
        this.cpf = other.cpf == null ? null : other.cpf.copy();
        this.dataNascimento = other.dataNascimento == null ? null : other.dataNascimento.copy();
        this.tipo = other.tipo == null ? null : other.tipo.copy();
        this.criacao = other.criacao == null ? null : other.criacao.copy();
        this.distinct = other.distinct;
    }

    @Override
    public UsuarioCriteria copy() {
        return new UsuarioCriteria(this);
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

    public StringFilter getNome() {
        return nome;
    }

    public StringFilter nome() {
        if (nome == null) {
            nome = new StringFilter();
        }
        return nome;
    }

    public void setNome(StringFilter nome) {
        this.nome = nome;
    }

    public StringFilter getCpf() {
        return cpf;
    }

    public StringFilter cpf() {
        if (cpf == null) {
            cpf = new StringFilter();
        }
        return cpf;
    }

    public void setCpf(StringFilter cpf) {
        this.cpf = cpf;
    }

    public LocalDateFilter getDataNascimento() {
        return dataNascimento;
    }

    public LocalDateFilter dataNascimento() {
        if (dataNascimento == null) {
            dataNascimento = new LocalDateFilter();
        }
        return dataNascimento;
    }

    public void setDataNascimento(LocalDateFilter dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public TipoUsuarioFilter getTipo() {
        return tipo;
    }

    public TipoUsuarioFilter tipo() {
        if (tipo == null) {
            tipo = new TipoUsuarioFilter();
        }
        return tipo;
    }

    public void setTipo(TipoUsuarioFilter tipo) {
        this.tipo = tipo;
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
        final UsuarioCriteria that = (UsuarioCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nome, that.nome) &&
            Objects.equals(cpf, that.cpf) &&
            Objects.equals(dataNascimento, that.dataNascimento) &&
            Objects.equals(tipo, that.tipo) &&
            Objects.equals(criacao, that.criacao) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, cpf, dataNascimento, tipo, criacao, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UsuarioCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nome != null ? "nome=" + nome + ", " : "") +
            (cpf != null ? "cpf=" + cpf + ", " : "") +
            (dataNascimento != null ? "dataNascimento=" + dataNascimento + ", " : "") +
            (tipo != null ? "tipo=" + tipo + ", " : "") +
            (criacao != null ? "criacao=" + criacao + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
