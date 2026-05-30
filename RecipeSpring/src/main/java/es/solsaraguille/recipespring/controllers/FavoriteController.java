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

        User user = userRepository.findById(userId).orElse(null);
        Recipe recipe = recipeRepository.findById(recipeId).orElse(null);

        if (user == null || recipe == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User or recipe not found");
        }

        if (favoriteRepository.existsByRecipeAndUser(recipe, user)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Already favorite");
        }

        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setRecipe(recipe);

        favoriteRepository.save(favorite);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@RequestParam Integer userId,
                                    @RequestParam Integer recipeId) {

        User user = userRepository.findById(userId).orElse(null);
        Recipe recipe = recipeRepository.findById(recipeId).orElse(null);

        if (user == null || recipe == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User or recipe not found");
        }

        return favoriteRepository.findByRecipeAndUser(recipe, user)
                .map(fav -> {
                    favoriteRepository.delete(fav);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Favorite not found"));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Recipe>> getFavorites(@PathVariable Integer userId) {

        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        List<Recipe> favorites = favoriteRepository.findByUser(user)
                .stream()
                .map(Favorite::getRecipe)
                .collect(Collectors.toList());

        return ResponseEntity.ok(favorites);
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> isFavorite(@RequestParam Integer userId,
                                              @RequestParam Integer recipeId) {

        User user = userRepository.findById(userId).orElse(null);
        Recipe recipe = recipeRepository.findById(recipeId).orElse(null);

        if (user == null || recipe == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        boolean exists = favoriteRepository.existsByRecipeAndUser(recipe, user);

        return ResponseEntity.ok(exists);
    }

    @GetMapping("/count/{recipeId}")
    public ResponseEntity<Integer> countFavorites(@PathVariable Integer recipeId) {

        Recipe recipe = recipeRepository.findById(recipeId).orElse(null);

        if (recipe == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        int total = favoriteRepository.findByRecipe(recipe).size();

        return ResponseEntity.ok(total);
    }
}