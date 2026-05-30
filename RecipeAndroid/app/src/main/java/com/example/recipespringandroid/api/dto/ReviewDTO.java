package com.example.recipespringandroid.api.dto;

import com.example.recipespringandroid.models.User;

public class ReviewDTO {

    private User user;
    private int rating;
    private String comment;

    public ReviewDTO() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}