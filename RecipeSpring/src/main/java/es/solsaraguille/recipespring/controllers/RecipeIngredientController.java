package es.solsaraguille.recipespring.controllers;

import es.solsaraguille.recipespring.entities.*;
import es.solsaraguille.recipespring.repositories.*;
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
    public RecipeIngredient add(@RequestParam Integer recipeId,
                                @RequestParam Integer ingredientId) {

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        Ingredient ingredient = ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new RuntimeException("Ingredient not found"));

        RecipeIngredient ri = new RecipeIngredient();
        ri.setRecipe(recipe);
        ri.setIngredient(ingredient);

        return recipeIngredientRepository.save(ri);
    }

    @GetMapping("/ingredient/{ingredientId}")
    public List<Recipe> getRecipesByIngredient(@PathVariable Integer ingredientId) {

        Ingredient ingredient = ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new RuntimeException("Ingredient not found"));

        return recipeIngredientRepository.findByIngredient(ingredient)
                .stream()
                .map(RecipeIngredient::getRecipe)
                .collect(Collectors.toList());
    }
}