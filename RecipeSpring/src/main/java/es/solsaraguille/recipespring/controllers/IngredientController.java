package es.solsaraguille.recipespring.controllers;

import es.solsaraguille.recipespring.entities.Ingredient;
import es.solsaraguille.recipespring.repositories.IngredientRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ingredients")
@CrossOrigin
public class IngredientController {

    private final IngredientRepository ingredientRepository;

    public IngredientController(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    @GetMapping
    public ResponseEntity<List<Ingredient>> getAll() {
        return ResponseEntity.ok(ingredientRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Ingredient ingredient) {

        if (ingredient.getName() == null || ingredient.getName().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Ingredient name cannot be empty");
        }

        boolean exists = ingredientRepository.findAll()
                .stream()
                .anyMatch(i -> i.getName().equalsIgnoreCase(ingredient.getName()));

        if (exists) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Ingredient already exists");
        }

        return ResponseEntity.ok(ingredientRepository.save(ingredient));
    }
}