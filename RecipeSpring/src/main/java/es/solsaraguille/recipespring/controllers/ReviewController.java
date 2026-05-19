package es.solsaraguille.recipespring.controllers;

import es.solsaraguille.recipespring.entities.*;
import es.solsaraguille.recipespring.repositories.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin
public class ReviewController {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;

    public ReviewController(ReviewRepository reviewRepository,
                            UserRepository userRepository,
                            RecipeRepository recipeRepository) {

        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.recipeRepository = recipeRepository;
    }

    @PostMapping
    public Review create(@RequestParam Integer userId,
                         @RequestParam Integer recipeId,
                         @RequestBody Review review) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        review.setUser(user);
        review.setRecipe(recipe);

        return reviewRepository.save(review);
    }

    @GetMapping("/recipe/{recipeId}")
    public List<Review> getByRecipe(@PathVariable Integer recipeId) {

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        return reviewRepository.findByRecipe(recipe);
    }
}