package com.example.exoesqueletov1.clases;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.example.exoesqueletov1.dialog.DialogAllDone;
import com.example.exoesqueletov1.dialog.DialogLoading;
import com.example.exoesqueletov1.dialog.DialogOops;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class DatabaseClass {

    private FirebaseFirestore db;
    private FragmentManager fragmentManager;

    public DatabaseClass(FragmentManager fragmentManager) {
        this.db = FirebaseFirestore.getInstance();
        this.fragmentManager = fragmentManager;
    }

    public void setData (String collectionPath, Map<String, Object> user) {
        final DialogLoading loading = new DialogLoading();
        loading.show(fragmentManager, "exmaple");
        db.collection(collectionPath).document(new AuthenticationClass().getCurrentUser().getEmail()).set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        loading.dismiss();
                        DialogAllDone allDone = new DialogAllDone("");
                        allDone.show(fragmentManager, "example");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loading.dismiss();
                DialogOops dialogOops = new DialogOops(e.getMessage());
                dialogOops.show(fragmentManager, "example");
            }
        });
    }

}
