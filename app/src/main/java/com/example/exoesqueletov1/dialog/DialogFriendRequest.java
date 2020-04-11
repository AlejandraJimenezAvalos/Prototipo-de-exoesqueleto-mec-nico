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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class DialogFriendRequest extends AppCompatDialogFragment {

    private String name;
    private String type;
    private String id;

    private static final String COLLECTION_NOTIFICATIONS = "notifications";
    private static final String DOCUMENT_PROFILE = "profile";
    private static final String TITLE = "title";
    private static final String DATE = "date";
    private static final String DESCRIPTION = "description";
    private static final String CODE = "code";
    private static final String STATE_NOTIFY = "stateNotify";
    private static final String TO = "to";
    private static final String DOCUMENT_PATIENTS = "Patients";
    private static final String ID_PATIENT = "idPatient";
    private static final String ID_CHAT = "idChat";
    private static final String ID_SPECIALIST = "idSpecialist";
    private static final String STATE_REQUEST = "stateRequest";
    private static final String NAME_PATIENT = "namePatient";
    private static final String CHAT = "Chat";

    private static final int CODE_FRIEND_REQUEST = 0;

    public DialogFriendRequest(String name, String type, String id) {
        this.name = name;
        this.type = type;
        this.id = id;
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
                Database database = new Database(getFragmentManager(), getContext());
                Map<String, Object> dataNotification = new HashMap<>();
                String idChat = UUID.randomUUID().toString();
                String idD = UUID.randomUUID().toString();

                dataNotification.put(TITLE, "Un fisioterapeuta quiere conectarse contigo.");
                dataNotification.put(DESCRIPTION, "Da click para ver mas informacion");
                dataNotification.put(DATE, DateFormat.format("MMMM d, yyyy ", new Date().getTime()));
                dataNotification.put(CODE, CODE_FRIEND_REQUEST);
                dataNotification.put(TO, id);
                dataNotification.put(STATE_NOTIFY, false);

                //escribe la notificación en la carpeta de notificaciones
                database.setData(COLLECTION_NOTIFICATIONS , "", dataNotification);

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

}
