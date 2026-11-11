package com.marcos.cidade_em_rede.repository;

import com.marcos.cidade_em_rede.entity.Ocorrencia;
import com.marcos.cidade_em_rede.enums.CategoriaOcorrencia;
import com.marcos.cidade_em_rede.enums.StatusOcorrencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OcorrenciaRepository extends JpaRepository<Ocorrencia, Long> {
    Optional<Ocorrencia> findByProtocolo(String protocolo);

    List<Ocorrencia> findByCategoria(CategoriaOcorrencia categoria);

    List<Ocorrencia> findByStatus(StatusOcorrencia status);

    List<Ocorrencia> findByCategoriaAndStatus(CategoriaOcorrencia categoria, StatusOcorrencia status);
}