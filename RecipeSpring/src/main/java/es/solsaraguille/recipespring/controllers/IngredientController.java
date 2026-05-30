package es.solsaraguille.recipespring.controllers;

import es.solsaraguille.recipespring.entities.Ingredient;
import es.solsaraguille.recipespring.repositories.IngredientRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

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
    public ResponseEntity<Ingredient> create(@RequestBody Ingredient ingredient) {

        if (ingredient.getName() == null || ingredient.getName().trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        String name = ingredient.getName().trim();

        Optional<Ingredient> existing = ingredientRepository
                .findAll()
                .stream()
                .filter(i -> i.getName().equalsIgnoreCase(name))
                .findFirst();

        if (existing.isPresent()) {
            return ResponseEntity.ok(existing.get());
        }

        Ingredient newIng = new Ingredient();
        newIng.setName(name);

        return ResponseEntity.ok(ingredientRepository.save(newIng));
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<Ingredient>> bulkFindOrCreate(@RequestBody List<String> names) {

        if (names == null || names.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        List<String> cleanNames = names.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(n -> !n.isEmpty())
                .distinct()
                .toList();

        List<Ingredient> existing = ingredientRepository.findAll();

        Map<String, Ingredient> map = existing.stream()
                .collect(Collectors.toMap(
                        i -> i.getName().toLowerCase(),
                        i -> i,
                        (a, b) -> a
                ));

        List<Ingredient> result = new ArrayList<>();

        for (String name : cleanNames) {

            String key = name.toLowerCase();

            if (map.containsKey(key)) {
                result.add(map.get(key));
            } else {
                Ingredient newIng = new Ingredient();
                newIng.setName(name);

                Ingredient saved = ingredientRepository.save(newIng);

                map.put(key, saved);
                result.add(saved);
            }
        }

        return ResponseEntity.ok(result);
    }
}