package com.example.exoesqueletov1.dialog;

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

import com.example.exoesqueletov1.R;
import com.example.exoesqueletov1.clases.Authentication;
import com.example.exoesqueletov1.clases.Database;
import com.example.exoesqueletov1.clases.Storge;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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
    private static final String COLLECTION_CHATS = "chats";
    private static final String COLLECTION_NOTIFICATIONS = "notifications";
    private static final String DOCUMENT_PROFILE = "profile";
    private static final String TITLE = "title";
    private static final String DATE = "date";
    private static final String DESCRIPTION = "description";
    private static final String CODE = "code";
    private static final String STATE_NOTIFY = "stateNotify";
    private static final String TO = "to";
    private static final String STATE = "state";
    private static final String ID_PATIENT = "idPatient";
    private static final String ID_CHAT = "idChat";
    private static final String ID_SPECIALIST = "idSpecialist";

    private static final int CODE_FRIEND_REQUEST = 0;
    private static final int CODE_ADMIN_REQUEST = 1;

    public DialogFriendRequest(String typeUser, String name, String type, String id) {
        this.name = name;
        this.type = type;
        this.id = id;
        this.typeUser = typeUser;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_friend_request, null);

        CircleImageView circleImageView = view.findViewById(R.id.image_dialog_request);
        TextView textViewName = view.findViewById(R.id.text_dialog_request_name);
        TextView textViewType = view.findViewById(R.id.text_dialog_request_type);
        final TextView textViewDescription = view.findViewById(R.id.text_dialog_request_description);
        Button buttonAdd = view.findViewById(R.id.button_dialog_request_add);
        Button buttonCancel = view.findViewById(R.id.button_dialog_request_cancel);
        final String idUser = new Authentication().getCurrentUser().getEmail();

        textViewName.setText(name);
        textViewType.setText(type);
        FirebaseFirestore.getInstance().collection(id).document(DOCUMENT_PROFILE).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                textViewDescription.setText(documentSnapshot.getData().get(DESCRIPTION).toString());
            }
        });

        new Storge().getProfileImage(circleImageView, id);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (typeUser.equals("a") || typeUser.equals("b")) { sendRequest(idUser); }
                if (typeUser.equals("c")) { toAccept(idUser); }
                dismiss();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        builder.setView(view);
        return builder.create();
    }

    private void toAccept(String idUser) {
        FirebaseFirestore.getInstance().collection(COLLECTION_CHATS).whereEqualTo(ID_PATIENT, idUser).
                get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    if (document.getData().get(ID_SPECIALIST).toString().equals(id)) {
                        Map<String, Object> data = document.getData();
                        data.remove(STATE);
                        data.put(STATE, true);
                        FirebaseFirestore.getInstance().collection(COLLECTION_CHATS).
                                document(document.getId()).update(data).
                                addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                    }
                                });
                    }
                }
            }
        });
    }

    private void sendRequest(String idUser) {
        Database database = new Database(getFragmentManager(), getContext());
        Map<String, Object> dataNotification = new HashMap<>();
        Map<String, Object> dataChats = new HashMap<>();
        String idChat = UUID.randomUUID().toString();
        Map<String, Object> data = new HashMap<>();

        if (typeUser.equals("a") || typeUser.equals("b")) {
            if (typeUser.equals("b")) {
                dataNotification.put(TITLE, getString(R.string.un_fisioterapeuta));
                dataNotification.put(CODE, CODE_FRIEND_REQUEST);
            }
            if (typeUser.equals("a")) {
                dataNotification.put(TITLE, getString(R.string.un_admin));
                dataNotification.put(CODE, CODE_ADMIN_REQUEST);
            }

            dataNotification.put(DESCRIPTION, getString(R.string.more_info));
            dataNotification.put(DATE, DateFormat.format("MMMM d, yyyy ", new Date().getTime()));
            dataNotification.put(TO, id);
            dataNotification.put(STATE_NOTIFY, false);

            database.setData(COLLECTION_NOTIFICATIONS , "", dataNotification);

            dataChats.put(ID_SPECIALIST, idUser);
            dataChats.put(ID_PATIENT, id);
            dataChats.put(ID_CHAT, idChat);
            if (typeUser.equals("b")) { dataChats.put(STATE, false); }
            if (typeUser.equals("a")) { dataChats.put(STATE, true); }

            database.setData(COLLECTION_CHATS, "", dataChats);

            data.put("hi", "hi");

            FirebaseFirestore.getInstance().collection(idChat).add(data);
        }
    }

}
