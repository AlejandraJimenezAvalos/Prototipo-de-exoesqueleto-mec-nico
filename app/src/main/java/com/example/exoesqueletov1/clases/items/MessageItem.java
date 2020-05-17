package com.example.exoesqueletov1.clases.items;

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

    public String getHour() {
        return hour;
    }

    public Boolean getMyMessage() {
        return myMessage;
    }

}
