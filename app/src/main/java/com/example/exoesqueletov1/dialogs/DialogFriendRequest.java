package com.example.exoesqueletov1.dialogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.exoesqueletov1.ConstantsDatabase;
import com.example.exoesqueletov1.R;
import com.example.exoesqueletov1.clases.Authentication;
import com.example.exoesqueletov1.clases.Database;
import com.example.exoesqueletov1.clases.Storge;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class DialogFriendRequest extends AppCompatDialogFragment {

    private String name;
    private String type;
    private String id;
    private String typeUser;

    private FirebaseFirestore db;

    public DialogFriendRequest(String typeUser, String name, String type, String id) {
        this.name = name;
        this.type = type;
        this.id = id;
        this.typeUser = typeUser;
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        @SuppressLint("InflateParams") View view;
        view = inflater.inflate(R.layout.dialog_friend_request, null);

        db = FirebaseFirestore.getInstance();

        CircleImageView circleImageView = view.findViewById(R.id.image_dialog_request);
        TextView textViewName = view.findViewById(R.id.text_dialog_request_name);
        TextView textViewType = view.findViewById(R.id.text_dialog_request_type);
        final TextView textViewDescription = view.findViewById(R.id.text_dialog_request_description);
        Button buttonAdd = view.findViewById(R.id.button_dialog_request_add);
        Button buttonCancel = view.findViewById(R.id.button_dialog_request_cancel);
        final String idUser = new Authentication().getCurrentUser().getEmail();

        textViewName.setText(name);
        textViewType.setText(type);
        db.collection(id).document(ConstantsDatabase.DOCUMENT_PROFILE).get()
                .addOnSuccessListener(documentSnapshot ->
                        textViewDescription.setText(documentSnapshot.getData()
                        .get(ConstantsDatabase.DESCRIPTION).toString()));

        new Storge().getProfileImage(circleImageView, id);

        buttonAdd.setOnClickListener(v -> {
            if (typeUser.equals("a") || typeUser.equals("b")) { sendRequest(idUser); }
            if (typeUser.equals("c")) { toAccept(idUser); }
            dismiss();
        });

        buttonCancel.setOnClickListener(v -> dismiss());

        builder.setView(view);
        return builder.create();
    }

    private void toAccept(String idUser) {

        final Database database = new Database(getFragmentManager(), getContext());
        final Map<String, Object> dataNotification = new HashMap<>();

        dataNotification.put(ConstantsDatabase.TITLE, getString(R.string.request_accept));
        dataNotification.put(ConstantsDatabase.DESCRIPTION, getString(R.string.more_info));
        dataNotification.put(ConstantsDatabase.CODE, ConstantsDatabase.CODE_NOTIFICATIONS_TO_ACCEPT);
        dataNotification.put(ConstantsDatabase.DATE, DateFormat.format("MMMM d, yyyy ", new Date().getTime()));
        dataNotification.put(ConstantsDatabase.TO, id);
        dataNotification.put(ConstantsDatabase.STATE_NOTIFY, false);

        db.collection(ConstantsDatabase.COLLECTION_CHATS).whereEqualTo(ConstantsDatabase.ID_PATIENT, idUser).
                get().addOnCompleteListener(task -> {
                    for (final QueryDocumentSnapshot document : task.getResult()) {
                        if (document.getData().get(ConstantsDatabase.ID_SPECIALIST).toString().equals(id)) {
                            final Map<String, Object> data = document.getData();
                            data.remove(ConstantsDatabase.STATE);
                            data.put(ConstantsDatabase.STATE, true);
                            db.collection(ConstantsDatabase.COLLECTION_CHATS).document(document.getId())
                                    .update(data);

                            db.collection(ConstantsDatabase.COLLECTION_USERS).document(new Authentication().
                                    getCurrentUser().getEmail()).get().
                                    addOnSuccessListener(documentSnapshot -> {
                                        Map<String, Object> dataUsers = documentSnapshot.getData();
                                        dataUsers.remove(ConstantsDatabase.SPECIALIST);
                                        dataUsers.put(ConstantsDatabase.SPECIALIST, true);
                                        db.collection(ConstantsDatabase.COLLECTION_USERS)
                                                .document(new Authentication().
                                                getCurrentUser().getEmail()).update(dataUsers);
                                    });

                                database.setData(ConstantsDatabase.COLLECTION_NOTIFICATIONS,
                                        "", dataNotification);
                        }
                    }
                });
    }

    private void sendRequest(String idUser) {
        Database database = new Database(getFragmentManager(), getContext());
        Map<String, Object> dataNotification = new HashMap<>();
        Map<String, Object> dataChats = new HashMap<>();
        String idChat = UUID.randomUUID().toString();

        if (typeUser.equals("a") || typeUser.equals("b")) {
            if (typeUser.equals("b")) {
                dataNotification.put(ConstantsDatabase.TITLE, getString(R.string.un_fisioterapeuta));
                dataNotification.put(ConstantsDatabase.CODE, ConstantsDatabase.CODE_NOTIFICATIONS_FRIEND_REQUEST);
            }
            if (typeUser.equals("a")) {
                dataNotification.put(ConstantsDatabase.TITLE, getString(R.string.un_admin));
                dataNotification.put(ConstantsDatabase.CODE, ConstantsDatabase.CODE_NOTIFICATIONS_ADMIN_REQUEST);
            }

            dataNotification.put(ConstantsDatabase.DESCRIPTION, getString(R.string.more_info));
            dataNotification.put(ConstantsDatabase.DATE, DateFormat
                    .format("MMMM d, yyyy ", new Date().getTime()));
            dataNotification.put(ConstantsDatabase.TO, id);
            dataNotification.put(ConstantsDatabase.STATE_NOTIFY, false);

            database.setData(ConstantsDatabase.COLLECTION_NOTIFICATIONS , "", dataNotification);

            dataChats.put(ConstantsDatabase.ID_SPECIALIST, idUser);
            dataChats.put(ConstantsDatabase.ID_PATIENT, id);
            dataChats.put(ConstantsDatabase.ID_CHAT, idChat);
            if (typeUser.equals("b")) { dataChats.put(ConstantsDatabase.STATE, false); }
            if (typeUser.equals("a")) { dataChats.put(ConstantsDatabase.STATE, true); }

            database.setData(ConstantsDatabase.COLLECTION_CHATS, "", dataChats);

            DialogAllDone dialogAllDone = new DialogAllDone("Solicitud enviada");
            dialogAllDone.show(getFragmentManager(), "");
        }
    }

}
