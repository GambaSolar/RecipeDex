package es.solsaraguille.recipespring.repositories;

import es.solsaraguille.recipespring.entities.Recipe;
import es.solsaraguille.recipespring.entities.Review;
import es.solsaraguille.recipespring.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

       List<Review> findByRecipe(Recipe recipe);

       List<Review> findByUser(User user);

       Optional<Review> findByRecipeAndUser(Recipe recipe, User user);


}