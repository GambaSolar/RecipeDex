package es.solsaraguille.recipespring.repositories;

import es.solsaraguille.recipespring.entities.Ingredient;
import es.solsaraguille.recipespring.entities.Recipe;
import es.solsaraguille.recipespring.entities.RecipeIngredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Integer> {

    List<RecipeIngredient> findByRecipe(Recipe recipe);

    List<RecipeIngredient> findByIngredient(Ingredient ingredient);

}