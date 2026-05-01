package es.solsaraguille.recipespring.controllers;


import es.solsaraguille.recipespring.entities.Ingredient;
import es.solsaraguille.recipespring.entities.Recipe;
import es.solsaraguille.recipespring.entities.RecipeIngredient;
import es.solsaraguille.recipespring.repositories.IngredientRepository;
import es.solsaraguille.recipespring.repositories.RecipeIngredientRepository;
import es.solsaraguille.recipespring.repositories.RecipeRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping
@CrossOrigin
public class RecipeIngredientController {

    private RecipeIngredientRepository recipeIngredientRepository;
    private RecipeRepository recipeRepository;
    private IngredientRepository ingredientRepository;

    public RecipeIngredientController(RecipeIngredientRepository repo,
                                      RecipeRepository recipeRepository,
                                      IngredientRepository ingredientRepository){

        this.recipeIngredientRepository = repo;
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;

    }

    @PostMapping
    public RecipeIngredient add(@RequestParam Integer recipeId, @RequestParam Integer ingredientId){

        Recipe recipe =  recipeRepository.findById(recipeId).orElseThrow();
        Ingredient ingredient = ingredientRepository.findById(ingredientId).orElseThrow();

        RecipeIngredient ri = new RecipeIngredient();
        ri.setRecipe(recipe);
        ri.setIngredient(ingredient);

        return recipeIngredientRepository.save(ri);
    }

    @GetMapping("/ingredient/{ingredientId}")
    public List<Recipe> getRecipesByIngredient(@PathVariable Integer ingredientId){
        Ingredient ingredient = ingredientRepository.findById(ingredientId).orElseThrow();

        return recipeIngredientRepository.findByIngredient(ingredient)
                .stream()
                .map(RecipeIngredient::getRecipe)
                .collect(Collectors.toList());
    }

}