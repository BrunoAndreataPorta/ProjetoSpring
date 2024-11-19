package com.poo.quiz.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class Partida {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private int pontuacao;
    private int chances;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "partida_id") // Chave estrangeira 'Pergunta'
    @JsonManagedReference
    private List<Pergunta> perguntas = new ArrayList<>();

    public Partida() {
        this.pontuacao = 0;
        this.chances = 3;
    }

    public Partida(List<Pergunta> perguntas) {
        this();
        this.perguntas.addAll(perguntas);
    }

    public Boolean responder(String resposta, Pergunta perguntaResposta){
        if (perguntaResposta.getResposta().equals(resposta)) {
            pontuacao++;
            return true;
        }
        this.chances--;
        return false;
    }

    public Boolean fimJogo(){
        return (chances < 0);
    }

}
