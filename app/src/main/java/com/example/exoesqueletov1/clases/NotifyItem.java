package com.example.exoesqueletov1.clases;

public class NotifyItem {

    private String title;
    private String date;
    private String description;

    public NotifyItem(String title, String date, String description) {
        this.title = title;
        this.date = date;
        this.description = description;
    }

    String getTitle() {
        return title;
    }

    String getDate() {
        return date;
    }

    String getDescription() {
        return description;
    }
}
