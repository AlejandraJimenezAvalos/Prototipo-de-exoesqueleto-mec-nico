package com.example.exoesqueletov1.clases;

public class ItemNotify {

    private String title;
    private String date;
    private String description;

    public ItemNotify(String title, String date, String description) {
        this.title = title;
        this.date = date;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }
}
