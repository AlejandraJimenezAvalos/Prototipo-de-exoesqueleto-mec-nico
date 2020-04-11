package com.example.exoesqueletov1.clases;

import java.util.UUID;

public class NotificationsItem {

    private String title;
    private String date;
    private String description;
    private String id;

    public NotificationsItem(String title, String date, String description, String id) {
        this.title = title;
        this.date = date;
        this.description = description;
        this.id = id;
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

    String getUuid() {
        return id;
    }
}
