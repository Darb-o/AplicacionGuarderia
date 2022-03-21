package com.example.aplicacionguarderia;

import java.io.Serializable;

public class ListElement implements Serializable {
    private String color, name, date, status, imgUrl;

    public ListElement(String color, String name, String date, String status, String imgUrl) {
        this.color = color;
        this.name = name;
        this.date = date;
        this.status = status;
        this.imgUrl = imgUrl;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
