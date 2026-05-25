package com.example.recipespringandroid.models;

public class Ingredient {

    private Integer id;
    private String name;

    public Ingredient() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}