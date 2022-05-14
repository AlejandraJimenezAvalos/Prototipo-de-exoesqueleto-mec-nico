package com.example.exoesqueletov1.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.exoesqueletov1.R;
import com.example.exoesqueletov1.clases.Authentication;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

public class DialogLostYourPass extends AppCompatDialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder;
        LayoutInflater inflater;
        View view;

        final TextInputLayout layoutEmail;
        Button btOk;


        builder = new AlertDialog.Builder(getActivity());
        inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_lost_your_pass, null);
        builder.setView(view);

        btOk = view.findViewById(R.id.button_ok_dialog_lost);
        layoutEmail = view.findViewById(R.id.correo_dialog_lost);

        try {
            btOk.setOnClickListener(v -> {
                String email = layoutEmail.getEditText().getText().toString().trim();
                layoutEmail.setError(null);
                layoutEmail.getEditText().setTextColor(Color.BLACK);
                if(validateFields(layoutEmail, email)) {
                    new Authentication(getFragmentManager()).sendPasswordReset(email);
                    dismiss();
                }
            });
        } catch (Exception e) {
            Toast.makeText(getContext(), "error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        return builder.create();
    }

    private boolean validateFields(TextInputLayout layoutEmail, String email) {
        boolean result = false;
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        if (!email.isEmpty()) {
            if (pattern.matcher(email).matches()) { result = true; }
            else {
                layoutEmail.setError(getString(R.string.error_correo_invalido));
                layoutEmail.getEditText().setTextColor(Color.WHITE);
            }
        }
        else {
            layoutEmail.setError(getString(R.string.error_introducir_email));
            layoutEmail.getEditText().setTextColor(Color.WHITE);
        }
        return result;
    }
}
