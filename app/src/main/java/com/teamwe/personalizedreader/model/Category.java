package com.teamwe.personalizedreader.model;

/**
 * Created by Petre on 2/3/2016.
 */
public class Category {

    private int id;
    private String name;
    private String title;
    private String imgUrl;
    private boolean checkedState;

    public Category(String name, String title) {
        this.name = name;
        this.title = title;
        this.checkedState = true;
        this.imgUrl = "";
    }

    public Category(String name, String title, String imgUrl) {
        this.name = name;
        this.title = title;
        this.imgUrl = imgUrl;
        this.checkedState = true;
    }

    public Category() {
    }
    public String getImgUrl() {
        return imgUrl;
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

    public boolean getCheckedState() {
        return checkedState;
    }

    public void setCheckedState(boolean checkedState) {
        this.checkedState = checkedState;
    }
}
