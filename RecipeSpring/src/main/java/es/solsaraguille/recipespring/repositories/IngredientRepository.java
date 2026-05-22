package es.solsaraguille.recipespring.repositories;

import es.solsaraguille.recipespring.entities.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IngredientRepository extends JpaRepository<Ingredient, Integer> {

    Optional<Ingredient> findByName(String name);

}