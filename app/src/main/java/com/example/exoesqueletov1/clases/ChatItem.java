package com.example.exoesqueletov1.clases;

public class ChatItem {

    private String id;
    private String name;
    private String date;
    private String message;
    private String idChat;

    ChatItem(String id, String name, String date, String message, String idChat) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.message = message;
        this.idChat = idChat;
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

    String getIdChat() {
        return idChat;
    }

    public void setName(String name) {
        this.name = name;
    }

    void setMessage(String message) {
        this.message = message;
    }
}
