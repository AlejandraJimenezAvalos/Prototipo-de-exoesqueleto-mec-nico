package com.example.exoesqueletov1.dialogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.exoesqueletov1.ConstantsDatabase;
import com.example.exoesqueletov1.MainActivity;
import com.example.exoesqueletov1.R;
import com.example.exoesqueletov1.clases.Database;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.exoesqueletov1.ConstantsDatabase.COLLECTION_CHATS;
import static com.example.exoesqueletov1.ConstantsDatabase.COLLECTION_NOTIFICATIONS;
import static com.example.exoesqueletov1.ConstantsDatabase.COLLECTION_USERS;
import static com.example.exoesqueletov1.ConstantsDatabase.ID_CHAT;
import static com.example.exoesqueletov1.ConstantsDatabase.NAME;
import static com.example.exoesqueletov1.ConstantsDatabase.SPECIALIST;

public class DialogContact extends AppCompatDialogFragment {

    private TextView textViewMail;
    private TextView textViewName;
    private TextView textViewUser;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private String id;
    private String idUserTo;
    private String idChat;
    private String typeUser;

    private static final int CODE_REPORT = 0;
    private static final int CODE_DELETE = 1;

    public DialogContact(String id, String idUserTo, String idChat, String typeUser) {
        this.id = id;
        this.idUserTo = idUserTo;
        this.idChat = idChat;
        this.typeUser = typeUser;
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        @SuppressLint("InflateParams") View view;
        view = inflater.inflate(R.layout.dialog_contact, null);

        ImageView circleImageView = view.findViewById(R.id.image_view_profile_view);
        TextView textViewCheck = view.findViewById(R.id.text_profile_view_check);
        TextView textViewAddress = view.findViewById(R.id.text_profile_view_address);
        TextView textViewSchool = view.findViewById(R.id.text_profile_view_school);
        ImageView linearLayoutCell = view.findViewById(R.id.imageView3);
        ImageView linearLayoutPhone = view.findViewById(R.id.imageView4);
        ImageView linearLayoutMail = view.findViewById(R.id.imageView5);
        Button buttonReport = view.findViewById(R.id.button_report);
        Button buttonDimiss = view.findViewById(R.id.button_dimiss);

        final Button buttonDelete = view.findViewById(R.id.button_delete);
        final TextView textViewPhone = view.findViewById(R.id.text_profile_view_phone);
        final TextView textViewCell = view.findViewById(R.id.text_profile_view_cell);
        final Database database = new Database(getFragmentManager(), getContext());

        textViewMail = view.findViewById(R.id.text_profile_view_email);
        textViewName = view.findViewById(R.id.text_profile_view_name);
        textViewUser = view.findViewById(R.id.text_profile_view_user);

        database.getAndShowProfile(idUserTo, textViewName, textViewUser, textViewCheck, textViewMail,
                textViewAddress, textViewCell, textViewPhone, textViewSchool, circleImageView);

        linearLayoutCell.setOnClickListener(v -> callPhone(textViewCell.getText().toString()));

        linearLayoutMail.setOnClickListener(v -> sendMail());

        buttonReport.setOnClickListener(v -> report());

        linearLayoutPhone.setOnClickListener(v -> callPhone(textViewPhone.getText().toString()));

        buttonDelete.setOnClickListener(v -> deleteContact());

        buttonDimiss.setOnClickListener(v -> dismiss());

        builder.setView(view);

        return builder.create();
    }

    private void callPhone(String phoneNumber) {
        startActivity(new Intent(Intent.ACTION_DIAL,
                Uri.fromParts("tel", phoneNumber, null)));
    }

