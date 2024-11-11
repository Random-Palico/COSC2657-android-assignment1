package com.example.easycooking.model;

import java.util.List;

public class Recipe {
    private String title;
    private int imageResource;
    private String time;
    private String calories;
    private String rating;
    private String about;
    private List<String> ingredients;
    private String recipeInstructions;

    public Recipe(String title, int imageResource, String time, String calories, String about, List<String> ingredients, String recipeInstructions, String rating) {
        this.title = title;
        this.imageResource = imageResource;
        this.time = time;
        this.calories = calories;
        this.rating = rating;
        this.about = about;
        this.ingredients = ingredients;
        this.recipeInstructions = recipeInstructions;
    }

    public String getTitle() {
        return title;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getTime() {
        return time;
    }

    public String getCalories() {
        return calories;
    }

    public String getRating() {
        return rating;
    }

    public String getAbout() {
        return about;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public String getRecipeInstructions() {
        return recipeInstructions;
    }

}
