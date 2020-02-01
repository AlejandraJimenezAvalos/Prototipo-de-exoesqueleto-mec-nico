package com.example.exoesqueletov1.clases;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Authentication {
    private FirebaseUser user;

    public Authentication() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        this.user = auth.getCurrentUser();
    }

    public boolean verificar () {
        if (user == null) return false;
        else return true;
    }
}