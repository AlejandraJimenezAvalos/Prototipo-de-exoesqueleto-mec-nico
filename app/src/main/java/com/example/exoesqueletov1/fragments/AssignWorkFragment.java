package com.example.exoesqueletov1.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exoesqueletov1.ConstantsDatabase;
import com.example.exoesqueletov1.R;
import com.example.exoesqueletov1.clases.Authentication;
import com.example.exoesqueletov1.clases.adapters.ChatAdapter;
import com.example.exoesqueletov1.clases.adapters.ChatAdapter.OnMenuListener;
import com.example.exoesqueletov1.clases.adapters.WorkAdapter;
import com.example.exoesqueletov1.clases.adapters.WorkAdapter.OnWorkListener;
import com.example.exoesqueletov1.clases.bleutooth.Constants;
import com.example.exoesqueletov1.clases.items.ChatItem;
import com.example.exoesqueletov1.clases.items.WorkItem;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static com.example.exoesqueletov1.ConstantsDatabase.ACTION;
import static com.example.exoesqueletov1.ConstantsDatabase.COLLECTION_CHATS;
import static com.example.exoesqueletov1.ConstantsDatabase.COLLECTION_USERS;
import static com.example.exoesqueletov1.ConstantsDatabase.DATE;
import static com.example.exoesqueletov1.ConstantsDatabase.DOCUMENT_USER;
import static com.example.exoesqueletov1.ConstantsDatabase.GENDER;
import static com.example.exoesqueletov1.ConstantsDatabase.ID_EXOESQUELETO;
import static com.example.exoesqueletov1.ConstantsDatabase.ID_PATIENT;
import static com.example.exoesqueletov1.ConstantsDatabase.ID_SPECIALIST;
import static com.example.exoesqueletov1.ConstantsDatabase.LAST_NAME;
import static com.example.exoesqueletov1.ConstantsDatabase.NAME;
import static com.example.exoesqueletov1.ConstantsDatabase.NO;
import static com.example.exoesqueletov1.ConstantsDatabase.NUMBER;
import static com.example.exoesqueletov1.ConstantsDatabase.STATE;

public class AssignWorkFragment extends Fragment implements OnMenuListener, OnWorkListener {

    private RecyclerView recyclerView;
    private RecyclerView recyclerView1;
    private RecyclerView recyclerView2;
    private TextView textName;
    private TextView textDate;
    private TextView textGender;
    private TextView idExoesqueleto;
    private Button buttonWalkSteps;
    private Button buttonWalkMinutes;
    private Button buttonUpRight;
    private Button buttonUpLeft;
    private NumberPicker numberPicker;

    private FirebaseFirestore db;
    private String id;
    private List<ChatItem> userItems;
    private List<WorkItem> workItemsTrue;
    private List<WorkItem> workItemsFalse;
    private WorkAdapter workAdapterTrue;
    private WorkAdapter workAdapterFalse;
    private ChatAdapter chatAdapter;

