package es.solsaraguille.recipespring.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "resena")
public class Resena {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resenaid")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "usuarioid")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "recetaid")
    private Recetas receta;

    @Column(name = "valoracion")
    private Integer valoracion;

    @Column(name = "comentario", columnDefinition = "TEXT")
    private String comentario;
}