package es.solsaraguille.recipespring.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "recipe")
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipe_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"recipes", "favorites", "followers", "following"})
    private User user;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", nullable = false, length = 10000)
    private String description;

    @Column(name = "preparation_time", nullable = false)
    private Integer preparationTime;

    @OneToMany(mappedBy = "recipe")
    @JsonIgnoreProperties({"recipe"})
    private List<RecipeIngredient> recipeIngredients;

    @OneToMany(mappedBy = "recipe")
    @JsonIgnoreProperties({"recipe"})
    private List<Review> reviews;

    @OneToMany(mappedBy = "recipe")
    @JsonIgnoreProperties({"recipe"})
    private List<Favorite> favorites;
}