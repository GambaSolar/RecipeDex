package es.solsaraguille.recipespring.porsiacaso;

import jakarta.persistence.*;

@Entity
@Table(name = "ingredientes")
public class Ingredientes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ingredienteid")
    private Integer id;

    @Column(name = "nombre")
    private String nombre;
}