package com.example.exoesqueletov1.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.exoesqueletov1.R;
import com.google.android.material.textfield.TextInputLayout;

public class DialogUpdateData extends AppCompatDialogFragment {

    private String data;
    private int resourse;
    private TextView textView;

    public DialogUpdateData(String data, int resourse, TextView textView) {
        this.data = data;
        this.resourse = resourse;
        this.textView = textView;
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
        textInputLayoutData.getEditText().setCompoundDrawablesWithIntrinsicBounds(resourse, 0, 0, 0);
        if (data.equals(getString(R.string.correo))) {
            textInputLayoutData.getEditText().setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        }
        if (data.equals(getString(R.string.celular)) || data.equals(getString(R.string.telefono))) {
            textInputLayoutData.getEditText().setInputType(InputType.TYPE_CLASS_PHONE);
        }
        if (data.equals(getString(R.string.direcci_n))) {
            textInputLayoutData.getEditText().setInputType(InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS);
        }

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText(textInputLayoutData.getEditText().getText().toString().trim());
                dismiss();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return builder.create();
    }

}
