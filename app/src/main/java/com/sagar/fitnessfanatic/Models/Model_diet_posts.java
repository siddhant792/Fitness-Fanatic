package com.sagar.fitnessfanatic.Models;

public class Model_diet_posts {
    private String text,photo,id;

    public Model_diet_posts(String text, String photo, String id) {
        this.text = text;
        this.photo = photo;
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public String getPhoto() {
        return photo;
    }

    public String getId() {
        return id;
    }
}
