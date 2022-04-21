package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Curso} entity.
 */
public class CursoDTO implements Serializable {

    private Long id;

    @NotNull
    private String titulo;

    private Double duracaoCH;

    private String descricao;

    private Double valor;

    private ZonedDateTime criacao;

    private UsuarioDTO professor;

    private UsuarioDTO aluno;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Double getDuracaoCH() {
        return duracaoCH;
    }

    public void setDuracaoCH(Double duracaoCH) {
        this.duracaoCH = duracaoCH;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public ZonedDateTime getCriacao() {
        return criacao;
    }

    public void setCriacao(ZonedDateTime criacao) {
        this.criacao = criacao;
    }

    public UsuarioDTO getProfessor() {
        return professor;
    }

    public void setProfessor(UsuarioDTO professor) {
        this.professor = professor;
    }

    public UsuarioDTO getAluno() {
        return aluno;
    }

    public void setAluno(UsuarioDTO aluno) {
        this.aluno = aluno;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CursoDTO)) {
            return false;
        }

        CursoDTO cursoDTO = (CursoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cursoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CursoDTO{" +
            "id=" + getId() +
            ", titulo='" + getTitulo() + "'" +
            ", duracaoCH=" + getDuracaoCH() +
            ", descricao='" + getDescricao() + "'" +
            ", valor=" + getValor() +
            ", criacao='" + getCriacao() + "'" +
            ", professor=" + getProfessor() +
            ", aluno=" + getAluno() +
            "}";
    }
}
