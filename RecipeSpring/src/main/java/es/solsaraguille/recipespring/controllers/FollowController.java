package es.solsaraguille.recipespring.controllers;

import es.solsaraguille.recipespring.entities.Follow;
import es.solsaraguille.recipespring.entities.User;
import es.solsaraguille.recipespring.repositories.FollowRepository;
import es.solsaraguille.recipespring.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/follows")
@CrossOrigin
public class FollowController {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    public FollowController(FollowRepository followRepository,
                            UserRepository userRepository) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<?> follow(@RequestParam Integer followerId,
                                    @RequestParam Integer followedId) {

        if (followerId.equals(followedId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("You cannot follow yourself");
        }

        User follower = userRepository.findById(followerId)
                .orElse(null);

        if (follower == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Follower not found");
        }

        User followed = userRepository.findById(followedId)
                .orElse(null);

        if (followed == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Followed user not found");
        }

        boolean exists = followRepository
                .findByFollower(follower)
                .stream()
                .anyMatch(f -> f.getFollowed().getId().equals(followedId));

        if (exists) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Already following this user");
        }

        Follow follow = new Follow();
        follow.setFollower(follower);
        follow.setFollowed(followed);

        return ResponseEntity.ok(followRepository.save(follow));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getFollowing(@PathVariable Integer userId) {

        User user = userRepository.findById(userId)
                .orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found");
        }

        List<Follow> follows = followRepository.findByFollower(user);

        return ResponseEntity.ok(follows);
    }
}