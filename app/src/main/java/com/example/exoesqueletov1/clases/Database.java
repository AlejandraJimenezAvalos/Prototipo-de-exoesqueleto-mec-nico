package com.example.exoesqueletov1.clases;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exoesqueletov1.R;
import com.example.exoesqueletov1.dialog.DialogAllDone;
import com.example.exoesqueletov1.dialog.DialogFriendRequest;
import com.example.exoesqueletov1.dialog.DialogLoading;
import com.example.exoesqueletov1.dialog.DialogOops;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Database implements ChatAdapter.OnMenuListener {

    private FirebaseFirestore db;
    private FragmentManager fragmentManager;
    private Context context;
    private List<ChatItem> mData;
    private ChatAdapter chatAdapter;

    private boolean stateOnClick = false;

    private static final String COLLECTION_USERS = "users";
    private static final String COLLECTION_CHATS = "collectionChats";
    private static final String COLLECTION_NOTIFICATIONS = "notifications";
    private static final String DOCUMENT_TYPE = "typeUser";
    private static final String DOCUMENT_PROFILE = "profile";

    private static final String ID = "id";
    private static final String ID_NEW_USER = "idNewUser";
    private static final String USER = "user";
    private static final String NAME = "name";
    private static final String VERIFY = "verify";
    private static final String VERIFY_EMAIL = "verifyEmail";
    private static final String ADDRESS = "address";
    private static final String CELL = "cell";
    private static final String PHONE = "phone";
    private static final String EMAIL = "email";
    private static final String SCHOOL = "school";
    private static final String DESCRIPTION = "description";
    private static final String SPECIALIST = "specialist";
    private static final String STATE = "state";

    private static final String TITLE = "title";
    private static final String DATE = "date";
    private static final String CODE = "code";
    private static final String STATE_NOTIFY = "stateNotify";
    private static final String TO = "to";
    private static final String ADMIN = "a";

    private static final int CODE_FRIEND_REQUEST = 0;
    private static final int CODE_NEW_USER = 1;


    public Database(FragmentManager fragmentManager, Context context) {
        this.context = context;
        db = FirebaseFirestore.getInstance();
        this.fragmentManager = fragmentManager;
    }

    public void getUsers(String typeUser, final RecyclerView recyclerView) {
        stateOnClick = true;
        if (typeUser.equals("a")) {
            FirebaseFirestore.getInstance().
                    collection("users").whereEqualTo("user", "c").get().
                    addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                        @Override
                        public void onComplete(@NonNull final Task<QuerySnapshot> taskC) {
                            FirebaseFirestore.getInstance().
                                    collection("users").whereEqualTo("user", "b").get().
                                    addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> taskB) {
                                            mData = new ArrayList<>();

                                            for (QueryDocumentSnapshot documentC : taskC.getResult()) {
                                                String id = documentC.getData().get(ID).toString();
                                                String name = documentC.getData().get(NAME).toString();
                                                mData.add(new ChatItem(id, name, "", "Pasiente"));
                                            }

                                            for (QueryDocumentSnapshot documentB : taskB.getResult()) {
                                                String id = documentB.getData().get("id").toString();
                                                String name = documentB.getData().get("name").toString();
                                                mData.add(new ChatItem(id, name, "", "Fisioterapeuta"));
                                            }

                                            chatAdapter = new ChatAdapter(context, mData, Database.this);
                                            recyclerView.setAdapter(chatAdapter);
                                            recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

                                        }
                                    });
                        }

                    });
        }
        if (typeUser.equals("b")) {
            FirebaseFirestore.getInstance().
                    collection("users").whereEqualTo(SPECIALIST, false).get().
                    addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull final Task<QuerySnapshot> task) {

                            mData = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String id = document.getData().get("id").toString();
                                String name = document.getData().get("name").toString();
                                mData.add(new ChatItem(id, name, "", "Pasiente"));
                            }

                            chatAdapter = new ChatAdapter(context, mData, Database.this);
                            recyclerView.setAdapter(chatAdapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                        }
                    });
        }
    }

    public void getChats(String id, RecyclerView recyclerView, String typeUser) {
        mData = new ArrayList<>();

        stateOnClick = false;

        //Aqui incertar los chats

        chatAdapter = new ChatAdapter(context, mData, this);
        recyclerView.setAdapter(chatAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
    }

    public void getProfile(final String id, final TextView textViewName, final TextView textViewUser,
                           final TextView textViewDes, final TextView textViewMail, final TextView textViewAddress,
                           final TextView textViewCell, final TextView textViewPhone, final TextView textViewSchool,
                           final CircleImageView circleImageViewProfile, final LinearLayout linearLayoutSchool) {
        db.collection(id).document(DOCUMENT_PROFILE).
                get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String typeUser = "";

                String user = documentSnapshot.getData().get(USER).toString();
                if (user.equals("a")) { typeUser = "Administrador"; }
                if (user.equals("b")) { typeUser = "Fisioterapeuta"; }
                if (user.equals("c")) {
                    typeUser = "Paciente";
                    linearLayoutSchool.setVisibility(View.INVISIBLE);
                }
                textViewName.setText(documentSnapshot.getData().get(NAME).toString());
                textViewUser.setText(typeUser);
                textViewDes.setText(documentSnapshot.getData().get(DESCRIPTION).toString());
                textViewMail.setText(documentSnapshot.getData().get(EMAIL).toString());
                textViewAddress.setText(documentSnapshot.getData().get(ADDRESS).toString());
                textViewCell.setText(documentSnapshot.getData().get(CELL).toString());
                textViewPhone.setText(documentSnapshot.getData().get(PHONE).toString());
                textViewSchool.setText(documentSnapshot.getData().get(SCHOOL).toString());

                new Storge().getProfileImage(circleImageViewProfile, id);
            }
        });
    }

    public void initMain(final TextView name, final TextView typeUser, final TextView state, final String id,
                         final CircleImageView circleImageView) {
        db.collection(id).document(USER).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Map<String, Object> data = documentSnapshot.getData();
                        try {
                            if (Boolean.parseBoolean(String.valueOf(data.get(id)))) {
                                new Storge().getProfileImage(circleImageView, id);
                                name.setText(data.get("name").toString());
                                typeUser.setText(data.get("user").toString());
                                state.setText("t");
                            }
                        } catch (Exception e) {
                            state.setText("t");
                            typeUser.setText("n");
                        }
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

    public void setDataUser(final String collectionPath, final String document,
                            Map<String, Object> dataUser, String userType) {
        final DialogLoading loading;
        Map<String, Object> dataTypeUser;
        Map<String, Object> dataCollectionUsers;
        Map<String, Object> dataCollectionProfile;
        Map<String, Object> dataNotification;

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
        if (userType.equals("c")) { dataCollectionUsers.put(SPECIALIST, false); }

        dataCollectionProfile = new HashMap<>();
        dataCollectionProfile.put(NAME, dataUser.get("name") + " " + dataUser.get("lastName"));
        dataCollectionProfile.put(USER, userType);
        dataCollectionProfile.put(DESCRIPTION, "");
        dataCollectionProfile.put(EMAIL, "");
        dataCollectionProfile.put(ADDRESS, "");
        dataCollectionProfile.put(CELL, "");
        dataCollectionProfile.put(PHONE, "");
        dataCollectionProfile.put(SCHOOL, "");

        dataNotification = new HashMap<>();

        dataNotification.put(TITLE, "Un nuevo usuario se ha registrado");
        dataNotification.put(DESCRIPTION, "Da click para ver mas informacion");
        dataNotification.put(DATE, DateFormat.format("MMMM d, yyyy ", new Date().getTime()));
        dataNotification.put(CODE, CODE_NEW_USER);
        dataNotification.put(TO, ADMIN);
        dataNotification.put(ID_NEW_USER, collectionPath);
        dataNotification.put(STATE_NOTIFY, false);

        db.collection(collectionPath).document(document).set(dataUser);
        db.collection(collectionPath).document(DOCUMENT_TYPE).set(dataTypeUser);
        db.collection(collectionPath).document(DOCUMENT_PROFILE).set(dataCollectionProfile);
        db.collection(COLLECTION_USERS).document(collectionPath).set(dataCollectionUsers);
        db.collection(COLLECTION_NOTIFICATIONS).add(dataNotification);

        if (userType.equals("b")) {
            Map<String, Object> dataCollectionChats;
            dataCollectionChats = new HashMap<>();
            dataCollectionChats.put(STATE, false);
            db.collection(dataUser.get(COLLECTION_CHATS).toString()).add(dataCollectionChats);
        }

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

    @Override
    public void onMenuClick(int position) {
        if (!stateOnClick) {
            Toast.makeText(context, "chat", Toast.LENGTH_SHORT).show();
        } else {
            DialogFriendRequest request;
            request = new DialogFriendRequest(mData.get(position).getName(),
                    mData.get(position).getMessage(), mData.get(position).getId());
            request.show(fragmentManager, "");
        }
    }

    public void search(CharSequence s) {
        chatAdapter.getFilter().filter(s);
    }

    public void setData(String collection, String document, Map<String, Object> data) {

        if (document.isEmpty()) {
            db.collection(collection).document().set(data);
        } else {
            db.collection(collection).document(document).set(data);
        }

    }
}
