package com.example.exoesqueletov1.clases;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exoesqueletov1.MainActivity;
import com.example.exoesqueletov1.NotifyFragment;
import com.example.exoesqueletov1.ProfileLogUpFragment;
import com.example.exoesqueletov1.R;
import com.example.exoesqueletov1.dialog.DialogAllDone;
import com.example.exoesqueletov1.dialog.DialogLoading;
import com.example.exoesqueletov1.dialog.DialogOops;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Database {

    private FirebaseFirestore db;
    private FragmentManager fragmentManager;
    private Context context;

    private static final String ID = "id";
    private static final String USER = "user";
    private static final String NAME = "name";
    private static final String VERIFY = "verify";
    private static final String VERIFY_EMAIL = "verifyEmail";
    private static final String COLLECTION_USERS = "users";
    private static final String DOCUMENT_TYPE = "typeUser";
    private static final String ADDRESS = "address";
    private static final String CELL = "cell";
    private static final String PHONE = "phone";
    private static final String EMAIL = "email";
    private static final String SCHOOL = "school";
    private static final String DESCRIPTION = "description";
    private static final String DOCUMENT_PROFILE = "profile";


    public Database(FragmentManager fragmentManager, Context context) {
        this.context = context;
        db = FirebaseFirestore.getInstance();
        this.fragmentManager = fragmentManager;
    }

    public void init(final TextView name, final TextView typeUser, final TextView state, final String id, final CircleImageView circleImageView) {
        FirebaseFirestore.getInstance().collection(id).document(USER).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Map<String, Object> data = documentSnapshot.getData();
                        try {
                            if (Boolean.parseBoolean(String.valueOf(data.get(id)))) {
                                name.setText(data.get("name").toString());
                                new Storge().getProfileImage(circleImageView, id);

                                typeUser.setText(data.get("user").toString());
                                state.setText("t");
                            }
                        } catch (Exception e) { typeUser.setText("n"); }
                    }
                });
    }

    public void updateData (String collection, String document, Map<String, Object> data) {
        db.collection(collection).document(document).update(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                DialogAllDone dialogAllDone = new DialogAllDone(context.getString(R.string.operacion_exitosa));
                dialogAllDone.show(fragmentManager, "example");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                DialogOops dialogOops = new DialogOops(e.getMessage());
                dialogOops.show(fragmentManager, "example");
            }
        });
    }

    public void setDataUser(final String collectionPath, final String document, Map<String, Object> dataUser, String userType) {
        final DialogLoading loading;
        Map<String, Object> dataTypeUser;
        Map<String, Object> dataCollectionUsers;
        Map<String, Object> dataCollectionProfile;

        loading = new DialogLoading();
        loading.show(fragmentManager, "exmaple");

        dataTypeUser = new HashMap<>();
        dataTypeUser.put(ID, collectionPath);
        dataTypeUser.put(USER, userType);

        dataCollectionUsers = new HashMap<>();
        dataCollectionUsers.put(ID, collectionPath);
        dataCollectionUsers.put(USER, userType);
        dataCollectionUsers.put(NAME, dataUser.get("name") + " " + dataUser.get("lastName"));
        dataCollectionUsers.put(VERIFY, false);
        dataCollectionUsers.put(VERIFY_EMAIL, false);

        dataCollectionProfile = new HashMap<>();
        dataCollectionProfile.put(NAME, dataUser.get("name") + " " + dataUser.get("lastName"));
        dataCollectionProfile.put(USER, userType);
        dataCollectionProfile.put(DESCRIPTION, "");
        dataCollectionProfile.put(EMAIL, "");
        dataCollectionProfile.put(ADDRESS, "");
        dataCollectionProfile.put(CELL, "");
        dataCollectionProfile.put(PHONE, "");
        dataCollectionProfile.put(SCHOOL, "");

        db.collection(collectionPath).document(document).set(dataUser).isComplete();
        db.collection(collectionPath).document(DOCUMENT_TYPE).set(dataTypeUser).isComplete();
        db.collection(collectionPath).document(DOCUMENT_PROFILE).set(dataCollectionProfile).isComplete();
        db.collection(COLLECTION_USERS).document(collectionPath).set(dataCollectionUsers).isComplete();

        db.collection(collectionPath).document(document).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                loading.dismiss();
                if (collectionPath.equals(documentSnapshot.getData().get("id").toString())) {
                    DialogAllDone dialogAllDone = new DialogAllDone(context.getString(R.string.registro_exitoso));
                    dialogAllDone.show(fragmentManager, context.getString(R.string.example));
                } else {
                    db.collection(collectionPath).document(document).delete();
                    db.collection(collectionPath).document(DOCUMENT_TYPE).delete();
                    db.collection(COLLECTION_USERS).document(collectionPath).delete();
                    DialogOops dialogOops = new DialogOops(context.getString(R.string.try_again));
                    dialogOops.show(fragmentManager, context.getString(R.string.example));
                }
            }
        });

    }

}
