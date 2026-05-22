package es.solsaraguille.recipespring.repositories;

import es.solsaraguille.recipespring.entities.Recipe;
import es.solsaraguille.recipespring.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Integer> {

    List<Recipe> findByUser(User user);
    List<Recipe> findByNameContainingIgnoreCase (String name);

}