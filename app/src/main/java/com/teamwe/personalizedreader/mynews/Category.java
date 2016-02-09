package com.teamwe.personalizedreader.mynews;

/**
 * Created by Petre on 2/3/2016.
 */
public class Category {
    private String name;
    private String title;

    public Category(String name, String title) {
        this.name = name;
        this.title = title;
    }

    public Category() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
