package com.teamwe.personalizedreader.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;


public class Category implements Parcelable {

    private int id;
    private String name;
    private String title;
    private String imgUrl;
    private boolean checkedState;

    public static HashMap<String, String> NAME_TO_TITLE = new HashMap<String, String>(){{
        put("MAKEDONIJA", "Македонија");
        put("ZDRAVJE", "Здравје");
        put("ZIVOT", "Живот");
        put("TENIS", "Тенис");
        put("SVET", "Свет");
        put("SCENA", "Сцена");
        put("KOSARKA", "Кошарка");
        put("KULTURA", "Култура");
        put("EKONOMIJA", "Економија");
        put("RAKOMET", "Ракомет");
        put("FUDBAL", "Фудбал");
        put("TEHNOLOGIJA", "Технологија");
    }};

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

    public Category(Parcel input){

        id = input.readInt();
        name = input.readString();
        title = input.readString();
        imgUrl = input.readString();
        checkedState = (input.readInt() == 1);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(title);
        dest.writeString(imgUrl);
        dest.writeInt(checkedState?1:0);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Category> CREATOR = new Parcelable.Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    public String getImgUrl() {
        return imgUrl;
    }

    public String getTitle() {
        return NAME_TO_TITLE.get(name);
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


    public int getId() {
        return id;
    }

}
