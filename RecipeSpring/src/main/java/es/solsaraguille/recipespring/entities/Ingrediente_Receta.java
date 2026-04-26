package es.solsaraguille.recipespring.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "ingrediente_receta")
public class Ingrediente_Receta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "relacionid")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "ingredienteid")
    private Ingredientes ingredientes;

    @ManyToOne
    @JoinColumn(name = "recetaid")
    private Recetas recetas;
}
