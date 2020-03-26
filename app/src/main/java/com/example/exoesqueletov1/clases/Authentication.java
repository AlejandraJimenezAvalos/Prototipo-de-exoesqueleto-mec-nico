package com.example.exoesqueletov1.clases;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.example.exoesqueletov1.R;
import com.example.exoesqueletov1.dialog.DialogAllDone;
import com.example.exoesqueletov1.dialog.DialogLoading;
import com.example.exoesqueletov1.dialog.DialogOops;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Authentication {

    private FragmentManager fragmentManager;
    private FirebaseAuth auth;

    public Authentication() {
        this.auth = FirebaseAuth.getInstance();
    }

    public Authentication(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
        this.auth = FirebaseAuth.getInstance();
    }

    public void logUp (String email, String password){
        final DialogLoading loading = new DialogLoading();
        loading.show(fragmentManager, "Loading");
        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                loading.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loading.dismiss();
                DialogOops dialogOops = new DialogOops(e.getMessage());
                dialogOops.show(fragmentManager, "Error");
            }
        });
    }

    public void logIn (String email, String password) {
        final DialogLoading loading = new DialogLoading();
        loading.show(fragmentManager, "Loding");
        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                loading.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loading.dismiss();
                DialogOops dialogOops = new DialogOops(e.getMessage());
                dialogOops.show(fragmentManager, "Error");
            }
        });
    }

    public void logInWithCredential (AuthCredential credential) {
        final DialogLoading loading = new DialogLoading();
        loading.show(fragmentManager, "Loding");
        auth.signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                loading.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loading.dismiss();
                DialogOops dialogOops = new DialogOops(e.getMessage());
                dialogOops.show(fragmentManager, "Error");
            }
        });
    }

    public boolean verifyCurrentUser() {
        boolean state = false;
        if (auth.getCurrentUser() != null) state = true;
        return state;
    }

    public FirebaseUser getCurrentUser() {
        return auth.getCurrentUser();
    }

    public void sendPasswordReset (String email) {
        auth.sendPasswordResetEmail(email)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        DialogAllDone dialogAllDone = new DialogAllDone("" + R.string.indicaciones_olvide_mi_contraseña);
                        dialogAllDone.show(fragmentManager, "AllDone");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                DialogOops dialogOops = new DialogOops(e.getMessage());
                dialogOops.show(fragmentManager, "example");
            }
        });
    }

}