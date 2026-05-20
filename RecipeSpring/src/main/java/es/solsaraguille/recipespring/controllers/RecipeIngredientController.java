package es.solsaraguille.recipespring.controllers;

import es.solsaraguille.recipespring.entities.*;
import es.solsaraguille.recipespring.repositories.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/recipe-ingredients")
@CrossOrigin
public class RecipeIngredientController {

    private final RecipeIngredientRepository recipeIngredientRepository;
    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;

    public RecipeIngredientController(RecipeIngredientRepository repo,
                                      RecipeRepository recipeRepository,
                                      IngredientRepository ingredientRepository) {

        this.recipeIngredientRepository = repo;
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
    }

    @PostMapping
    public ResponseEntity<?> add(@RequestParam Integer recipeId,
                                 @RequestParam Integer ingredientId) {

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElse(null);

        if (recipe == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Recipe not found");
        }

        Ingredient ingredient = ingredientRepository.findById(ingredientId)
                .orElse(null);

        if (ingredient == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Ingredient not found");
        }

        RecipeIngredient ri = new RecipeIngredient();
        ri.setRecipe(recipe);
        ri.setIngredient(ingredient);

        return ResponseEntity.ok(recipeIngredientRepository.save(ri));
    }

    @GetMapping("/ingredient/{ingredientId}")
    public ResponseEntity<?> getRecipesByIngredient(@PathVariable Integer ingredientId) {

        Ingredient ingredient = ingredientRepository.findById(ingredientId)
                .orElse(null);

        if (ingredient == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Ingredient not found");
        }

        List<Recipe> recipes = recipeIngredientRepository.findByIngredient(ingredient)
                .stream()
                .map(RecipeIngredient::getRecipe)
                .collect(Collectors.toList());

        return ResponseEntity.ok(recipes);
    }
}