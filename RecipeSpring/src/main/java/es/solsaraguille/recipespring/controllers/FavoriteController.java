package es.solsaraguille.recipespring.controllers;

import es.solsaraguille.recipespring.entities.Favorite;
import es.solsaraguille.recipespring.entities.Recipe;
import es.solsaraguille.recipespring.entities.User;
import es.solsaraguille.recipespring.repositories.FavoriteRepository;
import es.solsaraguille.recipespring.repositories.RecipeRepository;
import es.solsaraguille.recipespring.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> add(@RequestParam Integer userId,
                                 @RequestParam Integer recipeId) {

        User user = userRepository.findById(userId)
                .orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found");
        }

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElse(null);

        if (recipe == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Recipe not found");
        }

        if (favoriteRepository.existsByRecipeAndUser(recipe, user)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Favorite already exists");
        }

        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setRecipe(recipe);

        return ResponseEntity.ok(favoriteRepository.save(favorite));
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@RequestParam Integer userId,
                                    @RequestParam Integer recipeId) {

        User user = userRepository.findById(userId)
                .orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found");
        }

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElse(null);

        if (recipe == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Recipe not found");
        }

        if (!favoriteRepository.existsByRecipeAndUser(recipe, user)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Favorite not found");
        }

        favoriteRepository.deleteByRecipeAndUser(recipe, user);

        return ResponseEntity.ok("Favorite removed successfully");
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getFavorites(@PathVariable Integer userId) {

        User user = userRepository.findById(userId)
                .orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found");
        }

        List<Recipe> favorites = favoriteRepository.findByUser(user)
                .stream()
                .map(Favorite::getRecipe)
                .collect(Collectors.toList());

        return ResponseEntity.ok(favorites);
    }

    @GetMapping("/check")
    public ResponseEntity<?> isFavorite(@RequestParam Integer userId,
                                        @RequestParam Integer recipeId) {

        User user = userRepository.findById(userId)
                .orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found");
        }

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElse(null);

        if (recipe == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Recipe not found");
        }

        boolean exists = favoriteRepository.existsByRecipeAndUser(recipe, user);

        return ResponseEntity.ok(exists);
    }
}