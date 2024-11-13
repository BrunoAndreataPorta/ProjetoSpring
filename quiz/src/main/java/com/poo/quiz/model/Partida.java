package com.poo.quiz.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Partida {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private int pontuacao;
    private int chances;

    Partida(int pontuacao, int chances) {
        this.pontuacao = 0;
        this.chances = 3;
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
