package com.poo.quiz.controller;

import com.poo.quiz.model.Partida;
import com.poo.quiz.model.Pergunta;
import com.poo.quiz.repository.PartidaRepository;
import com.poo.quiz.repository.PerguntaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @GetMapping
    public List<Partida> todasPartidas(){
        return partidaRepository.findAll();
    }

    @PostMapping
    public Partida novaPartida(){
        List<Pergunta> todasPerguntas = perguntaRepository.findAll();
        Partida partida = new Partida();
        partida.setPerguntas(todasPerguntas);

        return partidaRepository.save(partida);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Partida> umaPartida(@PathVariable Long id) {
        Optional<Partida> partida = partidaRepository.findById(id);
        return partida.map(ResponseEntity::ok).orElseGet(() ->
                ResponseEntity.notFound().build());
    }

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPartida(@PathVariable Long id) {
        if (partidaRepository.existsById(id)) {
            partidaRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

}
