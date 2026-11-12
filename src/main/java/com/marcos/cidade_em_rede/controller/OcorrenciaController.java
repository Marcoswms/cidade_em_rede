package com.marcos.cidade_em_rede.controller;

import com.marcos.cidade_em_rede.dto.AtualizarStatusDTO;
import com.marcos.cidade_em_rede.dto.OcorrenciaRequestDTO;
import com.marcos.cidade_em_rede.dto.OcorrenciaResponseDTO;
import com.marcos.cidade_em_rede.entity.Ocorrencia;
import com.marcos.cidade_em_rede.enums.CategoriaOcorrencia;
import com.marcos.cidade_em_rede.enums.StatusOcorrencia;
import com.marcos.cidade_em_rede.service.OcorrenciaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @GetMapping("/paginadas")
    public ResponseEntity<Page<OcorrenciaResponseDTO>> listarPaginadas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dataCriacao,desc") String[] sort) {
        Sort.Direction direcao = Sort.Direction.fromString(sort[1]);

        Pageable pageable = PageRequest.of(page, size, Sort.by(direcao, sort[0]));

        Page<OcorrenciaResponseDTO> ocorrencias = ocorrenciaService.listatOcorrenciasPaginadas(pageable).map(OcorrenciaResponseDTO::new);
        return ResponseEntity.ok(ocorrencias);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OcorrenciaResponseDTO> buscarPorId(@PathVariable Long id) {
        return ocorrenciaService.buscarPorId(id)
                .map(OcorrenciaResponseDTO::new)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/protocolo/{protocolo}")
    public ResponseEntity<OcorrenciaResponseDTO> buscarPorProtocolo(@PathVariable String protocolo) {
        return ocorrenciaService.buscarPorProtocolo(protocolo)
                .map(OcorrenciaResponseDTO::new)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<OcorrenciaResponseDTO>> listarPorCategoria(@PathVariable CategoriaOcorrencia categoria) {
        List<OcorrenciaResponseDTO> ocorrencias = ocorrenciaService.listarPorCategoria(categoria)
                .stream()
                .map(OcorrenciaResponseDTO::new)
                .toList();

        return ResponseEntity.ok(ocorrencias);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<OcorrenciaResponseDTO>> listarPorStatus(@PathVariable StatusOcorrencia status) {
        List<OcorrenciaResponseDTO> ocorrencias = ocorrenciaService.listarPorStatus(status)
                .stream()
                .map(OcorrenciaResponseDTO::new)
                .toList();

        return ResponseEntity.ok(ocorrencias);
    }

    @GetMapping("/filtros")
    public ResponseEntity<List<OcorrenciaResponseDTO>> filtrar(@RequestParam CategoriaOcorrencia categoria, @RequestParam StatusOcorrencia status) {

        List<OcorrenciaResponseDTO> ocorrencias = ocorrenciaService.listarPorCategoriaEStatus(categoria, status)
                .stream()
                .map(OcorrenciaResponseDTO::new)
                .toList();

        return ResponseEntity.ok(ocorrencias);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<OcorrenciaResponseDTO> atualizarStatus(@PathVariable Long id, @Valid @RequestBody AtualizarStatusDTO dados) {
        StatusOcorrencia novoStatus = dados.getStatus();
        return ocorrenciaService.atualizarStatus(id, novoStatus)
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