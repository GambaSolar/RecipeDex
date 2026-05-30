package es.solsaraguille.recipespring.controllers;

import es.solsaraguille.recipespring.dto.RecipeDTO;
import es.solsaraguille.recipespring.entities.*;
import es.solsaraguille.recipespring.repositories.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
@CrossOrigin
public class RecipeController {

    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
    private final IngredientRepository ingredientRepository;
    private final ReviewRepository reviewRepository;

    public RecipeController(RecipeRepository recipeRepository,
                            UserRepository userRepository,
                            IngredientRepository ingredientRepository,
                            ReviewRepository reviewRepository) {
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
        this.ingredientRepository = ingredientRepository;
        this.reviewRepository = reviewRepository;
    }

    private RecipeDTO toDTO(Recipe r) {
        RecipeDTO dto = new RecipeDTO();
        dto.id = r.getId();
        dto.name = r.getName();
        dto.description = r.getDescription();
        dto.preparationTime = r.getPreparationTime();
        return dto;
    }

    @GetMapping
    public ResponseEntity<List<RecipeDTO>> getAll() {
        return ResponseEntity.ok(
                recipeRepository.findAll()
                        .stream()
                        .map(this::toDTO)
                        .toList()
        );
    }

    public static class RecipeDetailResponse {
        public RecipeDTO recipe;
        public double averageRating;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {

        Recipe recipe = recipeRepository.findById(id).orElse(null);

        if (recipe == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Recipe not found");
        }

        List<Review> reviews = reviewRepository.findByRecipe(recipe);

        double avgRating = reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);

        RecipeDetailResponse response = new RecipeDetailResponse();
        response.recipe = toDTO(recipe);
        response.averageRating = avgRating;

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestParam Integer userId,
                                    @RequestBody Recipe recipe) {

        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found");
        }

        recipe.setUser(user);

        return ResponseEntity.ok(recipeRepository.save(recipe));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id,
                                    @RequestBody Recipe updatedRecipe) {

        Recipe recipe = recipeRepository.findById(id).orElse(null);

        if (recipe == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Recipe not found");
        }

        recipe.setName(updatedRecipe.getName());
        recipe.setDescription(updatedRecipe.getDescription());
        recipe.setPreparationTime(updatedRecipe.getPreparationTime());

        return ResponseEntity.ok(recipeRepository.save(recipe));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getByUser(@PathVariable Integer userId) {

        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found");
        }

        return ResponseEntity.ok(
                recipeRepository.findByUser(user)
                        .stream()
                        .map(this::toDTO)
                        .toList()
        );
    }

    @GetMapping("/search")
    public ResponseEntity<List<RecipeDTO>> search(@RequestParam String name) {
        return ResponseEntity.ok(
                recipeRepository.findByNameContainingIgnoreCase(name)
                        .stream()
                        .map(this::toDTO)
                        .toList()
        );
    }

    @GetMapping("/filter")
    public ResponseEntity<?> filterByIngredient(@RequestParam Integer ingredientId) {

        Ingredient ingredient = ingredientRepository.findById(ingredientId).orElse(null);

        if (ingredient == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Ingredient not found");
        }

        return ResponseEntity.ok(
                recipeRepository.findAll()
                        .stream()
                        .filter(r -> r.getRecipeIngredients()
                                .stream()
                                .anyMatch(ri -> ri.getIngredient()
                                        .getId()
                                        .equals(ingredientId)))
                        .map(this::toDTO)
                        .toList()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {

        if (!recipeRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Recipe not found");
        }

        recipeRepository.deleteById(id);

        return ResponseEntity.ok("Recipe deleted successfully");
    }

    @GetMapping("/search/advanced")
    public ResponseEntity<?> advancedSearch(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer ingredientId,
            @RequestParam(required = false) Integer maxTime
    ) {

        return ResponseEntity.ok(
                recipeRepository.findAll()
                        .stream()
                        .filter(r -> name == null || r.getName().toLowerCase().contains(name.toLowerCase()))
                        .filter(r -> ingredientId == null ||
                                r.getRecipeIngredients()
                                        .stream()
                                        .anyMatch(ri -> ri.getIngredient().getId().equals(ingredientId)))
                        .filter(r -> maxTime == null || r.getPreparationTime() <= maxTime)
                        .map(this::toDTO)
                        .toList()
        );
    }
}