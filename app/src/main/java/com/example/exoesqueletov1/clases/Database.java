package com.example.exoesqueletov1.clases;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exoesqueletov1.ChatActivity;
import com.example.exoesqueletov1.Constants;
import com.example.exoesqueletov1.R;
import com.example.exoesqueletov1.dialogs.DialogAllDone;
import com.example.exoesqueletov1.dialogs.DialogFriendRequest;
import com.example.exoesqueletov1.dialogs.DialogLoading;
import com.example.exoesqueletov1.dialogs.DialogOops;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

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
                    addOnCompleteListener(taskC -> db.collection("users")
                            .whereEqualTo("user", "b")
                            .get().addOnCompleteListener(taskB -> {
                                mData = new ArrayList<>();

                                for (QueryDocumentSnapshot documentC : taskC.getResult()) {
                                    String id = documentC.getData().get(Constants.ID).toString();
                                    String name = documentC.getData().get(Constants.NAME).toString();
                                    mData.add(new ChatItem(id, name, "",
                                            "Pasiente", ""));
                                }

                                for (QueryDocumentSnapshot documentB : taskB.getResult()) {
                                    String id = documentB.getData().get("id").toString();
                                    String name = documentB.getData().get("name").toString();
                                    mData.add(new ChatItem(id, name, "",
                                            "Fisioterapeuta", ""));
                                }

                                chatAdapter = new ChatAdapter(context, mData,
                                        Database.this);
                                recyclerView.setAdapter(chatAdapter);
                                recyclerView.setLayoutManager(new LinearLayoutManager(context,
                                        LinearLayoutManager.VERTICAL, false));

                            }));
        }
        if (typeUser.equals("b")) {
            FirebaseFirestore.getInstance().
                    collection("users")
                    .whereEqualTo(Constants.SPECIALIST, false).get()
                    .addOnCompleteListener(task -> {

                        mData = new ArrayList<>();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String id = document.getData().get("id").toString();
                            String name = document.getData().get("name").toString();
                            mData.add(new ChatItem(id, name, "",
                                    "Pasiente", ""));
                        }

                        chatAdapter = new ChatAdapter(context, mData, Database.this);
                        recyclerView.setAdapter(chatAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(context,
                                LinearLayoutManager.VERTICAL, false));
                    });
        }
        if (typeUser.equals("c")) {
            String id = new Authentication().getCurrentUser().getEmail();
            db.collection(Constants.COLLECTION_CHATS).whereEqualTo(Constants.ID_PATIENT, id).get().
                    addOnCompleteListener(task -> {
                        mData = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if (!Boolean.parseBoolean(document.getData()
                                    .get(Constants.STATE).toString())) {
                                mData.add(new ChatItem(document.getData()
                                        .get(Constants.ID_SPECIALIST).toString(),
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
        stateOnClick = false;

        String field = "";
        String otherField = "";
        mData = new ArrayList<>();

        if (typeUser.equals("a") || typeUser.equals("b")) {
            field = Constants.ID_SPECIALIST;
            otherField = Constants.ID_PATIENT;
        }
        if (typeUser.equals("c")) {
            field = Constants.ID_PATIENT;
            otherField = Constants.ID_SPECIALIST;
        }
        final String finalOtherField = otherField;
        db.collection(Constants.COLLECTION_CHATS).whereEqualTo(field, id).get()
                .addOnCompleteListener(task -> {
                    mData = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (Boolean.parseBoolean(document.getData().get(Constants.STATE).toString())) {
                            Toast.makeText(context, document.getData()
                                    .get(finalOtherField).toString(), Toast.LENGTH_SHORT).show();
                            mData.add(new ChatItem(document.getData()
                                    .get(finalOtherField).toString(),
                                    "", "", "",
                                    document.getData().get(Constants.ID_CHAT).toString()));
                        }
                    }
                    if (typeUser.equals("b")) {
                        db.collection(Constants.COLLECTION_CHATS)
                                .whereEqualTo(Constants.ID_PATIENT, id).get()
                                .addOnCompleteListener(task1 -> {
                                    for (QueryDocumentSnapshot document : task1.getResult()) {
                                        if (Boolean.parseBoolean(document.getData()
                                                .get(Constants.STATE).toString())) {
                                            mData.add(new ChatItem(document.getData()
                                                    .get(Constants.ID_SPECIALIST).toString(),
                                                    "", "", "", document.
                                                    getData().get(Constants.ID_CHAT).toString()));
                                        }
                                    }
                                    chatAdapter = new ChatAdapter(context, mData,
                                            Database.this);
                                    recyclerView.setAdapter(chatAdapter);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(context,
                                            LinearLayoutManager.VERTICAL, false));
                                });
                    }
                    if (typeUser.equals("a")) {
                        chatAdapter = new ChatAdapter(context, mData,
                                Database.this);
                        recyclerView.setAdapter(chatAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(context,
                                LinearLayoutManager.VERTICAL, false));
                    }
                });
    }

    public void getProfile(final String id, final TextView textViewName, final TextView textViewUser,
                           final TextView textViewDes, final TextView textViewMail,
                           final TextView textViewAddress, final TextView textViewCell,
                           final TextView textViewPhone, final TextView textViewSchool,
                           final CircleImageView circleImageViewProfile,
                           final LinearLayout linearLayoutSchool) {
        db.collection(id).document(Constants.DOCUMENT_PROFILE)
                .get().addOnSuccessListener(documentSnapshot -> {

                    String typeUser = "";

                    String user = documentSnapshot.getData().get(Constants.USER).toString();
                    if (user.equals("a")) { typeUser = "Administrador"; }
                    if (user.equals("b")) { typeUser = "Fisioterapeuta"; }
                    if (user.equals("c")) {
                        typeUser = "Paciente";
                        linearLayoutSchool.setVisibility(View.INVISIBLE);
                    }
                    textViewName.setText(documentSnapshot.getData()
                            .get(Constants.NAME).toString());
                    textViewUser.setText(typeUser);
                    textViewDes.setText(documentSnapshot.getData()
                            .get(Constants.DESCRIPTION).toString());
                    textViewMail.setText(documentSnapshot.getData()
                            .get(Constants.EMAIL).toString());
                    textViewAddress.setText(documentSnapshot.getData()
                            .get(Constants.ADDRESS).toString());
                    textViewCell.setText(documentSnapshot.getData()
                            .get(Constants.CELL).toString());
                    textViewPhone.setText(documentSnapshot.getData()
                            .get(Constants.PHONE).toString());
                    textViewSchool.setText(documentSnapshot.getData()
                            .get(Constants.SCHOOL).toString());

                    new Storge().getProfileImage(circleImageViewProfile, id);
                });
    }

    public void getAndShowProfile(final String id, final TextView textViewName, final TextView textViewUser,
                                  final TextView textViewCheck, final TextView textViewMail,
                                  final TextView textViewAddress, final TextView textViewCell,
                                  final TextView textViewPhone, final TextView textViewSchool,
                                  final CircleImageView circleImageViewProfile) {
        db.collection(id).document(Constants.DOCUMENT_PROFILE)
                .get().addOnSuccessListener(documentSnapshot -> {

                    String typeUser = "";

                    String user = documentSnapshot.getData().get(Constants.USER).toString();
                    if (user.equals("a")) { typeUser = "Administrador"; }
                    if (user.equals("b")) { typeUser = "Fisioterapeuta"; }
                    if (user.equals("c")) {
                        typeUser = "Paciente";
                        textViewSchool.setVisibility(View.INVISIBLE);
                    }
                    textViewName.setText(documentSnapshot.getData()
                            .get(Constants.NAME).toString());
                    textViewUser.setText(typeUser);
                    textViewMail.setText(documentSnapshot.getData()
                            .get(Constants.EMAIL).toString());
                    textViewAddress.setText(documentSnapshot.getData()
                            .get(Constants.ADDRESS).toString());
                    textViewCell.setText(documentSnapshot.getData()
                            .get(Constants.CELL).toString());
                    textViewPhone.setText(documentSnapshot.getData()
                            .get(Constants.PHONE).toString());
                    textViewSchool.setText(documentSnapshot.getData()
                            .get(Constants.SCHOOL).toString());

                    new Storge().getProfileImage(circleImageViewProfile, id);
                });

        db.collection(Constants.COLLECTION_USERS).document(id).get()
                .addOnCompleteListener(task -> {
                    Map<String, Object> data = task.getResult().getData();
                    if (Boolean.parseBoolean(data.get(Constants.VERIFY).toString())) {
                        textViewCheck.setText(R.string.cuenta_verificada);
                    } else {
                        textViewCheck.setText(R.string.cuenta_sin_verificar);
                    }
                });
    }

    public void initMain(final TextView name, final TextView typeUser, final TextView state,
                         final String id, final CircleImageView circleImageView) {
        db.collection(id).document(Constants.USER).get()
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
        Map<String, Object> dataNotification;

        loading = new DialogLoading();
        loading.show(fragmentManager, "exmaple");

        dataTypeUser = new HashMap<>();
        dataTypeUser.put(Constants.ID, collectionPath);
        dataTypeUser.put(Constants.USER, userType);

        dataCollectionUsers = new HashMap<>();
        dataCollectionUsers.put(Constants.ID, collectionPath);
        dataCollectionUsers.put(Constants.USER, userType);
        dataCollectionUsers.put(Constants.NAME, dataUser.get("name")
                + " " + dataUser.get("lastName"));
        dataCollectionUsers.put(Constants.VERIFY, false);
        dataCollectionUsers.put(Constants.VERIFY_EMAIL, false);
        if (userType.equals("c")) { dataCollectionUsers.put(Constants.SPECIALIST, false); }

        dataCollectionProfile = new HashMap<>();
        dataCollectionProfile.put(Constants.NAME, dataUser.get("name")
                + " " + dataUser.get("lastName"));
        dataCollectionProfile.put(Constants.USER, userType);
        dataCollectionProfile.put(Constants.DESCRIPTION, "");
        dataCollectionProfile.put(Constants.EMAIL, "");
        dataCollectionProfile.put(Constants.ADDRESS, "");
        dataCollectionProfile.put(Constants.CELL, "");
        dataCollectionProfile.put(Constants.PHONE, "");
        dataCollectionProfile.put(Constants.SCHOOL, "");

        dataNotification = new HashMap<>();

        dataNotification.put(Constants.TITLE, "Un nuevo usuario se ha registrado");
        dataNotification.put(Constants.DESCRIPTION, context.getString(R.string.click_more_info));
        dataNotification.put(Constants.DATE,
                DateFormat.format("MMMM d, yyyy ", new Date().getTime()));
        dataNotification.put(Constants.CODE, Constants.CODE_NOTIFICATIONS_NEW_USER);
        dataNotification.put(Constants.TO, Constants.ADMIN);
        dataNotification.put(Constants.ID_NEW_USER, collectionPath);
        dataNotification.put(Constants.STATE_NOTIFY, false);

        db.collection(collectionPath).document(document).set(dataUser);
        db.collection(collectionPath).document(Constants.DOCUMENT_TYPE).set(dataTypeUser);
        db.collection(collectionPath)
                .document(Constants.DOCUMENT_PROFILE).set(dataCollectionProfile);
        db.collection(Constants.COLLECTION_USERS)
                .document(collectionPath).set(dataCollectionUsers);
        db.collection(Constants.COLLECTION_NOTIFICATIONS).add(dataNotification);

        db.collection(collectionPath).document(document).get().
                addOnSuccessListener(documentSnapshot -> {
                    loading.dismiss();
                    if (collectionPath.equals(documentSnapshot.getData().get("id").toString())) {
                        DialogAllDone dialogAllDone;
                        dialogAllDone = new DialogAllDone(context.
                                getString(R.string.registro_exitoso));
                        dialogAllDone.show(fragmentManager, context.getString(R.string.example));
                    } else {
                        db.collection(collectionPath).document(document).delete();
                        db.collection(collectionPath).document(Constants.DOCUMENT_TYPE).delete();
                        db.collection(Constants.COLLECTION_USERS).document(collectionPath).delete();
                        DialogOops dialogOops = new DialogOops(context.getString(R.string.try_again));
                        dialogOops.show(fragmentManager, context.getString(R.string.example));
                    }
                });

    }

    @Override
    public void onMenuClick(int position) {
        if (!stateOnClick) {
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra(Constants.ID_CHAT, mData.get(position).getIdChat());
            intent.putExtra(Constants.ID_SPECIALIST, mData.get(position).getId());
            intent.putExtra(Constants.USER, typeUser);
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

    private void onSuccess(Void aVoid) {
        DialogAllDone dialogAllDone = new DialogAllDone(context.getString(R.string.operacion_exitosa));
        dialogAllDone.show(fragmentManager, "example");
    }

    private void onFailure(Exception e) {
        DialogOops dialogOops = new DialogOops(e.getMessage());
        dialogOops.show(fragmentManager, "example");
    }
}
