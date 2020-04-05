package com.example.exoesqueletov1.clases;

public class MenuItem {


    private String Title;
    private int userPhoto;

    public MenuItem(String title, int userPhoto) {
        Title = title;
        this.userPhoto = userPhoto;
    }

    public String getTitle() {
        return Title;
    }

    int getUserPhoto() {
        return userPhoto;
    }
}
