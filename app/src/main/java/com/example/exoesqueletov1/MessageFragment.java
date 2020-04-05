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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exoesqueletov1.clases.Authentication;
import com.example.exoesqueletov1.clases.ChatAdapter;
import com.example.exoesqueletov1.clases.ChatItem;
import com.example.exoesqueletov1.dialog.DialogOops;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MessageFragment extends Fragment  implements ChatAdapter.OnMenuListener {

    private EditText editTextSearch;
    private View viewChats;
    private View viewAdd;
    private RecyclerView recyclerView;

    private List<ChatItem> mData;
    private ChatAdapter chatAdapter;
    private String typeUser;
    private boolean stateOnClick = false;

    MessageFragment(String typeUser) {
        this.typeUser = typeUser;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_messages, container, false);

        TextView textViewAdd = view.findViewById(R.id.text_add);
        TextView textViewChats = view.findViewById(R.id.text_chats);
        TextView textViewCancel = view.findViewById(R.id.text_fragment_message_cancel);
        viewChats = view.findViewById(R.id.view_chats);
        viewAdd = view.findViewById(R.id.view_add);
        editTextSearch = view.findViewById(R.id.search_edit_text);
        recyclerView = view.findViewById(R.id.recycler_chats);

        setChats();

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                chatAdapter.getFilter().filter(s);
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
                setChats();
                stateOnClick = false;
            }
        });

        textViewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (typeUser.equals("c")) {
                    DialogOops oops = new DialogOops("Por la seguridad de sus datos, usted no puede buscar personas para platicar con ellas.");
                    oops.show(getFragmentManager(), "");
                } else {
                    stateOnClick = true;
                    viewAdd.setBackgroundResource(R.color.pinkDark);
                    viewChats.setBackgroundResource(R.color.blueDark);
                    setUsers();
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

    private void setUsers() {
        if (typeUser.equals("a")) {
            FirebaseFirestore.getInstance().
                    collection("users").whereEqualTo("user", "c").get().
                    addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                        @Override
                        public void onComplete(@NonNull final Task<QuerySnapshot> taskC) {
                            FirebaseFirestore.getInstance().
                                    collection("users").whereEqualTo("user", "b").get().
                                    addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> taskB) {
                                            mData = new ArrayList<>();

                                            for (QueryDocumentSnapshot documentC : taskC.getResult()) {
                                                String id = documentC.getData().get("id").toString();
                                                String name = documentC.getData().get("name").toString();
                                                mData.add(new ChatItem(id, name, "", "Pasiente"));
                                            }

                                            for (QueryDocumentSnapshot documentB : taskB.getResult()) {
                                                String id = documentB.getData().get("id").toString();
                                                String name = documentB.getData().get("name").toString();
                                                mData.add(new ChatItem(id, name, "", "Fisioterapeuta"));
                                            }

                                            chatAdapter = new ChatAdapter(getContext(), mData, MessageFragment.this);
                                            recyclerView.setAdapter(chatAdapter);
                                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

                                        }
                                    });
                        }

                    });
        }
        if (typeUser.equals("b")) {
            FirebaseFirestore.getInstance().
                    collection("users").whereEqualTo("user", "c").get().
                    addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull final Task<QuerySnapshot> task) {

                            mData = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String id = document.getData().get("id").toString();
                                String name = document.getData().get("name").toString();
                                mData.add(new ChatItem(id, name, "", "Pasiente"));
                            }

                            chatAdapter = new ChatAdapter(getContext(), mData, MessageFragment.this);
                            recyclerView.setAdapter(chatAdapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                        }
                    });
        }
    }

    private void setChats() {
        String id = new Authentication().getCurrentUser().getEmail();
        mData = new ArrayList<>();
        mData.add(new ChatItem(id, "Ale", "17/01/2019", "hola, ¿Como estás?"));
        mData.add(new ChatItem(id, "Paco", "17/01/2019", "hola, ¿Como estás?"));
        mData.add(new ChatItem(id, "Juan", "17/01/2019", "hola, ¿Como estás?"));
        mData.add(new ChatItem(id, "Emo", "17/01/2019", "hola, ¿Como estás?"));
        mData.add(new ChatItem(id, "Ema", "17/01/2019", "hola, ¿Como estás?"));
        mData.add(new ChatItem(id, "Vero", "17/01/2019", "hola, ¿Como estás?"));
        mData.add(new ChatItem(id, "Peter", "17/01/2019", "hola, ¿Como estás?"));
        mData.add(new ChatItem(id, "Alan", "17/01/2019", "hola, ¿Como estás?"));

        chatAdapter = new ChatAdapter(getContext(), mData, this);
        recyclerView.setAdapter(chatAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void onMenuClick(int position) {
        if (!stateOnClick) {

        }
    }
}
