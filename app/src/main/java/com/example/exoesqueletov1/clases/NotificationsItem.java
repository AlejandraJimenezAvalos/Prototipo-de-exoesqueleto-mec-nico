package com.example.exoesqueletov1.clases;

public class NotificationsItem {

    private String title;
    private String date;
    private String description;
    private String id;
    private int code;
    private boolean state;

    public NotificationsItem(String title, String date, String description, String id, int code,
                             boolean state) {
        this.title = title;
        this.date = date;
        this.description = description;
        this.id = id;
        this.code = code;
        this.state = state;
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

    public boolean isState() {
        return state;
    }

    public String getId() {
        return id;
    }

    public int getCode () {
        return code;
    }
}
