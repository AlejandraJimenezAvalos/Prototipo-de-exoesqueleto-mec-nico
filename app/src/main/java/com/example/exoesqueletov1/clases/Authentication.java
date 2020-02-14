package com.example.exoesqueletov1.clases;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Authentication {
    private FirebaseUser user;
    private FirebaseAuth auth;

    public Authentication() {
        auth = FirebaseAuth.getInstance();
        this.user = auth.getCurrentUser();
    }

    public String registrar (String email, String password){
        final String[] state = new String[1];
        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                state[0] = "";
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                state[0] = e.getMessage();
            }
        });
        return state[0];
    }

    public boolean verificar () {
        if (user == null) return false;
        else return true;
    }
}