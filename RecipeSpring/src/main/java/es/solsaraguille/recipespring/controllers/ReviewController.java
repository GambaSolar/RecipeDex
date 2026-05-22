package es.solsaraguille.recipespring.controllers;

import es.solsaraguille.recipespring.entities.*;
import es.solsaraguille.recipespring.repositories.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> create(@RequestParam Integer userId,
                                    @RequestParam Integer recipeId,
                                    @RequestBody Review review) {

        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found");
        }

        Recipe recipe = recipeRepository.findById(recipeId).orElse(null);
        if (recipe == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Recipe not found");
        }

        if (review.getRating() < 1 || review.getRating() > 5) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Rating must be between 1 and 5");
        }

        review.setUser(user);
        review.setRecipe(recipe);

        return ResponseEntity.ok(reviewRepository.save(review));
    }

    @GetMapping("/recipe/{recipeId}")
    public ResponseEntity<?> getByRecipe(@PathVariable Integer recipeId) {

        Recipe recipe = recipeRepository.findById(recipeId).orElse(null);

        if (recipe == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Recipe not found");
        }

        return ResponseEntity.ok(
                reviewRepository.findByRecipe(recipe)
        );
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<?> update(@PathVariable Integer reviewId,
                                    @RequestBody Review updatedReview) {

        Review review = reviewRepository.findById(reviewId).orElse(null);

        if (review == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Review not found");
        }

        if (updatedReview.getRating() < 1 || updatedReview.getRating() > 5) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Rating must be between 1 and 5");
        }

        review.setRating(updatedReview.getRating());
        review.setComment(updatedReview.getComment());

        return ResponseEntity.ok(reviewRepository.save(review));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> delete(@PathVariable Integer reviewId) {

        if (!reviewRepository.existsById(reviewId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Review not found");
        }

        reviewRepository.deleteById(reviewId);

        return ResponseEntity.ok("Review deleted successfully");
    }
}