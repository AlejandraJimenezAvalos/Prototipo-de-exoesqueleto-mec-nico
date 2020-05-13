package com.example.exoesqueletov1.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.exoesqueletov1.Constants;
import com.example.exoesqueletov1.MainActivity;
import com.example.exoesqueletov1.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DialogUpdateData extends AppCompatDialogFragment {

    private String data;
    private int recourse;
    private TextView textView;
    private int i = 1;
    private String id;
    private String idUserTo;

    public DialogUpdateData(String data, int recourse, TextView textView) {
        this.data = data;
        this.recourse = recourse;
        this.textView = textView;
    }

    DialogUpdateData(String data, int resource, int i, String id, String idUserTo) {
        this.data = data;
        this.recourse = resource;
        this.i = i;
        this.id = id;
        this.idUserTo = idUserTo;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final TextInputLayout textInputLayoutData;
        Button buttonOk;
        Button buttonCancel;

        AlertDialog.Builder builder;
        LayoutInflater inflater;
        View view;

        builder = new AlertDialog.Builder(getActivity());
        inflater = getActivity().getLayoutInflater();

        view = inflater.inflate(R.layout.dialog_update_data, null);
        builder.setView(view);


        textInputLayoutData = view.findViewById(R.id.text_input_update);
        buttonOk = view.findViewById(R.id.button_input_update_ok);
        buttonCancel = view.findViewById(R.id.button_input_update_cancel);

        textInputLayoutData.getEditText().setHint(data);
        textInputLayoutData.getEditText().
                setCompoundDrawablesWithIntrinsicBounds(recourse, 0, 0, 0);
        if (i == 1) {
            if (data.equals(getString(R.string.correo))) {
                textInputLayoutData.getEditText().
                        setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            }
            if (data.equals(getString(R.string.celular)) || data.equals(getString(R.string.telefono))) {
                textInputLayoutData.getEditText().setInputType(InputType.TYPE_CLASS_PHONE);
            }
            if (data.equals(getString(R.string.direcci_n))) {
                textInputLayoutData.getEditText().
                        setInputType(InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS);
            }
            if (data.equals(getString(R.string.nombre)) || data.equals(getString(R.string.apellidos))) {
                textInputLayoutData.getEditText().
                        setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
            }
        }

        buttonOk.setOnClickListener(v -> {
            if (i == 1 ) {
                textView.setText(textInputLayoutData.getEditText().getText().
                        toString().trim());
            }
            else {
                Map<String, Object> data = new HashMap<>();

                data.put(Constants.TITLE, getString(R.string.report_message));
                data.put(Constants.DATE, DateFormat.format("MMMM d, yyyy ",
                        new Date().getTime()));
                data.put(Constants.DESCRIPTION, getString(R.string.click_more_info));
                data.put(Constants.STATE_NOTIFY, false);

                data.put(Constants.REASON, textInputLayoutData.getEditText().getText().
                        toString().trim());
                data.put(Constants.CODE,
                        Constants.CODE_NOTIFICATIONS_DELET_REQUEST_FOR_INFRACTION);

                data.put(Constants.FROM, id);
                data.put(Constants.ID_USER_INFRACTION, idUserTo);
                data.put(Constants.TO, "a");

                FirebaseFirestore.getInstance().
                        collection(Constants.COLLECTION_NOTIFICATIONS).add(data);
                getActivity().finish();
                startActivity(new Intent(getContext(), MainActivity.class));
            }
            dismiss();
        });

        buttonCancel.setOnClickListener(v -> dismiss());

        return builder.create();
    }

}
