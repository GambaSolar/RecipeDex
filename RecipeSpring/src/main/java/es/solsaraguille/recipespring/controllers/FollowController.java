package es.solsaraguille.recipespring.controllers;

import es.solsaraguille.recipespring.entities.*;
import es.solsaraguille.recipespring.repositories.*;
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
    public Follow follow(@RequestParam Integer followerId,
                         @RequestParam Integer followedId) {

        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new RuntimeException("Follower not found"));

        User followed = userRepository.findById(followedId)
                .orElseThrow(() -> new RuntimeException("Followed not found"));

        Follow follow = new Follow();
        follow.setFollower(follower);
        follow.setFollowed(followed);

        return followRepository.save(follow);
    }

    @GetMapping("/user/{userId}")
    public List<Follow> getFollowing(@PathVariable Integer userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return followRepository.findByFollower(user);
    }
}