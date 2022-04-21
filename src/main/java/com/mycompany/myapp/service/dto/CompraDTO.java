package com.mycompany.myapp.service.dto;

import com.mycompany.myapp.domain.enumeration.EstadoTransacao;
import com.mycompany.myapp.domain.enumeration.Pagamento;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Compra} entity.
 */
public class CompraDTO implements Serializable {

    private Long id;

    private Double percentualDesconto;

    private Double valorFinal;

    private ZonedDateTime dataCriacao;

    private Pagamento formaPagamento;

    private EstadoTransacao estado;

    private CursoDTO curso;

    private UsuarioDTO usuario;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getPercentualDesconto() {
        return percentualDesconto;
    }

    public void setPercentualDesconto(Double percentualDesconto) {
        this.percentualDesconto = percentualDesconto;
    }

    public Double getValorFinal() {
        return valorFinal;
    }

    public void setValorFinal(Double valorFinal) {
        this.valorFinal = valorFinal;
    }

    public ZonedDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(ZonedDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Pagamento getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(Pagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public EstadoTransacao getEstado() {
        return estado;
    }

    public void setEstado(EstadoTransacao estado) {
        this.estado = estado;
    }

    public CursoDTO getCurso() {
        return curso;
    }

    public void setCurso(CursoDTO curso) {
        this.curso = curso;
    }

    public UsuarioDTO getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioDTO usuario) {
        this.usuario = usuario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CompraDTO)) {
            return false;
        }

        CompraDTO compraDTO = (CompraDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, compraDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompraDTO{" +
            "id=" + getId() +
            ", percentualDesconto=" + getPercentualDesconto() +
            ", valorFinal=" + getValorFinal() +
            ", dataCriacao='" + getDataCriacao() + "'" +
            ", formaPagamento='" + getFormaPagamento() + "'" +
            ", estado='" + getEstado() + "'" +
            ", curso=" + getCurso() +
            ", usuario=" + getUsuario() +
            "}";
    }
}
