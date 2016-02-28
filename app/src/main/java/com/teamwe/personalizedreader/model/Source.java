package com.teamwe.personalizedreader.model;


public class Source {

    private int id;
    private String url;
    private boolean isChecked;

    public Source(int id,String url, boolean isChecked) {
        this.id = id;
        this.url = url;
        this.isChecked = isChecked;
    }

    public Source(int id, String url) {
        this.id = id;
        this.url = url;
        this.isChecked = true;
    }

    public Source() {
        this.isChecked = true;
    }

    public int getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }
}
