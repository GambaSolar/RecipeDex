package es.solsaraguille.recipespring.repositories;

import es.solsaraguille.recipespring.entities.Favorite;
import es.solsaraguille.recipespring.entities.Ingredient;
import es.solsaraguille.recipespring.entities.Recipe;
import es.solsaraguille.recipespring.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {

    List<Favorite> findByRecipe(Recipe recipe);

    List<Favorite> findByUser(User user);

    Optional<Favorite> findByRecipeAndUser(Recipe recipe, User user);

    boolean existsByRecipeAndUser(Recipe recipe, User user);

    void deleteByRecipeAndUser(Recipe recipe, User user);

}