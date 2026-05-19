package es.solsaraguille.recipespring.controllers;

import es.solsaraguille.recipespring.entities.Favorite;
import es.solsaraguille.recipespring.entities.Recipe;
import es.solsaraguille.recipespring.entities.User;
import es.solsaraguille.recipespring.repositories.FavoriteRepository;
import es.solsaraguille.recipespring.repositories.RecipeRepository;
import es.solsaraguille.recipespring.repositories.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/favorites")
@CrossOrigin
public class FavoriteController {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;

    public FavoriteController(FavoriteRepository favoriteRepository,
                              UserRepository userRepository,
                              RecipeRepository recipeRepository) {
        this.favoriteRepository = favoriteRepository;
        this.userRepository = userRepository;
        this.recipeRepository = recipeRepository;
    }

    @PostMapping
    public Favorite add(@RequestParam Integer userId,
                        @RequestParam Integer recipeId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        if (favoriteRepository.existsByRecipeAndUser(recipe, user)) {
            return null;
        }

        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setRecipe(recipe);

        return favoriteRepository.save(favorite);
    }

    @DeleteMapping
    public void delete(@RequestParam Integer userId,
                       @RequestParam Integer recipeId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        favoriteRepository.deleteByRecipeAndUser(recipe, user);
    }

    @GetMapping("/user/{userId}")
    public List<Recipe> getFavorites(@PathVariable Integer userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return favoriteRepository.findByUser(user)
                .stream()
                .map(Favorite::getRecipe)
                .collect(Collectors.toList());
    }
}