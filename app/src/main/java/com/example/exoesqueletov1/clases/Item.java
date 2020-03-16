package com.example.exoesqueletov1.clases;

public class Item {

    private int code;
    private int icon;
    private String name;

    public Item(int code, int icon, String name) {
        this.code = code;
        this.icon = icon;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
