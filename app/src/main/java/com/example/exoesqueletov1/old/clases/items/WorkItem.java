package com.example.exoesqueletov1.old.clases.items;

public class WorkItem {

    private String action;
    private String date;
    private int number;
    private boolean state;
    private String id;

    public WorkItem(String action, String date, int number, boolean state, String id) {
        this.action = action;
        this.date = date;
        this.number = number;
        this.state = state;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getAction() {
        return action;
    }

    public String getDate() {
        return date;
    }

    public int getNumber() {
        return number;
    }

    public boolean isState() {
        return state;
    }
}
