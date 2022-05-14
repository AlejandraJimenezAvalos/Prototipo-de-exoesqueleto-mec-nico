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

public class DialogAllDone extends AppCompatDialogFragment {

    private String message;

    public DialogAllDone(String message) {
        this.message = message;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder;
        LayoutInflater inflater;
        View view;
        Button buttonAllDone;
        TextView textProcess;

        builder = new AlertDialog.Builder(getActivity());
        inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_all_done, null);
        builder.setView(view);

        textProcess = view.findViewById(R.id.text_process);
        textProcess.setText(message);

        buttonAllDone = view.findViewById(R.id.button_all_done);
        buttonAllDone.setOnClickListener(v -> dismiss());

        return builder.create();
    }
}
