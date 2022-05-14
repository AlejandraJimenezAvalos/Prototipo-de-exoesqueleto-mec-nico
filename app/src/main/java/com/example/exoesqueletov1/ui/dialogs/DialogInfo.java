package com.example.exoesqueletov1.ui.dialogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.exoesqueletov1.R;

public class DialogInfo extends AppCompatDialogFragment {

    private int id;

    public DialogInfo(int id) {
        this.id = id;
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_info, null);
        Button buttonOk = view.findViewById(R.id.button_ok);
        ImageView imageView2 = view.findViewById(R.id.imageView2);
        imageView2.setImageDrawable(getContext().getDrawable(id));

        buttonOk.setOnClickListener(v -> dismiss());

        builder.setView(view);
        return builder.create();
    }

}
