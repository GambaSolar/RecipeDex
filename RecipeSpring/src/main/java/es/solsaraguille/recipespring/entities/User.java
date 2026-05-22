package es.solsaraguille.recipespring.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Integer id;

    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Column(name = "email", nullable = false, length = 50)
    private String email;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Recipe> recipes;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Favorite> favorites;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Review> reviews;

    @OneToMany(mappedBy = "follower")
    @JsonIgnore
    private List<Follow> following;

    @OneToMany(mappedBy = "followed")
    @JsonIgnore
    private List<Follow> followers;
}