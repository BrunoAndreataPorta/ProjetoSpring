package com.poo.quiz.controller;

import com.poo.quiz.model.Partida;
import com.poo.quiz.model.Pergunta;
import com.poo.quiz.repository.PartidaRepository;
import com.poo.quiz.repository.PerguntaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/partidas")
public class PartidaController {
    private final PartidaRepository partidaRepository;
    private final PerguntaRepository perguntaRepository;

    public PartidaController(PartidaRepository partidaRepository, PerguntaRepository perguntaRepository) {
        this.partidaRepository = partidaRepository;
        this.perguntaRepository = perguntaRepository;
    }

    @Operation(
            summary = "Lista todas as partidas",
            description = "Retorna uma lista de todas as partidas cadastradas."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de partidas retornada com sucesso.")
    })
    @GetMapping
    public List<Partida> todasPartidas(){
        return partidaRepository.findAll();
    }

    @Operation(
            summary = "Cria uma nova partida",
            description = "Cria uma nova partida com todas as perguntas disponíveis."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Partida criada com sucesso.")
    })
    @PostMapping
    public Partida novaPartida(){
        List<Pergunta> todasPerguntas = perguntaRepository.findAll();
        Partida partida = new Partida();
        partida.setPerguntas(todasPerguntas);

        return partidaRepository.save(partida);
    }

    @Operation(
            summary = "Busca uma partida pelo ID",
            description = "Retorna uma partida específica dado o seu ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Partida encontrada."),
            @ApiResponse(responseCode = "404", description = "Partida não encontrada.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Partida> umaPartida(@PathVariable Long id) {
        Optional<Partida> partida = partidaRepository.findById(id);
        return partida.map(ResponseEntity::ok).orElseGet(() ->
                ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Altera os dados de uma partida",
            description = "Atualiza as informações de uma partida específica, como chances e pontuação."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Partida atualizada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Requisição mal formada."),
            @ApiResponse(responseCode = "404", description = "Partida não encontrada.")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Partida> alterarPartida(@RequestBody Partida novaPartida, @PathVariable Long id) {

        return partidaRepository.findById(id)
                .map(partida -> {
                    partida.setChances(novaPartida.getChances());
                    partida.setPontuacao(novaPartida.getPontuacao());
                    return ResponseEntity.ok(partidaRepository.save(partida));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @Operation(
            summary = "Deleta uma partida",
            description = "Deleta uma partida existente a partir de seu ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Partida deletada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Partida não encontrada.")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPartida(@PathVariable Long id) {
        if (partidaRepository.existsById(id)) {
            partidaRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Responde uma pergunta durante a partida",
            description = "Permite que o jogador responda uma pergunta durante uma partida, atualizando a pontuação e chances."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resposta processada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Requisição mal formada."),
            @ApiResponse(responseCode = "404", description = "Partida ou pergunta não encontrada.")
    })
    @PostMapping("/{partidaId}/responder")
    public ResponseEntity<?> responderPergunta(
            @PathVariable Long partidaId,
            @RequestParam Long perguntaId,
            @RequestParam String resposta) {
        // Busca a partida
        Partida partida = partidaRepository.findById(partidaId)
                .orElseThrow(() -> new RuntimeException("Partida não encontrada"));

        // Busca a pergunta
        Pergunta pergunta = perguntaRepository.findById(perguntaId)
                .orElseThrow(() -> new RuntimeException("Pergunta não encontrada"));

        // Processa a resposta
        boolean respostaCorreta = partida.responder(resposta, pergunta);

        // Salva a partida atualizada
        partidaRepository.save(partida);

        // Retorna o resultado como JSON
        return ResponseEntity.ok(Map.of(
                "respostaCorreta", respostaCorreta,
                "pontuacao", partida.getPontuacao(),
                "chances", partida.getChances(),
                "fimDeJogo", partida.fimJogo()
        ));
    }

}
