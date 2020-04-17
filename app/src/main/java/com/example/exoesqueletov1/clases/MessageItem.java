package com.example.exoesqueletov1.clases;

public class MessageItem {

    private String message;
    private String hour;
    private Boolean myMessage;

    public MessageItem(String message, String hour, boolean myMessage) {
        this.message = message;
        this.hour = hour;
        this.myMessage = myMessage;
    }

    public String getMessage() {
        return message;
    }

    String getHour() {
        return hour;
    }

    Boolean getMyMessage() {
        return myMessage;
    }

}
