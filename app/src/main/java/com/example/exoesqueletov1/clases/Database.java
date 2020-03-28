package com.example.exoesqueletov1.clases;

import android.content.Context;

import androidx.fragment.app.FragmentManager;

import com.example.exoesqueletov1.R;
import com.example.exoesqueletov1.dialog.DialogAllDone;
import com.example.exoesqueletov1.dialog.DialogLoading;
import com.example.exoesqueletov1.dialog.DialogOops;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

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


    public Database(FragmentManager fragmentManager, Context context) {
        this.context = context;
        this.db = FirebaseFirestore.getInstance();
        this.fragmentManager = fragmentManager;
    }



    public void setDataUser(final String collectionPath, final String document, Map<String, Object> dataUser, String userType) {
        final DialogLoading loading;
        Map<String, Object> dataTypeUser;
        Map<String, Object> dataCollectionUsers;

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

        db.collection(collectionPath).document(document).set(dataUser).isComplete();
        db.collection(collectionPath).document(DOCUMENT_TYPE).set(dataTypeUser).isComplete();
        db.collection(COLLECTION_USERS).document(collectionPath).set(dataCollectionUsers).isComplete();

        loading.dismiss();

        db.collection(collectionPath).document(document).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
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
