package com.example.exoesqueletov1;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exoesqueletov1.clases.Authentication;
import com.example.exoesqueletov1.clases.Database;
import com.example.exoesqueletov1.dialog.DialogOops;

public class MessageFragment extends Fragment {

    private EditText editTextSearch;
    private View viewChats;
    private View viewAdd;
    private RecyclerView recyclerView;

    private String typeUser;

    MessageFragment(String typeUser) {
        this.typeUser = typeUser;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_messages, container, false);
        final String id = new Authentication().getCurrentUser().getEmail();

        TextView textViewAdd = view.findViewById(R.id.text_add);
        TextView textViewChats = view.findViewById(R.id.text_chats);
        TextView textViewCancel = view.findViewById(R.id.text_fragment_message_cancel);
        viewChats = view.findViewById(R.id.view_chats);
        viewAdd = view.findViewById(R.id.view_add);
        editTextSearch = view.findViewById(R.id.search_edit_text);
        recyclerView = view.findViewById(R.id.recycler_chats);

        final Database database = new Database(getFragmentManager(), getContext());
        database.getChats(id, recyclerView, typeUser);

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                database.search(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        textViewChats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewChats.setBackgroundResource(R.color.pinkDark);
                viewAdd.setBackgroundResource(R.color.blueDark);
                database.getChats(id, recyclerView, typeUser);
            }
        });

        textViewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (typeUser.equals("c")) {
                    DialogOops oops = new DialogOops("Por la seguridad de sus datos, usted no puede hacer mas amistades.");
                    oops.show(getFragmentManager(), "");
                } else {
                    viewAdd.setBackgroundResource(R.color.pinkDark);
                    viewChats.setBackgroundResource(R.color.blueDark);
                    database.getUsers(typeUser, recyclerView);
                }
            }
        });

        textViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextSearch.setText("");
            }
        });

        return view;
    }

}
