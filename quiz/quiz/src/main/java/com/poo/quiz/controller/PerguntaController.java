package com.poo.quiz.controller;

import com.poo.quiz.model.Pergunta;
import com.poo.quiz.repository.PerguntaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/perguntas")
public class PerguntaController {
    private final PerguntaRepository repository;

    public PerguntaController(PerguntaRepository repository) {
        this.repository = repository;
    }

    @Operation(
            summary = "Lista todas as perguntas",
            description = "Retorna uma lista de todas as perguntas cadastradas."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de perguntas retornada com sucesso")
    })
    @GetMapping
    public List<Pergunta> todasPerguntas(){
        return repository.findAll();
    }

    @Operation(
            summary = "Cria uma nova pergunta e salva no banco",
            description = "Recebe os dados de uma nova pergunta e a armazena no banco de dados."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pergunta criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PostMapping
    public Pergunta novaPergunta(@RequestBody Pergunta pergunta){
        return repository.save(pergunta);
    }

    @Operation(
            summary = "Retorna uma pergunta específica pelo ID",
            description = "Recebe o ID de uma pergunta e retorna os dados dela. Se não encontrada, retorna erro 404."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pergunta encontrada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pergunta não encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Pergunta> umaPergunta(@PathVariable Long id) {
        Optional<Pergunta> pergunta = repository.findById(id);
        return pergunta.map(ResponseEntity::ok).orElseGet(() ->
                ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Atualiza uma pergunta existente",
            description = "Permite atualizar os dados de uma pergunta já cadastrada, com base no ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pergunta atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pergunta não encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Pergunta> alterarPergunta(@RequestBody Pergunta novaPergunta, @PathVariable Long id) {

        return repository.findById(id)
                .map(pergunta -> {
                    pergunta.setPergunta(novaPergunta.getPergunta());
                    pergunta.setOpcaoA(novaPergunta.getOpcaoA());
                    pergunta.setOpcaoB(novaPergunta.getOpcaoB());
                    pergunta.setOpcaoC(novaPergunta.getOpcaoC());
                    pergunta.setOpcaoD(novaPergunta.getOpcaoD());
                    pergunta.setResposta(novaPergunta.getResposta());
                    return ResponseEntity.ok(repository.save(pergunta));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Deleta uma pergunta pelo ID",
            description = "Deleta uma pergunta cadastrada no sistema com base no ID fornecido."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pergunta deletada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pergunta não encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPergunta(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }


}
