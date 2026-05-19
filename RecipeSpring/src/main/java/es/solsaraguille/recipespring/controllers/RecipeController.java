package es.solsaraguille.recipespring.controllers;

import es.solsaraguille.recipespring.entities.Recipe;
import es.solsaraguille.recipespring.entities.User;
import es.solsaraguille.recipespring.repositories.RecipeRepository;
import es.solsaraguille.recipespring.repositories.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
@CrossOrigin
public class RecipeController {

    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;

    public RecipeController(RecipeRepository recipeRepository,
                            UserRepository userRepository) {
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<Recipe> getAll() {
        return recipeRepository.findAll();
    }

    @GetMapping("/{id}")
    public Recipe getById(@PathVariable Integer id) {
        return recipeRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Recipe create(@RequestParam Integer userId,
                         @RequestBody Recipe recipe) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        recipe.setUser(user);

        return recipeRepository.save(recipe);
    }

    @GetMapping("/user/{userId}")
    public List<Recipe> getByUserId(@PathVariable Integer userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return recipeRepository.findByUser(user);
    }

    @GetMapping("/search")
    public List<Recipe> search(@RequestParam String name) {
        return recipeRepository.findByNameContainingIgnoreCase(name);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        recipeRepository.deleteById(id);
    }
}