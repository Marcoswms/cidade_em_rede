package com.marcos.cidade_em_rede.entity;

import com.marcos.cidade_em_rede.enums.StatusOcorrencia;
import com.marcos.cidade_em_rede.enums.CategoriaOcorrencia;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ocorrencias")
public class Ocorrencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String protocolo;

    @NotBlank(message = "A descrição da ocorrência é obrigatória.")
    private String descricao;

    @NotNull(message = "A categoria da ocorrência é obrigatória.")
    @Enumerated(EnumType.STRING)
    private CategoriaOcorrencia categoria;

    @NotNull(message = "A latitude é obrigatória.")
    @DecimalMin(value = "-90.0", message = "A latitude não pode ser menor que -90.")
    @DecimalMax(value = "90.0", message = "A latitude não pode ser maior que 90.")
    private Double latitude;

    @NotNull(message = "A longitude é obrigatória.")
    @DecimalMin(value = "-180.0", message = "A longitude não pode ser menor que -180.")
    @DecimalMax(value = "180.0", message = "A longitude não pode ser maior que 180.")
    private Double longitude;
    private LocalDateTime dataCriacao;

    @Enumerated(EnumType.STRING)
    private StatusOcorrencia status;

    @OneToMany(mappedBy = "ocorrencia")
    private List<HistoricoStatus> historicos = new ArrayList<>();

    public Ocorrencia() {

    }

    public Long getId() {
        return id;
    }

    public String getProtocolo() {
        return protocolo;
    }

    public void setProtocolo (String protocolo) {
        this.protocolo = protocolo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public CategoriaOcorrencia getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaOcorrencia categoria) {
        this.categoria = categoria;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public StatusOcorrencia getStatus() {
        return status;
    }

    public void setStatus(StatusOcorrencia status) {
        this.status = status;
    }

    public List<HistoricoStatus> getHistoricos() {
        return historicos;
    }
}