package com.example.exoesqueletov1.clases;

public class ChatItem {

    private String id;
    private String name;
    private String date;
    private String message;

    public ChatItem(String id, String name, String date, String message) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    String getDate() {
        return date;
    }

    String getMessage() {
        return message;
    }
}
