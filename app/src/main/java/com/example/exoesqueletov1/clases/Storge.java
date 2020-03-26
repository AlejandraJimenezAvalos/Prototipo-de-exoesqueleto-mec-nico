package com.example.exoesqueletov1.clases;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.example.exoesqueletov1.R;
import com.example.exoesqueletov1.dialog.DialogAllDone;
import com.example.exoesqueletov1.dialog.DialogLoading;
import com.example.exoesqueletov1.dialog.DialogOops;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Storge {

    private StorageReference reference;
    private FragmentManager fragmentManager;
    private Context context;

    public Storge(FragmentManager fragmentManager, String path, String name, Context context) {
        this.context = context;
        this.reference = FirebaseStorage.getInstance().getReference().child(path).child(name);
        this.fragmentManager = fragmentManager;
    }

    public void setDocument (Uri uri) {
        final DialogLoading dialogLoading = new DialogLoading();
        dialogLoading.show(fragmentManager, context.getString(R.string.example));
        reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                DialogAllDone allDone;
                dialogLoading.dismiss();
                allDone = new DialogAllDone(context.getString(R.string.registro_exitoso));
                allDone.show(fragmentManager, context.getString(R.string.example));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                DialogOops dialogOops;
                dialogOops = new DialogOops(e.getMessage());
                dialogOops.show(fragmentManager, context.getString(R.string.example));
            }
        });
    }
}
