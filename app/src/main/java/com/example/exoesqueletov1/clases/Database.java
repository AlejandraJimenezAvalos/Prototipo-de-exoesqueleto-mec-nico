package com.example.exoesqueletov1.clases;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exoesqueletov1.ui.ChatActivity;
import com.example.exoesqueletov1.ui.ConstantsDatabase;
import com.example.exoesqueletov1.R;
import com.example.exoesqueletov1.clases.adapters.ChatAdapter;
import com.example.exoesqueletov1.clases.items.ChatItem;
import com.example.exoesqueletov1.ui.dialogs.DialogAllDone;
import com.example.exoesqueletov1.ui.dialogs.DialogFriendRequest;
import com.example.exoesqueletov1.ui.dialogs.DialogLoading;
import com.example.exoesqueletov1.ui.dialogs.DialogOops;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.exoesqueletov1.ui.ConstantsDatabase.ADDRESS;
import static com.example.exoesqueletov1.ui.ConstantsDatabase.ADMIN;
import static com.example.exoesqueletov1.ui.ConstantsDatabase.CELL;
import static com.example.exoesqueletov1.ui.ConstantsDatabase.CODE;
import static com.example.exoesqueletov1.ui.ConstantsDatabase.CODE_NOTIFICATIONS_NEW_USER;
import static com.example.exoesqueletov1.ui.ConstantsDatabase.COLLECTION_CHATS;
import static com.example.exoesqueletov1.ui.ConstantsDatabase.COLLECTION_NOTIFICATIONS;
import static com.example.exoesqueletov1.ui.ConstantsDatabase.COLLECTION_USERS;
import static com.example.exoesqueletov1.ui.ConstantsDatabase.DATE;
import static com.example.exoesqueletov1.ui.ConstantsDatabase.DESCRIPTION;
import static com.example.exoesqueletov1.ui.ConstantsDatabase.DOCUMENT_PROFILE;
import static com.example.exoesqueletov1.ui.ConstantsDatabase.DOCUMENT_TYPE;
import static com.example.exoesqueletov1.ui.ConstantsDatabase.EMAIL;
import static com.example.exoesqueletov1.ui.ConstantsDatabase.ID;
import static com.example.exoesqueletov1.ui.ConstantsDatabase.ID_CHAT;
import static com.example.exoesqueletov1.ui.ConstantsDatabase.ID_EXOESQUELETO;
import static com.example.exoesqueletov1.ui.ConstantsDatabase.ID_NEW_USER;
import static com.example.exoesqueletov1.ui.ConstantsDatabase.ID_PATIENT;
import static com.example.exoesqueletov1.ui.ConstantsDatabase.ID_SPECIALIST;
import static com.example.exoesqueletov1.ui.ConstantsDatabase.NAME;
import static com.example.exoesqueletov1.ui.ConstantsDatabase.PATIENT;
import static com.example.exoesqueletov1.ui.ConstantsDatabase.PHONE;
import static com.example.exoesqueletov1.ui.ConstantsDatabase.SCHOOL;
import static com.example.exoesqueletov1.ui.ConstantsDatabase.SPECIALIST;
import static com.example.exoesqueletov1.ui.ConstantsDatabase.SPECIALIST1;
import static com.example.exoesqueletov1.ui.ConstantsDatabase.STATE;
import static com.example.exoesqueletov1.ui.ConstantsDatabase.STATE_NOTIFY;
import static com.example.exoesqueletov1.ui.ConstantsDatabase.TITLE;
import static com.example.exoesqueletov1.ui.ConstantsDatabase.TO;
import static com.example.exoesqueletov1.ui.ConstantsDatabase.USER;
import static com.example.exoesqueletov1.ui.ConstantsDatabase.VERIFY;
import static com.example.exoesqueletov1.ui.ConstantsDatabase.VERIFY_EMAIL;

public class Database implements ChatAdapter.OnMenuListener {

    private FirebaseFirestore db;
    private FragmentManager fragmentManager;
    private Context context;
    private List<ChatItem> mData;
    private ChatAdapter chatAdapter;

