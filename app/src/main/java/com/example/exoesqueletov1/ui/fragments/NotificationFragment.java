package com.example.exoesqueletov1.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exoesqueletov1.R;
import com.example.exoesqueletov1.clases.Authentication;
import com.example.exoesqueletov1.clases.adapters.NotificationsAdapter;
import com.example.exoesqueletov1.clases.items.NotificationsItem;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.example.exoesqueletov1.ui.ConstantsDatabase.ADMIN;
import static com.example.exoesqueletov1.ui.ConstantsDatabase.CODE;
import static com.example.exoesqueletov1.ui.ConstantsDatabase.CODE_NOTIFICATIONS_ADMIN_REQUEST;
import static com.example.exoesqueletov1.ui.ConstantsDatabase.CODE_NOTIFICATIONS_DELETE_REQUEST;
import static com.example.exoesqueletov1.ui.ConstantsDatabase.CODE_NOTIFICATIONS_DELETE_REQUEST_FOR_INFRACTION;
import static com.example.exoesqueletov1.ui.ConstantsDatabase.CODE_NOTIFICATIONS_FRIEND_REQUEST;
import static com.example.exoesqueletov1.ui.ConstantsDatabase.CODE_NOTIFICATIONS_NEW_USER;
import static com.example.exoesqueletov1.ui.ConstantsDatabase.CODE_NOTIFICATIONS_TO_ACCEPT;
import static com.example.exoesqueletov1.ui.ConstantsDatabase.COLLECTION_NOTIFICATIONS;
import static com.example.exoesqueletov1.ui.ConstantsDatabase.DATE;
import static com.example.exoesqueletov1.ui.ConstantsDatabase.DESCRIPTION;
import static com.example.exoesqueletov1.ui.ConstantsDatabase.NO;
import static com.example.exoesqueletov1.ui.ConstantsDatabase.STATE_NOTIFY;
import static com.example.exoesqueletov1.ui.ConstantsDatabase.TITLE;
import static com.example.exoesqueletov1.ui.ConstantsDatabase.TO;

public class NotificationFragment extends Fragment implements NotificationsAdapter.OnMenuListener {

    private RecyclerView recyclerView;
    private List<NotificationsItem> mData;
    private String typeUser;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    public NotificationFragment(String typeUser) {
        this.typeUser = typeUser;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        final String idUser = new Authentication().getCurrentUser().getEmail();

        recyclerView = view.findViewById(R.id.recycler_pending);
        mData = new ArrayList<>();


        if (typeUser.equals(ADMIN)) {
            db.collection(COLLECTION_NOTIFICATIONS).
                    orderBy(NO, Query.Direction.DESCENDING).get().
                    addOnCompleteListener(task -> {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            boolean state;
                            String title = document.get(TITLE).toString();
                            String date = document.get(DATE).toString();
                            String description = document.get(DESCRIPTION).toString();
                            int code = Integer.parseInt(document.get(CODE).toString());
                            state = Boolean.parseBoolean(document.get(STATE_NOTIFY)
                                    .toString());

                            if (document.get(TO).toString().equals("a")){
                                mData.add(new NotificationsItem(title, date, description,
                                        document.getId(), code, state));
                            }
                        }
                        NotificationsAdapter notificationsAdapter;
                        notificationsAdapter = new NotificationsAdapter(getContext(), mData,
                                NotificationFragment.this);
                        recyclerView.setAdapter(notificationsAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                                LinearLayoutManager.VERTICAL, false));
                    });
        }
        else {
            db.collection(COLLECTION_NOTIFICATIONS).
                    orderBy(NO, Query.Direction.DESCENDING).get().
                    addOnCompleteListener(task -> {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            boolean state;
                            String title = document.get(TITLE).toString();
                            String date = document.get(DATE).toString();
                            String description = document.get(DESCRIPTION).toString();
                            int code = Integer.parseInt(document.get(CODE).toString());
                            state = Boolean.parseBoolean(document.get(STATE_NOTIFY)
                                    .toString());

                            if (document.get(TO).toString().equals(idUser)){
                                mData.add(new NotificationsItem(title, date, description,
                                        document.getId(), code, state));
                            }
                        }
                        NotificationsAdapter notificationsAdapter;
                        notificationsAdapter = new NotificationsAdapter(getContext(), mData,
                                NotificationFragment.this);
                        recyclerView.setAdapter(notificationsAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                                LinearLayoutManager.VERTICAL, false));
                    });
        }

        return view;
    }

    @Override
    public void onMenuClick(int position) {
        final String idNotification = mData.get(position).getId();
        int codeNotification = mData.get(position).getCode();
        Fragment fragment;
        db.collection(COLLECTION_NOTIFICATIONS).
                document(idNotification).get().
                addOnSuccessListener(documentSnapshot -> {
                    Map<String, Object> data = documentSnapshot.getData();
                    data.remove(STATE_NOTIFY);
                    data.put(STATE_NOTIFY, true);
                    FirebaseFirestore.getInstance().collection(COLLECTION_NOTIFICATIONS).
                            document(idNotification).update(data);
                });

        switch (codeNotification) {
            case CODE_NOTIFICATIONS_FRIEND_REQUEST:
                fragment = new MessageFragment(typeUser, CODE_NOTIFICATIONS_FRIEND_REQUEST);
                getActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.container_main, fragment).commit();
                break;
            case CODE_NOTIFICATIONS_ADMIN_REQUEST:
            case CODE_NOTIFICATIONS_TO_ACCEPT:
                fragment = new MessageFragment(typeUser, CODE_NOTIFICATIONS_ADMIN_REQUEST);
                getActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.container_main, fragment).commit();
                break;
            case CODE_NOTIFICATIONS_NEW_USER:
            case CODE_NOTIFICATIONS_DELETE_REQUEST_FOR_INFRACTION:
            case CODE_NOTIFICATIONS_DELETE_REQUEST:
                Toast.makeText(getContext(), "Estamos trabajando en ello", Toast.LENGTH_SHORT).show();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + codeNotification);
        }
    }
}
