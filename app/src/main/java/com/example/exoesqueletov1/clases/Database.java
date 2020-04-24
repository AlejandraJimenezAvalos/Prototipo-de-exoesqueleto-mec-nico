package com.example.exoesqueletov1.clases;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exoesqueletov1.ChatActivity;
import com.example.exoesqueletov1.R;
import com.example.exoesqueletov1.dialogs.DialogAllDone;
import com.example.exoesqueletov1.dialogs.DialogFriendRequest;
import com.example.exoesqueletov1.dialogs.DialogLoading;
import com.example.exoesqueletov1.dialogs.DialogOops;
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
    private FragmentActivity fragmentActivity;

    private String typeUser;

    private boolean stateOnClick = false;

    public static final String COLLECTION_USERS = "users";
    public static final String COLLECTION_CHATS = "chats";
    public static final String COLLECTION_NOTIFICATIONS = "notifications";

    public static final String DOCUMENT_USER = "user";
    private static final String DOCUMENT_TYPE = "typeUser";
    public static final String DOCUMENT_PROFILE = "profile";

    public static final String ID = "id";
    public static final String ID_PATIENT = "idPatient";
    public static final String ID_CHAT = "idChat";
    public static final String ID_SPECIALIST = "idSpecialist";
    public static final String HOUR = "hour";
    public static final String ID_USER_INFRACTION = "idUserInfraction";
    private static final String ID_NEW_USER = "idNewUser";
    public static final String LAST_NAME = "lastName";
    public static final String COUNTRY = "country";
    public static final String GENDER = "gender";
    public static final String USER = "user";
    public static final String NO = "no";
    public static final String RASON = "rason";
    public static final String NAME = "name";
    public static final String MESSAGE = "message";
    private static final String VERIFY = "verify";
    private static final String VERIFY_EMAIL = "verifyEmail";
    public static final String ADDRESS = "address";
    public static final String FROM = "from";
    public static final String CELL = "cell";
    public static final String PHONE = "phone";
    public static final String EMAIL = "email";
    public static final String SCHOOL = "school";
    public static final String DESCRIPTION = "description";
    public static final String SPECIALIST = "specialist";
    public static final String STATE = "state";
    public static final String TITLE = "title";
    public static final String DATE = "date";
    public static final String CODE = "code";
    public static final String STATE_NOTIFY = "stateNotify";
    public static final String TO = "to";
    public static final String ADMIN = "a";

    public static final String REASON_CODE = "reasonCode";

    public static final int CODE_NOTIFICATIONS_FRIEND_REQUEST = 0;
    public static final int CODE_NOTIFICATIONS_ADMIN_REQUEST = 1;
    public static final int CODE_NOTIFICATIONS_NEW_USER = 2;
    public static final int CODE_NOTIFICATIONS_TO_ACCEPT = 3;
    public static final int CODE_NOTIFICATIONS_DELET_REQUEST = 4;
    public static final int CODE_NOTIFICATIONS_DELET_REQUEST_FOR_INFRACTION = 5;
    public static final int CODE_REGULAR = 1000;


    public Database(FragmentManager fragmentManager, Context context) {
        this.context = context;
        db = FirebaseFirestore.getInstance();
        this.fragmentManager = fragmentManager;
    }

    public Database(FragmentManager fragmentManager, Context context, String typeUser,
                    FragmentActivity fragmentActivity) {
        this.context = context;
        db = FirebaseFirestore.getInstance();
        this.fragmentManager = fragmentManager;
        this.typeUser = typeUser;
        this.fragmentActivity = fragmentActivity;
    }

    public void getUsers(final RecyclerView recyclerView) {
        stateOnClick = true;
        if (typeUser.equals("a")) {
            db.collection("users").whereEqualTo("user", "c").get().
                    addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull final Task<QuerySnapshot> taskC) {
                            db.collection("users").whereEqualTo("user", "b")
                                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> taskB) {
                                            mData = new ArrayList<>();

                                            for (QueryDocumentSnapshot documentC : taskC.getResult()) {
                                                String id = documentC.getData().get(ID).toString();
                                                String name = documentC.getData().get(NAME).toString();
                                                mData.add(new ChatItem(id, name, "", "Pasiente", ""));
                                            }

                                            for (QueryDocumentSnapshot documentB : taskB.getResult()) {
                                                String id = documentB.getData().get("id").toString();
                                                String name = documentB.getData().get("name").toString();
                                                mData.add(new ChatItem(id, name, "", "Fisioterapeuta", ""));
                                            }

                                            chatAdapter = new ChatAdapter(context, mData, Database.this);
                                            recyclerView.setAdapter(chatAdapter);
                                            recyclerView.setLayoutManager(new LinearLayoutManager(context,
                                                    LinearLayoutManager.VERTICAL, false));

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
                                mData.add(new ChatItem(id, name, "", "Pasiente", ""));
                            }

                            chatAdapter = new ChatAdapter(context, mData, Database.this);
                            recyclerView.setAdapter(chatAdapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(context,
                                    LinearLayoutManager.VERTICAL, false));
                        }
                    });
        }
        if (typeUser.equals("c")) {
            String id = new Authentication().getCurrentUser().getEmail();
            db.collection(COLLECTION_CHATS).whereEqualTo(ID_PATIENT, id).get().
                    addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            mData = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (!Boolean.parseBoolean(document.getData().get(STATE).toString())) {
                                    mData.add(new ChatItem(document.getData().get(ID_SPECIALIST).toString(),
                                            "", "", context.getString(R.string.do_you_know_this_person), ""));
                                }
                            }
                            chatAdapter = new ChatAdapter(context, mData, Database.this);
                            recyclerView.setAdapter(chatAdapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                        }
                    });
        }
    }

    public void getChats(final String id, final RecyclerView recyclerView) {
        stateOnClick = false;

        String field = "";
        String otherField = "";
        mData = new ArrayList<>();

        if (typeUser.equals("a") || typeUser.equals("b")) {
            field = ID_SPECIALIST;
            otherField = ID_PATIENT;
        }
        if (typeUser.equals("c")) {
            field = ID_PATIENT;
            otherField = ID_SPECIALIST;
        }
        final String finalOtherField = otherField;
        db.collection(COLLECTION_CHATS).whereEqualTo(field, id).get().
                addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                mData = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    if (Boolean.parseBoolean(document.getData().get(STATE).toString())) {
                        mData.add(new ChatItem(document.getData().get(finalOtherField).toString(),
                                "", "", "", document.getData().get(ID_CHAT).
                                toString()));
                    }
                }
                if (typeUser.equals("b")) {
                    db.collection(COLLECTION_CHATS).whereEqualTo(ID_PATIENT, id).get().
                            addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        if (Boolean.parseBoolean(document.getData().get(STATE)
                                                .toString())) {
                                            mData.add(new ChatItem(document.getData()
                                                    .get(ID_SPECIALIST).toString(),
                                                    "", "", "", document.
                                                    getData().get(ID_CHAT).toString()));
                                        }
                                    }
                                    chatAdapter = new ChatAdapter(context, mData,
                                            Database.this);
                                    recyclerView.setAdapter(chatAdapter);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(context,
                                            LinearLayoutManager.VERTICAL, false));
                                }
                            });
                }
                if (typeUser.equals("a")) {
                    chatAdapter = new ChatAdapter(context, mData,
                            Database.this);
                    recyclerView.setAdapter(chatAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context,
                            LinearLayoutManager.VERTICAL, false));
                }
            }
        });
    }

    public void getProfile(final String id, final TextView textViewName, final TextView textViewUser,
                           final TextView textViewDes, final TextView textViewMail,
                           final TextView textViewAddress, final TextView textViewCell,
                           final TextView textViewPhone, final TextView textViewSchool,
                           final CircleImageView circleImageViewProfile,
                           final LinearLayout linearLayoutSchool) {
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

    public void getAndShowProfile(final String id, final TextView textViewName, final TextView textViewUser,
                                  final TextView textViewCheck, final TextView textViewMail,
                                  final TextView textViewAddress, final TextView textViewCell,
                                  final TextView textViewPhone, final TextView textViewSchool,
                                  final CircleImageView circleImageViewProfile) {
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
                    textViewSchool.setVisibility(View.INVISIBLE);
                }
                textViewName.setText(documentSnapshot.getData().get(NAME).toString());
                textViewUser.setText(typeUser);
                textViewMail.setText(documentSnapshot.getData().get(EMAIL).toString());
                textViewAddress.setText(documentSnapshot.getData().get(ADDRESS).toString());
                textViewCell.setText(documentSnapshot.getData().get(CELL).toString());
                textViewPhone.setText(documentSnapshot.getData().get(PHONE).toString());
                textViewSchool.setText(documentSnapshot.getData().get(SCHOOL).toString());

                new Storge().getProfileImage(circleImageViewProfile, id);
            }
        });

        db.collection(COLLECTION_USERS).document(id).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Map<String, Object> data = task.getResult().getData();
                if (Boolean.parseBoolean(data.get(VERIFY).toString())) {
                    textViewCheck.setText(R.string.cuenta_verificada);
                } else {
                    textViewCheck.setText(R.string.cuenta_sin_verificar);
                }
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
        db.collection(collection).document(document).update(data).
                addOnSuccessListener(new OnSuccessListener<Void>() {
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
        dataNotification.put(DESCRIPTION, context.getString(R.string.click_more_info));
        dataNotification.put(DATE, DateFormat.format("MMMM d, yyyy ", new Date().getTime()));
        dataNotification.put(CODE, CODE_NOTIFICATIONS_NEW_USER);
        dataNotification.put(TO, ADMIN);
        dataNotification.put(ID_NEW_USER, collectionPath);
        dataNotification.put(STATE_NOTIFY, false);

        db.collection(collectionPath).document(document).set(dataUser);
        db.collection(collectionPath).document(DOCUMENT_TYPE).set(dataTypeUser);
        db.collection(collectionPath).document(DOCUMENT_PROFILE).set(dataCollectionProfile);
        db.collection(COLLECTION_USERS).document(collectionPath).set(dataCollectionUsers);
        db.collection(COLLECTION_NOTIFICATIONS).add(dataNotification);

        db.collection(collectionPath).document(document).get().
                addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
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
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra(ID_CHAT, mData.get(position).getIdChat());
            intent.putExtra(ID_SPECIALIST, mData.get(position).getId());
            intent.putExtra(USER, typeUser);
            context.startActivity(intent);
            fragmentActivity.finish();
        } else {
            DialogFriendRequest request;
            request = new DialogFriendRequest(typeUser, mData.get(position).getName(),
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
