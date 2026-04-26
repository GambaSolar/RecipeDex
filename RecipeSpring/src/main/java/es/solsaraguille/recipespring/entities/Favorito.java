package es.solsaraguille.recipespring.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "favorito")
public class Favorito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favoritoid")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "usuarioid")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "recetaid")
    private Recetas receta;
}
