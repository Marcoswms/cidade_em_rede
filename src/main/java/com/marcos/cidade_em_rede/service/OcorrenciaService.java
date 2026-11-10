package com.marcos.cidade_em_rede.service;


import com.marcos.cidade_em_rede.entity.HistoricoStatus;
import com.marcos.cidade_em_rede.entity.Ocorrencia;
import com.marcos.cidade_em_rede.enums.StatusOcorrencia;
import com.marcos.cidade_em_rede.exception.OcorrenciaEncerradaException;
import com.marcos.cidade_em_rede.repository.HistoricoStatusRepository;
import com.marcos.cidade_em_rede.repository.OcorrenciaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OcorrenciaService {

    private final OcorrenciaRepository ocorrenciaRepository;
    private final HistoricoStatusRepository historicoStatusRepository;

    public OcorrenciaService(OcorrenciaRepository ocorrenciaRepository, HistoricoStatusRepository historicoStatusRepository) {
        this.ocorrenciaRepository = ocorrenciaRepository;
        this.historicoStatusRepository = historicoStatusRepository;
    }

    @Transactional
    public Ocorrencia registrarOcorrencia(Ocorrencia ocorrencia) {
        ocorrencia.setProtocolo(
                UUID.randomUUID().toString().replace("-", "").toUpperCase()
        );
        ocorrencia.setStatus(StatusOcorrencia.RECEBIDO);
        ocorrencia.setDataCriacao(LocalDateTime.now());

        Ocorrencia ocorrenciaSalva = ocorrenciaRepository.save(ocorrencia);

        HistoricoStatus historico = new HistoricoStatus();

        historico.setStatus(ocorrenciaSalva.getStatus());
        historico.setDataAlteracao(LocalDateTime.now());
        historico.setOcorrencia(ocorrenciaSalva);

        historicoStatusRepository.save(historico);
        return ocorrenciaSalva;
    }

    public List<Ocorrencia> listarOcorrencias() {
        return ocorrenciaRepository.findAll();
    }

    public Optional<Ocorrencia> buscarPorId(Long id) {
        return ocorrenciaRepository.findById(id);
    }

    @Transactional
    public Optional<Ocorrencia> atualizarStatus(Long id, StatusOcorrencia novoStatus) {
        Optional<Ocorrencia> resultado = ocorrenciaRepository.findById(id);

        if (resultado.isEmpty()) {
            return Optional.empty();
        }

        Ocorrencia ocorrencia = resultado.get();

        if (ocorrencia.getStatus() == StatusOcorrencia.ENCERRADO) {
            throw new OcorrenciaEncerradaException("Não é possível alterar o status de uma ocorrência encerrada.");
        }

        if (ocorrencia.getStatus() == novoStatus) {
            return Optional.of(ocorrencia);
        }
        ocorrencia.setStatus(novoStatus);
        ocorrenciaRepository.save(ocorrencia);

        HistoricoStatus historico = new HistoricoStatus();

        historico.setStatus(novoStatus);
        historico.setDataAlteracao(LocalDateTime.now());
        historico.setOcorrencia(ocorrencia);

        historicoStatusRepository.save(historico);
        return Optional.of(ocorrencia);
    }

    public Optional<Ocorrencia> atualizarPorId(Long id, Ocorrencia dadosAtualizados) {
        return ocorrenciaRepository.findById(id)
                .map(ocorrencia -> {
                    ocorrencia.setDescricao(dadosAtualizados.getDescricao());
                    ocorrencia.setCategoria(dadosAtualizados.getCategoria());
                    ocorrencia.setLatitude(dadosAtualizados.getLatitude());
                    ocorrencia.setLongitude(dadosAtualizados.getLongitude());

                    return ocorrenciaRepository.save(ocorrencia);
                });
    }

    public boolean deletarPorId(Long id) {
        if (!ocorrenciaRepository.existsById(id)) {
            return false;
        }
        ocorrenciaRepository.deleteById(id);
        return true;
    }
}