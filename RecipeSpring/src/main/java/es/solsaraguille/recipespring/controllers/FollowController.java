package es.solsaraguille.recipespring.controllers;

import es.solsaraguille.recipespring.entities.*;
import es.solsaraguille.recipespring.repositories.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@CrossOrigin
public class FollowController {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    public FollowController(FollowRepository followRepository, UserRepository userRepository) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
    }

    @PostMapping
    public Follow follow(@RequestParam Integer followerId, @RequestParam Integer followedId) {

        User follower = userRepository.findById(followerId).orElseThrow();
        User followed = userRepository.findById(followedId).orElseThrow();

        Follow follow = new Follow();
        follow.setFollower(follower);
        follow.setFollowed(followed);

        return followRepository.save(follow);
    }

    @GetMapping("/user/{userId}")
    public List<Follow> getFollowing(@PathVariable Integer userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return followRepository.findByFollower(user);
    }

}