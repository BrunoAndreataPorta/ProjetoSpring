package com.poo.quiz.controller;

import com.poo.quiz.model.Partida;
import com.poo.quiz.repository.PartidaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/partidas")
public class PartidaController {
    private final PartidaRepository repository;

    public PartidaController(PartidaRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    List<Partida> todasPartidas(){
        return repository.findAll();
    }

    @PostMapping
    Partida novaPartida(@RequestBody Partida partida){
        return repository.save(partida);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Partida> umaPartida(@PathVariable Long id) {
        Optional<Partida> partida = repository.findById(id);
        return partida.map(ResponseEntity::ok).orElseGet(() ->
                ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Partida> alterarPartida(@RequestBody Partida novaPartida, @PathVariable Long id) {

        return repository.findById(id)
                .map(partida -> {
                    partida.setChances(novaPartida.getChances());
                    partida.setPontuacao(novaPartida.getPontuacao());
                    return ResponseEntity.ok(repository.save(partida));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPartida(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

}