    public AssignWorkFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_assign_work, container, false);
        this.recyclerView = view.findViewById(R.id.recyclerView);
        this.recyclerView1 = view.findViewById(R.id.recyclerView1);
        this.recyclerView2 = view.findViewById(R.id.recyclerView2);
        this.textGender = view.findViewById(R.id.text_gender);
        this.textDate = view.findViewById(R.id.text_date);
        this.textName = view.findViewById(R.id.text_name);
        this.idExoesqueleto = view.findViewById(R.id.id_exoesqueleto);
        this.buttonWalkMinutes = view.findViewById(R.id.button_walk_minutes);
        this.buttonWalkSteps = view.findViewById(R.id.button_walk_steps);
        this.buttonUpRight = view.findViewById(R.id.button_up_right);
        this.buttonUpLeft = view.findViewById(R.id.button_up_left);
        this.numberPicker = view.findViewById(R.id.number_picker);
        EditText textSearchFinalise = view.findViewById(R.id.text_search_finalise);
        EditText textSearchPending = view.findViewById(R.id.text_search_pending);

        textSearchPending.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                workAdapterFalse.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        textSearchFinalise.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                workAdapterTrue.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        db = FirebaseFirestore.getInstance();
        id = new Authentication().getCurrentUser().getEmail();

        buttonWalkSteps.setEnabled(false);
        buttonUpRight.setEnabled(false);
        buttonWalkMinutes.setEnabled(false);
        buttonUpLeft.setEnabled(false);
        buttonWalkMinutes.setOnClickListener(v -> setData(Constants.WALK_MINUTES));
        buttonWalkSteps.setOnClickListener(v -> setData(Constants.WALK_STEPS));
        buttonUpRight.setOnClickListener(v -> setData(Constants.UP_RIGHT));
        buttonUpLeft.setOnClickListener(v -> setData(Constants.UP_LEFT));

        numberPicker.setMinValue(2);
        numberPicker.setMaxValue(9);

        getUsers();

        return view;
    }

    private void getFinaliseWork() {
        try {
            String idCollection = idExoesqueleto.getText().toString();
            Query query = db.collection(idCollection).orderBy(NO, Query.Direction.DESCENDING);
            workItemsTrue = new ArrayList<>();
            query.get().addOnCompleteListener(task -> {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Map<String, Object> data = document.getData();
                    if (Boolean.parseBoolean(String.valueOf(data.get(STATE)))) {
                        String action = data.get(ACTION).toString();
                        String date = data.get(DATE).toString();
                        int number = Integer.parseInt(data.get(NUMBER).toString());
                        boolean state = Boolean.parseBoolean(data.get(STATE).toString());
                        workItemsTrue
                                .add(new WorkItem(action, date, number, state, document.getId()));
                    }
                }
                workAdapterTrue = new WorkAdapter(getContext(), workItemsTrue, this);
                recyclerView1.setAdapter(workAdapterTrue);
                recyclerView1.setLayoutManager(new LinearLayoutManager(getContext(),
                        LinearLayoutManager.HORIZONTAL, false));
            });
        } catch (Exception e) {
            Toast.makeText(getContext(), "e: " + e, Toast.LENGTH_SHORT).show();
        }
    }

    private void getPendingWork() {
        try {
            String idCollection = idExoesqueleto.getText().toString();
            Query query = db.collection(idCollection).orderBy(NO, Query.Direction.DESCENDING);
            workItemsFalse = new ArrayList<>();
            query.get().addOnCompleteListener(task -> {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Map<String, Object> data = document.getData();
                    if (!Boolean.parseBoolean(String.valueOf(data.get(STATE)))) {
                        String action = data.get(ACTION).toString();
                        String date = data.get(DATE).toString();
                        int number = Integer.parseInt(data.get(NUMBER).toString());
                        boolean state = Boolean.parseBoolean(data.get(STATE).toString());
                        workItemsFalse
                                .add(new WorkItem(action, date, number, state, document.getId()));
                    }
                }
                workAdapterFalse =
                        new WorkAdapter(getContext(), workItemsFalse, this);
                recyclerView2.setAdapter(workAdapterFalse);
                recyclerView2.setLayoutManager(new LinearLayoutManager(getContext(),
                        LinearLayoutManager.HORIZONTAL, false));
            });
        } catch (Exception e) {
            Toast.makeText(getContext(), "e: " + e, Toast.LENGTH_SHORT).show();
        }

    }

    private void getUsers() {
        Query query = db.collection(COLLECTION_CHATS).whereEqualTo(ID_SPECIALIST, id);
        query.get().addOnCompleteListener(task -> {
            userItems = new ArrayList<>();
            for (QueryDocumentSnapshot document : task.getResult()) {
                Map<String, Object> data = document.getData();
                userItems.add(new ChatItem(data.get(ID_PATIENT).toString(),
                        "", "", "", ""));
            }
            chatAdapter = new ChatAdapter(getContext(), userItems,
                    AssignWorkFragment.this);
            recyclerView.setAdapter(chatAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                    LinearLayoutManager.VERTICAL, false));
        });
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onMenuClick(int position) {
        try {
            String idUser = userItems.get(position).getId();
            DocumentReference documentReference = db.collection(COLLECTION_USERS).document(idUser);
            AtomicReference<String> gender = new AtomicReference<>(getString(R.string.masculino));
            db.collection(idUser).document(DOCUMENT_USER).get().
                    addOnSuccessListener(documentSnapshot -> {
                        Map<String, Object> data = documentSnapshot.getData();
                        if (Boolean.parseBoolean(data.get(GENDER).toString())) {
                            gender.set(getString(R.string.femenino));
                        }
                        textName.setText(data.get(NAME)
                                .toString() + " " + data.get(LAST_NAME).toString());
                        textDate.setText(data.get(DATE).toString());
                        textGender.setText(gender.get());
                    });
            documentReference.get().addOnSuccessListener(documentSnapshot -> {
                idExoesqueleto.setText(documentSnapshot.getData().get(ID_EXOESQUELETO).toString());
                idExoesqueleto.setVisibility(View.INVISIBLE);
                getPendingWork();
                getFinaliseWork();
            });
            buttonUpLeft.setEnabled(true);
            buttonUpRight.setEnabled(true);
            buttonWalkMinutes.setEnabled(true);
            buttonWalkSteps.setEnabled(true);
        } catch (Exception e) {
            Toast.makeText(getContext(), "e: " + e, Toast.LENGTH_SHORT).show();
        }
    }

    private void setData(String constant) {
        db.collection(idExoesqueleto.getText().toString()).get().addOnCompleteListener(task -> {
            Map <String, Object> data = new HashMap<>();
            data.put(ACTION, constant);
            data.put(NUMBER, numberPicker.getValue());
            data.put(ConstantsDatabase.STATE, false);
            data.put(NO, task.getResult().size());
            data.put(ConstantsDatabase.DATE, DateFormat.format("MMMM d, yyyy, HH:mm:ss",
                    new Date().getTime()));
            db.collection(idExoesqueleto.getText().toString()).add(data);
            getPendingWork();
        });
    }

    @Override
    public void onWorkClick(int position) {
        String idDocument = workItemsFalse.get(position).getId();
        db.collection(idExoesqueleto.getText().toString()).document(idDocument).delete();
        getPendingWork();
    }
}
