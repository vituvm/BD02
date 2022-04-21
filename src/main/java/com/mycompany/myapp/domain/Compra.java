package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.EstadoTransacao;
import com.mycompany.myapp.domain.enumeration.Pagamento;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Compra.
 */
@Entity
@Table(name = "compra")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Compra implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "percentual_desconto")
    private Double percentualDesconto;

    @Column(name = "valor_final")
    private Double valorFinal;

    @Column(name = "data_criacao")
    private ZonedDateTime dataCriacao;

    @Enumerated(EnumType.STRING)
    @Column(name = "forma_pagamento")
    private Pagamento formaPagamento;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoTransacao estado;

    @ManyToOne
    @JsonIgnoreProperties(value = { "professor", "aluno" }, allowSetters = true)
    private Curso curso;

    @ManyToOne
    private Usuario usuario;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Compra id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getPercentualDesconto() {
        return this.percentualDesconto;
    }

    public Compra percentualDesconto(Double percentualDesconto) {
        this.setPercentualDesconto(percentualDesconto);
        return this;
    }

    public void setPercentualDesconto(Double percentualDesconto) {
        this.percentualDesconto = percentualDesconto;
    }

    public Double getValorFinal() {
        return this.valorFinal;
    }

    public Compra valorFinal(Double valorFinal) {
        this.setValorFinal(valorFinal);
        return this;
    }

    public void setValorFinal(Double valorFinal) {
        this.valorFinal = valorFinal;
    }

    public ZonedDateTime getDataCriacao() {
        return this.dataCriacao;
    }

    public Compra dataCriacao(ZonedDateTime dataCriacao) {
        this.setDataCriacao(dataCriacao);
        return this;
    }

    public void setDataCriacao(ZonedDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Pagamento getFormaPagamento() {
        return this.formaPagamento;
    }

    public Compra formaPagamento(Pagamento formaPagamento) {
        this.setFormaPagamento(formaPagamento);
        return this;
    }

    public void setFormaPagamento(Pagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public EstadoTransacao getEstado() {
        return this.estado;
    }

    public Compra estado(EstadoTransacao estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(EstadoTransacao estado) {
        this.estado = estado;
    }

    public Curso getCurso() {
        return this.curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public Compra curso(Curso curso) {
        this.setCurso(curso);
        return this;
    }

    public Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Compra usuario(Usuario usuario) {
        this.setUsuario(usuario);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Compra)) {
            return false;
        }
        return id != null && id.equals(((Compra) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Compra{" +
            "id=" + getId() +
            ", percentualDesconto=" + getPercentualDesconto() +
            ", valorFinal=" + getValorFinal() +
            ", dataCriacao='" + getDataCriacao() + "'" +
            ", formaPagamento='" + getFormaPagamento() + "'" +
            ", estado='" + getEstado() + "'" +
            "}";
    }
}
