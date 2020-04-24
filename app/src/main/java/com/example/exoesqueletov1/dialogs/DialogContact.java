package com.example.exoesqueletov1.dialogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.exoesqueletov1.MainActivity;
import com.example.exoesqueletov1.R;
import com.example.exoesqueletov1.clases.Database;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

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
    private static final int CODE_DELATE = 1;

    public DialogContact(String id, String idUserTo, String idChat, String typeUser) {
        this.id = id;
        this.idUserTo = idUserTo;
        this.idChat = idChat;
        this.typeUser = typeUser;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.dialog_contact, null);

        CircleImageView circleImageView = view.findViewById(R.id.image_view_profile_view);
        textViewName = view.findViewById(R.id.text_profile_view_name);
        textViewUser = view.findViewById(R.id.text_profile_view_user);
        TextView textViewCheck = view.findViewById(R.id.text_profile_view_check);
        TextView textViewAddress = view.findViewById(R.id.text_profile_view_address);
        TextView textViewSchool = view.findViewById(R.id.text_profile_view_school);
        LinearLayout linearLayoutCell = view.findViewById(R.id.linear_to_call_cell);
        LinearLayout linearLayoutPhone = view.findViewById(R.id.linear_to_call_phone);
        LinearLayout linearLayoutMail = view.findViewById(R.id.linear_send_email);
        Button buttonReport = view.findViewById(R.id.button_report);
        Button buttonDimiss = view.findViewById(R.id.button_dimiss);

        final Button buttonDelete = view.findViewById(R.id.button_delete);
        final TextView textViewPhone = view.findViewById(R.id.text_profile_view_phone);
        final TextView textViewCell = view.findViewById(R.id.text_profile_view_cell);
        final Database database = new Database(getFragmentManager(), getContext());

        textViewMail = view.findViewById(R.id.text_profile_view_email);

        database.getAndShowProfile(idUserTo, textViewName, textViewUser, textViewCheck, textViewMail,
                textViewAddress, textViewCell, textViewPhone, textViewSchool, circleImageView);

        linearLayoutCell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPhone(textViewCell.getText().toString());
            }
        });

        linearLayoutMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMail();
            }
        });

        buttonReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                report();
            }
        });

        linearLayoutPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPhone(textViewPhone.getText().toString());
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteContact();
            }
        });

        buttonDimiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        builder.setView(view);

        return builder.create();
    }

    private void callPhone(String phoneNumber) {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
    }

    @SuppressLint("IntentReset")
    private void sendMail() {
        String[] email = textViewMail.getText().toString().split(",");
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, email);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Necesito ayuda.");
        intent.putExtra(Intent.EXTRA_TEXT, "Estimado (a) " + textViewName.getText().toString());
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void deleteContact() {
        final CharSequence[] options = { getString(R.string.se_acabo_el_tratamiento),
        getString(R.string.poca_profecionalidad)};

        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("¿Cuál es la razón para eliminar este contacto?");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals(getString(R.string.se_acabo_el_tratamiento))) {
                    delete(CODE_DELATE);
                }
                if (options[item].equals(getString(R.string.poca_profecionalidad))) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                    builder1.setTitle(R.string.reportar_user);
                    builder1.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            delete(CODE_DELATE);
                        }
                    });
                    builder1.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            report();
                        }
                    });
                    builder1.show();
                }
            }
        });
        builder.show();
    }

    private void report() {
        delete(CODE_REPORT);
        DialogUpdateData updateData = new DialogUpdateData(getString(R.string.please_write),
                0, 0, id, idUserTo);
        updateData.show(getFragmentManager(), "");
    }

    private void delete(final int code) {
        try {
            db.collection(Database.COLLECTION_CHATS).whereEqualTo(Database.ID_CHAT, idChat).get().
                    addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for (DocumentSnapshot document : task.getResult()) {
                                db.collection(Database.COLLECTION_CHATS).document(document.getId())
                                        .delete();
                            }
                        }
                    });

            db.collection(idChat).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                db.collection(idChat).document(documentSnapshot.getId()).
                                        delete();
                            }
                        }
                    });
            if (textViewUser.getText().toString().equals("Paciente")) {
                db.collection(Database.COLLECTION_USERS).document(idUserTo).get().
                        addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                final Map<String, Object> data = task.getResult().getData();
                                data.remove(Database.SPECIALIST);
                                data.put(Database.SPECIALIST, false);
                                db.collection(Database.COLLECTION_USERS).document(idUserTo).update(data);
                            }
                        });
            }
            if (typeUser.equals("c")) {
                db.collection(Database.COLLECTION_USERS).document(id).get().
                        addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                final Map<String, Object> data = task.getResult().getData();
                                data.remove(Database.SPECIALIST);
                                data.put(Database.SPECIALIST, false);
                                db.collection(Database.COLLECTION_USERS).document(id).update(data);
                            }
                        });
            }

            db.collection(Database.COLLECTION_USERS).document(id).get().
                    addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            String name;
                            name = documentSnapshot.getData().get(Database.NAME).toString();
                            final Map<String, Object> data = new HashMap<>();
                            data.put(Database.CODE, Database.CODE_NOTIFICATIONS_DELET_REQUEST);
                            data.put(Database.DATE, DateFormat.format("MMMM d, yyyy ",
                                    new Date().getTime()));
                            data.put(Database.DESCRIPTION, "");
                            data.put(Database.STATE_NOTIFY, false);
                            data.put(Database.TITLE, name + getString(R.string.de_su_lista_de_contactos));
                            data.put(Database.TO, idUserTo);
                            db.collection(Database.COLLECTION_NOTIFICATIONS).add(data);
                            if (code == CODE_DELATE) {
                                dismiss();
                                getActivity().finish();
                                startActivity(new Intent(getContext(), MainActivity.class));
                            }
                        }
                    });
            Toast.makeText(getContext(), "e: " + code, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getContext(), "e: " + e, Toast.LENGTH_LONG).show();
        }
    }
}