    @SuppressLint("IntentReset")
    private void sendMail() {
        String[] email = textViewMail.getText().toString().split(",");
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, email);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Necesito ayuda.");
        intent.putExtra(Intent.EXTRA_TEXT, "Estimado (a) "
                + textViewName.getText().toString());
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void deleteContact() {
        final CharSequence[] options = { getString(R.string.se_acabo_el_tratamiento),
        getString(R.string.poca_profecionalidad)};

        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("¿Cuál es la razón para eliminar este contacto?");
        builder.setItems(options, (dialog, item) -> {
            if (options[item].equals(getString(R.string.se_acabo_el_tratamiento))) {
                delete();
            }
            if (options[item].equals(getString(R.string.poca_profecionalidad))) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                builder1.setTitle(R.string.reportar_user);
                builder1.setNegativeButton(R.string.no, (dialog1, which) -> delete());
                builder1.setPositiveButton(R.string.si, (dialog12, which) -> report());
                builder1.show();
            }
        });
        builder.show();
    }

    private void report() {
        DialogUpdateData updateData = new DialogUpdateData(getString(R.string.please_write),
                0, 0, id, idUserTo);
        updateData.show(getFragmentManager(), "");
    }

    private void delete() {
        try {
            db.collection(COLLECTION_CHATS).whereEqualTo(ID_CHAT, idChat).get().
                    addOnCompleteListener(task -> {
                        for (DocumentSnapshot document : task.getResult()) {
                            db.collection(COLLECTION_CHATS).document(document.getId())
                                    .delete();
                        }
                    });

            db.collection(idChat).get()
                    .addOnCompleteListener(task -> {
                        for (DocumentSnapshot documentSnapshot : task.getResult()) {
                            db.collection(idChat).document(documentSnapshot.getId()).
                                    delete();
                        }
                    });
            if (textViewUser.getText().toString().equals("Paciente")) {
                db.collection(COLLECTION_USERS).document(idUserTo).get().
                        addOnCompleteListener(task -> {
                            final Map<String, Object> data = task.getResult().getData();
                            data.remove(SPECIALIST);
                            data.put(SPECIALIST, false);
                            db.collection(COLLECTION_USERS).document(idUserTo).update(data);
                        });
            }
            if (typeUser.equals("c")) {
                db.collection(COLLECTION_USERS).document(id).get().
                        addOnCompleteListener(task -> {
                            final Map<String, Object> data = task.getResult().getData();
                            data.remove(SPECIALIST);
                            data.put(SPECIALIST, false);
                            db.collection(COLLECTION_USERS).document(id).update(data);
                        });
            }

            db.collection(COLLECTION_USERS).document(id).get().
                    addOnSuccessListener(documentSnapshot -> {
                        String name;
                        name = documentSnapshot.getData().get(NAME).toString();
                        db.collection(COLLECTION_NOTIFICATIONS).get().addOnCompleteListener(task ->{
                            final Map<String, Object> data = new HashMap<>();
                            data.put(ConstantsDatabase.CODE,
                                    ConstantsDatabase.CODE_NOTIFICATIONS_DELETE_REQUEST);
                            data.put(ConstantsDatabase.DATE,
                                    DateFormat.format("MMMM d, yyyy ",
                                            new Date().getTime()));
                            data.put(ConstantsDatabase.DESCRIPTION, "");
                            data.put(ConstantsDatabase.STATE_NOTIFY, false);
                            data.put(ConstantsDatabase.TITLE, name +
                                    getString(R.string.de_su_lista_de_contactos));
                            data.put(ConstantsDatabase.TO, idUserTo);
                            data.put(ConstantsDatabase.NO, task.getResult().size() + 1);
                            db.collection(ConstantsDatabase.COLLECTION_NOTIFICATIONS).add(data);
                        });
                        if (DialogContact.CODE_DELETE == CODE_DELETE) {
                            dismiss();
                            getActivity().finish();
                            startActivity(new Intent(getContext(), MainActivity.class));
                        }
                    });
        } catch (Exception e) {
            Toast.makeText(getContext(), "e: " + e, Toast.LENGTH_LONG).show();
        }
    }
}
