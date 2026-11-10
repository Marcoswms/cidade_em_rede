package com.marcos.cidade_em_rede.dto;

import com.marcos.cidade_em_rede.enums.StatusOcorrencia;
import jakarta.validation.constraints.NotNull;

public class AtualizarStatusDTO {

    @NotNull(message = "O novo status da ocorrência é obrigatório.")
    private StatusOcorrencia status;

    public StatusOcorrencia getStatus() {
        return status;
    }

    public void setStatus(StatusOcorrencia status) {
        this.status = status;
    }
}