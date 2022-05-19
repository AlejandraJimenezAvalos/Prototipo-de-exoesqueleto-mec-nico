package com.example.exoesqueletov1.ui.fragments

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import com.example.exoesqueletov1.R
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.example.exoesqueletov1.old.clases.Database
import com.example.exoesqueletov1.ui.activity.ConstantsDatabase
import com.example.exoesqueletov1.old.clases.Authentication

class MessageFragment(private val typeUser: String, private val code: Int) : Fragment() {
    private var editTextSearch: SearchView? = null
    //private val viewChats: View? = null
    //private val viewAdd: View? = null
    private var recyclerView: RecyclerView? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val id = Authentication().currentUser.email
        val view: View = inflater.inflate(R.layout.fragment_messages, container, false)
        val textViewAdd = view.findViewById<TextView>(R.id.text_add)
        val textViewChats = view.findViewById<TextView>(R.id.text_chats)
        //TextView textViewCancel = view.findViewById(R.id.text_fragment_message_cancel);
        //viewChats = view.findViewById(R.id.view_chats);
        //viewAdd = view.findViewById(R.id.view_add);
        editTextSearch = view.findViewById(R.id.search_edit_text)
        recyclerView = view.findViewById(R.id.recycler_chats)
        val database = Database(fragmentManager, context, typeUser)
        when (code) {
            ConstantsDatabase.CODE_NOTIFICATIONS_FRIEND_REQUEST -> {
                database.getUsers(recyclerView)
            }
            ConstantsDatabase.CODE_NOTIFICATIONS_ADMIN_REQUEST, ConstantsDatabase.CODE_REGULAR -> database.getChats(
                id,
                recyclerView
            )
        }
        editTextSearch
        /*.addTextChangedListener(new TextWatcher() {
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
        });*/
        textViewChats.setOnClickListener {
            database.getChats(id, recyclerView)
        }
        textViewAdd.setOnClickListener {
            database.getUsers(recyclerView)
        }

        //textViewCancel.setOnClickListener(v -> editTextSearch.setText(""));
        return view
    }
}