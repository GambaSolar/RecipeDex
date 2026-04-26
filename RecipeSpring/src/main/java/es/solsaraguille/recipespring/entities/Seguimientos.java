package es.solsaraguille.recipespring.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "seguimientos")
public class Seguimientos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seguimientoid")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "seguidorid")
    private Usuario seguidor;

    @ManyToOne
    @JoinColumn(name = "seguidoid")
    private Usuario seguido;
}
