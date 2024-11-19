package com.poo.quiz.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Pergunta {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String pergunta;
    private String opcaoA;
    private String opcaoB;
    private String opcaoC;
    private String opcaoD;
    private String resposta;
    @ManyToOne
    @JoinColumn(name = "partida_id") // Vira chave estrangeira no tabela 'Partida'
    @JsonBackReference
    private Partida partida;
}
