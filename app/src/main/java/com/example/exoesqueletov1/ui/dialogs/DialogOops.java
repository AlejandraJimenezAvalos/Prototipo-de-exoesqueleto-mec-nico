package com.example.exoesqueletov1.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.exoesqueletov1.R;

public class DialogOops extends AppCompatDialogFragment {

    private String message;

    public DialogOops(String message) {
        this.message = message;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Button okButton;
        Button out;
        AlertDialog.Builder builder;
        LayoutInflater inflater;
        View view;
        TextView texOutputError;

        builder = new AlertDialog.Builder(getActivity());
        inflater = getActivity().getLayoutInflater();

        view = inflater.inflate(R.layout.dialog_oops, null);
        builder.setView(view);

        okButton = view.findViewById(R.id.button_oops);
        okButton.setOnClickListener(v -> dismiss());

        out = view.findViewById(R.id.button_oops_out);
        out.setOnClickListener(v -> getActivity().finish());

        texOutputError = view.findViewById(R.id.tex_output_error);
        texOutputError.setText(message);

        return builder.create();
    }

}
