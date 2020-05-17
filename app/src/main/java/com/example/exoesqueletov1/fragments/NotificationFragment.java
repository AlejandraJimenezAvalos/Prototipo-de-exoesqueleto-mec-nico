package com.example.exoesqueletov1.fragments;

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

import com.example.exoesqueletov1.ConstantsDatabase;
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


        if (typeUser.equals("a")) {
            db.collection(ConstantsDatabase.COLLECTION_NOTIFICATIONS).
                    orderBy(ConstantsDatabase.DATE, Query.Direction.DESCENDING).get().
                    addOnCompleteListener(task -> {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            boolean state;
                            String title = document.get(ConstantsDatabase.TITLE).toString();
                            String date = document.get(ConstantsDatabase.DATE).toString();
                            String description = document.get(ConstantsDatabase.DESCRIPTION).toString();
                            int code = Integer.parseInt(document.get(ConstantsDatabase.CODE).toString());
                            state = Boolean.parseBoolean(document.get(ConstantsDatabase.STATE_NOTIFY)
                                    .toString());

                            if (document.get(ConstantsDatabase.TO).toString().equals("a")){
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
            db.collection(ConstantsDatabase.COLLECTION_NOTIFICATIONS).
                    orderBy(ConstantsDatabase.DATE, Query.Direction.DESCENDING).get().
                    addOnCompleteListener(task -> {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            boolean state;
                            String title = document.get(ConstantsDatabase.TITLE).toString();
                            String date = document.get(ConstantsDatabase.DATE).toString();
                            String description = document.get(ConstantsDatabase.DESCRIPTION).toString();
                            int code = Integer.parseInt(document.get(ConstantsDatabase.CODE).toString());
                            state = Boolean.parseBoolean(document.get(ConstantsDatabase.STATE_NOTIFY)
                                    .toString());

                            if (document.get(ConstantsDatabase.TO).toString().equals(idUser)){
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
        db.collection(ConstantsDatabase.COLLECTION_NOTIFICATIONS).
                document(idNotification).get().
                addOnSuccessListener(documentSnapshot -> {
                    Map<String, Object> data = documentSnapshot.getData();
                    data.remove(ConstantsDatabase.STATE_NOTIFY);
                    data.put(ConstantsDatabase.STATE_NOTIFY, true);
                    FirebaseFirestore.getInstance().collection(ConstantsDatabase.COLLECTION_NOTIFICATIONS).
                            document(idNotification).update(data);
                });

        switch (codeNotification) {
            case ConstantsDatabase.CODE_NOTIFICATIONS_FRIEND_REQUEST:
                fragment = new MessageFragment(typeUser, ConstantsDatabase.CODE_NOTIFICATIONS_FRIEND_REQUEST);
                getActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.container_main, fragment).commit();
                break;
            case ConstantsDatabase.CODE_NOTIFICATIONS_ADMIN_REQUEST:
            case ConstantsDatabase.CODE_NOTIFICATIONS_TO_ACCEPT:
                fragment = new MessageFragment(typeUser, ConstantsDatabase.CODE_NOTIFICATIONS_ADMIN_REQUEST);
                getActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.container_main, fragment).commit();
                break;
            case ConstantsDatabase.CODE_NOTIFICATIONS_NEW_USER:
            case ConstantsDatabase.CODE_NOTIFICATIONS_DELETE_REQUEST_FOR_INFRACTION:
            case ConstantsDatabase.CODE_NOTIFICATIONS_DELETE_REQUEST:
                Toast.makeText(getContext(), "Estamos trabajando en ello", Toast.LENGTH_SHORT).show();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + codeNotification);
        }
    }
}
