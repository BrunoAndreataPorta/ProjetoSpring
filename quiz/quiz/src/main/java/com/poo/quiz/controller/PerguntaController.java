package com.poo.quiz.controller;

import com.poo.quiz.model.Pergunta;
import com.poo.quiz.repository.PerguntaRepository;
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

    @GetMapping
    List<Pergunta> todasPerguntas(){
        return repository.findAll();
    }

    @PostMapping
    Pergunta novaPergunta(@RequestBody Pergunta pergunta){
        return repository.save(pergunta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pergunta> umaPergunta(@PathVariable Long id) {
        Optional<Pergunta> pergunta = repository.findById(id);
        return pergunta.map(ResponseEntity::ok).orElseGet(() ->
                ResponseEntity.notFound().build());
    }

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPergunta(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }


}
