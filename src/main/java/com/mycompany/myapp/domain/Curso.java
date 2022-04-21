package com.mycompany.myapp.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Curso.
 */
@Entity
@Table(name = "curso")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Curso implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Column(name = "duracao_ch")
    private Double duracaoCH;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "valor")
    private Double valor;

    @Column(name = "criacao")
    private ZonedDateTime criacao;

    @ManyToOne
    private Usuario professor;

    @ManyToOne
    private Usuario aluno;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Curso id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public Curso titulo(String titulo) {
        this.setTitulo(titulo);
        return this;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Double getDuracaoCH() {
        return this.duracaoCH;
    }

    public Curso duracaoCH(Double duracaoCH) {
        this.setDuracaoCH(duracaoCH);
        return this;
    }

    public void setDuracaoCH(Double duracaoCH) {
        this.duracaoCH = duracaoCH;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public Curso descricao(String descricao) {
        this.setDescricao(descricao);
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getValor() {
        return this.valor;
    }

    public Curso valor(Double valor) {
        this.setValor(valor);
        return this;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public ZonedDateTime getCriacao() {
        return this.criacao;
    }

    public Curso criacao(ZonedDateTime criacao) {
        this.setCriacao(criacao);
        return this;
    }

    public void setCriacao(ZonedDateTime criacao) {
        this.criacao = criacao;
    }

    public Usuario getProfessor() {
        return this.professor;
    }

    public void setProfessor(Usuario usuario) {
        this.professor = usuario;
    }

    public Curso professor(Usuario usuario) {
        this.setProfessor(usuario);
        return this;
    }

    public Usuario getAluno() {
        return this.aluno;
    }

    public void setAluno(Usuario usuario) {
        this.aluno = usuario;
    }

    public Curso aluno(Usuario usuario) {
        this.setAluno(usuario);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Curso)) {
            return false;
        }
        return id != null && id.equals(((Curso) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Curso{" +
            "id=" + getId() +
            ", titulo='" + getTitulo() + "'" +
            ", duracaoCH=" + getDuracaoCH() +
            ", descricao='" + getDescricao() + "'" +
            ", valor=" + getValor() +
            ", criacao='" + getCriacao() + "'" +
            "}";
    }
}
