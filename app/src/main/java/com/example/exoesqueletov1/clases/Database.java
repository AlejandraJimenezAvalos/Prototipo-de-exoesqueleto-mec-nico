package com.example.exoesqueletov1.clases;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.example.exoesqueletov1.R;
import com.example.exoesqueletov1.dialog.DialogAllDone;
import com.example.exoesqueletov1.dialog.DialogLoading;
import com.example.exoesqueletov1.dialog.DialogOops;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class Database {

    private FirebaseFirestore db;
    private FragmentManager fragmentManager;
    private Context context;

    public Database() {
        this.db = FirebaseFirestore.getInstance();
    }

    public Database(FragmentManager fragmentManager, Context context) {
        this.context = context;
        this.db = FirebaseFirestore.getInstance();
        this.fragmentManager = fragmentManager;
    }

    public void setData (String collectionPath, String document, Map<String, Object> data) {
        final DialogLoading loading = new DialogLoading();
        loading.show(fragmentManager, "exmaple");
        db.collection(collectionPath).document(document).set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        loading.dismiss();
                        DialogAllDone allDone = new DialogAllDone(context.getString(R.string.registro_exitoso));
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
