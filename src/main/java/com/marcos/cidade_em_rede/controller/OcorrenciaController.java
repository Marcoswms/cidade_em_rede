package com.marcos.cidade_em_rede.controller;


import com.marcos.cidade_em_rede.dto.OcorrenciaRequestDTO;
import com.marcos.cidade_em_rede.dto.OcorrenciaResponseDTO;
import com.marcos.cidade_em_rede.entity.Ocorrencia;
import com.marcos.cidade_em_rede.service.OcorrenciaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ocorrencias")
public class OcorrenciaController {

    private final OcorrenciaService ocorrenciaService;

    public OcorrenciaController(OcorrenciaService ocorrenciaService) {
        this.ocorrenciaService = ocorrenciaService;
    }

    @PostMapping
    public ResponseEntity<OcorrenciaResponseDTO> registrar(@Valid @RequestBody OcorrenciaRequestDTO dados) {
        Ocorrencia ocorrencia = new Ocorrencia();

        ocorrencia.setDescricao(dados.getDescricao());
        ocorrencia.setCategoria(dados.getCategoria());
        ocorrencia.setLatitude(dados.getLatitude());
        ocorrencia.setLongitude(dados.getLongitude());

        Ocorrencia novaOcorrencia = ocorrenciaService.registrarOcorrencia(ocorrencia);
        OcorrenciaResponseDTO resposta = new OcorrenciaResponseDTO(novaOcorrencia);

        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }

    @GetMapping
    public ResponseEntity<List<OcorrenciaResponseDTO>> listar() {
        List<OcorrenciaResponseDTO> ocorrencias = ocorrenciaService.listarOcorrencias()
                .stream()
                .map(OcorrenciaResponseDTO::new)
                .toList();
        return ResponseEntity.ok(ocorrencias);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OcorrenciaResponseDTO> buscarPorId(@PathVariable Long id) {
        return ocorrenciaService.buscarPorId(id)
                .map(OcorrenciaResponseDTO::new)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<OcorrenciaResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody OcorrenciaRequestDTO dadosAtualizados) {
        Ocorrencia ocorrencia = new Ocorrencia();

        ocorrencia.setDescricao(dadosAtualizados.getDescricao());
        ocorrencia.setCategoria(dadosAtualizados.getCategoria());
        ocorrencia.setLatitude(dadosAtualizados.getLatitude());
        ocorrencia.setLongitude(dadosAtualizados.getLongitude());

        return ocorrenciaService.atualizarPorId(id, ocorrencia)
                .map(OcorrenciaResponseDTO::new)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        boolean deletado = ocorrenciaService.deletarPorId(id);

        if (!deletado) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}