package com.example.exoesqueletov1.clases;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;

import androidx.fragment.app.FragmentManager;

import com.example.exoesqueletov1.R;
import com.example.exoesqueletov1.dialogs.DialogAllDone;
import com.example.exoesqueletov1.dialogs.DialogLoading;
import com.example.exoesqueletov1.dialogs.DialogOops;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class Storge {

    private StorageReference reference;
    private FragmentManager fragmentManager;
    private Context context;
    private static final long ONE_MEGABYTE = 1024 * 1024;
    private static final String LOCATION = "pictureProfile";

    public Storge(FragmentManager fragmentManager, String path, String name, Context context) {
        this.context = context;
        this.reference = FirebaseStorage.getInstance().getReference().child(path).child(name);
        this.fragmentManager = fragmentManager;
    }

    public Storge () {
    }

    public void getProfileImage (final CircleImageView circleImageView, String name) {
        FirebaseStorage.getInstance().getReference().child(LOCATION).child(name)
                .getBytes(ONE_MEGABYTE).addOnSuccessListener(bytes -> {
                    circleImageView.setImageBitmap(BitmapFactory.
                            decodeByteArray(bytes, 0, bytes.length));
        });
    }

    public void setProfile(Uri uri) {
        final DialogLoading dialogLoading = new DialogLoading();
        dialogLoading.show(fragmentManager, context.getString(R.string.example));
        reference.putFile(uri).addOnSuccessListener(taskSnapshot -> {
            DialogAllDone allDone;
            dialogLoading.dismiss();
            allDone = new DialogAllDone(context.getString(R.string.registro_exitoso));
            allDone.show(fragmentManager, context.getString(R.string.example));
        }).addOnFailureListener(e -> {
            DialogOops dialogOops;
            dialogOops = new DialogOops(e.getMessage());
            dialogOops.show(fragmentManager, context.getString(R.string.example));
        });
    }
}