    private String typeUser;

    private boolean stateOnClick = false;

    public Database(FragmentManager fragmentManager, Context context) {
        this.context = context;
        db = FirebaseFirestore.getInstance();
        this.fragmentManager = fragmentManager;
    }

    public Database(FragmentManager fragmentManager, Context context, String typeUser) {
        this.context = context;
        db = FirebaseFirestore.getInstance();
        this.fragmentManager = fragmentManager;
        this.typeUser = typeUser;
    }

    public void getUsers(final RecyclerView recyclerView) {
        CollectionReference referenceUsers = db.collection(COLLECTION_USERS);
        stateOnClick = true;
        mData = new ArrayList<>();
        if (typeUser.equals(ADMIN)) {
            referenceUsers.get().addOnCompleteListener(task -> {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Map<String, Object> data = document.getData();
                    if (!data.get(USER).toString().equals(ADMIN)) {
                        String id = data.get("id").toString();
                        String name = data.get("name").toString();
                        String message;
                        if (data.get(USER).toString().equals(PATIENT)) { message = "Pasiente"; }
                        else { message = "Especialista"; }
                        mData.add(new ChatItem(id, name, "", message, ""));
                    }
                }
                chatAdapter = new ChatAdapter(context, mData, Database.this);
                recyclerView.setAdapter(chatAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(context,
                        LinearLayoutManager.VERTICAL, false));
            });
        }
        if (typeUser.equals(SPECIALIST1)) {
            Query query = referenceUsers.whereEqualTo(SPECIALIST, false);
            query.get().addOnCompleteListener(task -> {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Map<String, Object> data = document.getData();
                    String id = data.get("id").toString();
                    String name = data.get("name").toString();
                    String message = "Pasiente";
                    mData.add(new ChatItem(id, name, "", message, ""));
                }
                chatAdapter = new ChatAdapter(context, mData, Database.this);
                recyclerView.setAdapter(chatAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(context,
                        LinearLayoutManager.VERTICAL, false));
            });
        }
        if (typeUser.equals(PATIENT)) {
            String id = new Authentication().getCurrentUser().getEmail();
            Query query = db.collection(COLLECTION_CHATS).whereEqualTo(ID_PATIENT, id);
            query.get().addOnCompleteListener(task -> {
                mData = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    if (!Boolean.parseBoolean(document.getData()
                            .get(STATE).toString())) {
                        mData.add(new ChatItem(document.getData()
                                .get(ID_SPECIALIST).toString(),
                                "", "", context
                                .getString(R.string.do_you_know_this_person), ""));
                    }
                }
                chatAdapter = new ChatAdapter(context, mData, Database.this);
                recyclerView.setAdapter(chatAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(context,
                        LinearLayoutManager.VERTICAL, false));
            });
        }
    }

    public void getChats(final String id, final RecyclerView recyclerView) {
        CollectionReference chats = db.collection(COLLECTION_CHATS);
        Query toSpecialist = chats.whereEqualTo(ID_SPECIALIST, id);
        Query toPatient = chats.whereEqualTo(ID_PATIENT, id);
        stateOnClick = false;
        mData = new ArrayList<>();
        if (typeUser.equals(ADMIN) || typeUser.equals(SPECIALIST1)) {
            toSpecialist.get().addOnCompleteListener(task -> {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Map<String, Object> data = document.getData();
                    mData.add(new ChatItem(data.get(ID_PATIENT).toString(),
                            "", "", "", data.get(ID_CHAT).toString()));
                }
                chatAdapter = new ChatAdapter(context, mData, Database.this);
                recyclerView.setAdapter(chatAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(context,
                        LinearLayoutManager.VERTICAL, false));
            });
        }
        if (typeUser.equals(PATIENT) || typeUser.equals(SPECIALIST1)) {
            toPatient.get().addOnCompleteListener(task -> {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Map<String, Object> data = document.getData();
                    mData.add(new ChatItem(data.get(ID_SPECIALIST).toString(),
                            "", "", "", data.get(ID_CHAT).toString()));
                }
                chatAdapter = new ChatAdapter(context, mData, Database.this);
                recyclerView.setAdapter(chatAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(context,
                        LinearLayoutManager.VERTICAL, false));
            });
        }
    }

    public void getProfile(final String id, final TextView textViewName,
                           final TextView textViewUser, final TextView textViewDes,
                           final TextView textViewMail, final TextView textViewAddress,
                           final TextView textViewCell, final TextView textViewPhone,
                           final TextView textViewSchool,
                           final CircleImageView circleImageViewProfile,
                           final MaterialCardView linearLayoutSchool) {
        db.collection(id).document(DOCUMENT_PROFILE)
                .get().addOnSuccessListener(documentSnapshot -> {

                    String typeUser = "";

                    String user = documentSnapshot.getData().get(USER).toString();
                    if (user.equals("a")) { typeUser = "Administrador"; }
                    if (user.equals("b")) { typeUser = "Fisioterapeuta"; }
                    if (user.equals("c")) {
                        typeUser = "Paciente";
                        linearLayoutSchool.setVisibility(View.GONE);
                    }
                    textViewName.setText(documentSnapshot.getData()
                            .get(NAME).toString());
                    textViewUser.setText(typeUser);
                    textViewDes.setText(documentSnapshot.getData()
                            .get(DESCRIPTION).toString());
                    textViewMail.setText(documentSnapshot.getData()
                            .get(EMAIL).toString());
                    textViewAddress.setText(documentSnapshot.getData()
                            .get(ADDRESS).toString());
                    textViewCell.setText(documentSnapshot.getData()
                            .get(CELL).toString());
                    textViewPhone.setText(documentSnapshot.getData()
                            .get(PHONE).toString());
                    textViewSchool.setText(documentSnapshot.getData()
                            .get(SCHOOL).toString());

                    new Storge().getProfileImage(circleImageViewProfile, id);
                });
    }

    public void getAndShowProfile(final String id, final TextView textViewName,
                                  final TextView textViewUser, final TextView textViewCheck,
                                  final TextView textViewMail, final TextView textViewAddress,
                                  final TextView textViewCell, final TextView textViewPhone,
                                  final TextView textViewSchool,
                                  final ImageView circleImageViewProfile) {
        db.collection(id).document(DOCUMENT_PROFILE)
                .get().addOnSuccessListener(documentSnapshot -> {

                    String typeUser = "";

                    String user = documentSnapshot.getData().get(USER).toString();
                    if (user.equals("a")) { typeUser = "Administrador"; }
                    if (user.equals("b")) { typeUser = "Fisioterapeuta"; }
                    if (user.equals("c")) {
                        typeUser = "Paciente";
                        textViewSchool.setVisibility(View.INVISIBLE);
                    }
                    textViewName.setText(documentSnapshot.getData()
                            .get(NAME).toString());
                    textViewUser.setText(typeUser);
                    textViewMail.setText(documentSnapshot.getData()
                            .get(EMAIL).toString());
                    textViewAddress.setText(documentSnapshot.getData()
                            .get(ADDRESS).toString());
                    textViewCell.setText(documentSnapshot.getData()
                            .get(CELL).toString());
                    textViewPhone.setText(documentSnapshot.getData()
                            .get(PHONE).toString());
                    textViewSchool.setText(documentSnapshot.getData()
                            .get(SCHOOL).toString());

                    new Storge().getProfileImage(circleImageViewProfile, id);
                });

        db.collection(COLLECTION_USERS).document(id).get()
                .addOnCompleteListener(task -> {
                    Map<String, Object> data = task.getResult().getData();
                    if (Boolean.parseBoolean(data.get(VERIFY).toString())) {
                        textViewCheck.setText(R.string.cuenta_verificada);
                    } else {
                        textViewCheck.setText(R.string.cuenta_sin_verificar);
                    }
                });
    }

    public void initMain(final TextView name, final TextView typeUser, final TextView state,
                         final String id, final ImageView circleImageView) {
        db.collection(id).document(USER).get()
                .addOnSuccessListener(documentSnapshot -> {
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
                });
    }

    public void updateData (String collection, String document, Map<String, Object> data) {
        db.collection(collection).document(document).update(data).
                addOnSuccessListener(this::onSuccess).addOnFailureListener(this::onFailure);
    }

    public void setDataUser(final String collectionPath, final String document,
                            Map<String, Object> dataUser, String userType) {
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
        dataCollectionUsers.put(NAME, dataUser.get("name")
                + " " + dataUser.get("lastName"));
        dataCollectionUsers.put(VERIFY, false);
        dataCollectionUsers.put(VERIFY_EMAIL, false);
        if (userType.equals("c")) {
            String idExoesqueleto = UUID.randomUUID().toString();
            dataCollectionUsers.put(SPECIALIST, false);
            dataCollectionUsers.put(ID_EXOESQUELETO, idExoesqueleto);
        }

        dataCollectionProfile = new HashMap<>();
        dataCollectionProfile.put(NAME, dataUser.get("name")
                + " " + dataUser.get("lastName"));
        dataCollectionProfile.put(USER, userType);
        dataCollectionProfile.put(DESCRIPTION, "");
        dataCollectionProfile.put(EMAIL, "");
        dataCollectionProfile.put(ADDRESS, "");
        dataCollectionProfile.put(CELL, "");
        dataCollectionProfile.put(PHONE, "");
        dataCollectionProfile.put(SCHOOL, "");

        db.collection(COLLECTION_NOTIFICATIONS).get().addOnCompleteListener(task -> {
            Map<String, Object> dataNotification;
            dataNotification = new HashMap<>();

            dataNotification.put(TITLE, "Un nuevo usuario se ha registrado");
            dataNotification.put(DESCRIPTION, context.getString(R.string.click_more_info));
            dataNotification.put(DATE,
                    DateFormat.format("MMMM d, yyyy ", new Date().getTime()));
            dataNotification.put(CODE, CODE_NOTIFICATIONS_NEW_USER);
            dataNotification.put(ConstantsDatabase.NO, task.getResult().size() + 1);
            dataNotification.put(TO, ADMIN);
            dataNotification.put(ID_NEW_USER, collectionPath);
            dataNotification.put(STATE_NOTIFY, false);

            db.collection(COLLECTION_NOTIFICATIONS).add(dataNotification);
        });

        db.collection(collectionPath).document(document).set(dataUser);
        db.collection(collectionPath).document(DOCUMENT_TYPE).set(dataTypeUser);
        db.collection(collectionPath)
                .document(DOCUMENT_PROFILE).set(dataCollectionProfile);
        db.collection(COLLECTION_USERS)
                .document(collectionPath).set(dataCollectionUsers);

        loading.dismiss();
        DialogAllDone dialogAllDone;
        dialogAllDone = new DialogAllDone(context.
                getString(R.string.registro_exitoso));
        dialogAllDone.show(fragmentManager, context.getString(R.string.example));
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

    private void onSuccess(Void aVoid) {
        DialogAllDone dialogAllDone = new DialogAllDone(context.getString(R.string.operacion_exitosa));
        dialogAllDone.show(fragmentManager, "example");
    }

    private void onFailure(Exception e) {
        DialogOops dialogOops = new DialogOops(e.getMessage());
        dialogOops.show(fragmentManager, "example");
    }

    @Override
    public void onMenuClick(int position) {
        if (!stateOnClick) {
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra(ID_CHAT, mData.get(position).getIdChat());
            intent.putExtra(ID_SPECIALIST, mData.get(position).getId());
            intent.putExtra(USER, typeUser);
            context.startActivity(intent);
        } else {
            DialogFriendRequest request;
            request = new DialogFriendRequest(typeUser, mData.get(position).getName(),
                    mData.get(position).getMessage(), mData.get(position).getId());
            request.show(fragmentManager, "");
        }
    }

}
