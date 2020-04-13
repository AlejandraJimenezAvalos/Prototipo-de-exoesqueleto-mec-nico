package com.example.exoesqueletov1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exoesqueletov1.clases.Authentication;
import com.example.exoesqueletov1.clases.NotificationsAdapter;
import com.example.exoesqueletov1.clases.NotificationsItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NotificationFragment extends Fragment implements NotificationsAdapter.OnMenuListener {

    private RecyclerView recyclerView;
    private List<NotificationsItem> mData;
    private String typeUser;

    private static final String COLLECTION_NOTIFICATIONS = "notifications";
    private static final String TO = "to";
    private static final String ADMIN = "a";
    private static final String TITLE = "title";
    private static final String DATE = "date";
    private static final String DESCRIPTION = "description";
    private static final String STATE_NOTIFY = "stateNotify";
    private static final String CODE = "code";

    private static final int CODE_FRIEND_REQUEST = 0;
    private static final int CODE_ADMIN_REQUEST = 1;
    private static final int CODE_NEW_USER = 2;


    NotificationFragment(String typeUser) {
        this.typeUser = typeUser;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        String idUser = new Authentication().getCurrentUser().getEmail();

        recyclerView = view.findViewById(R.id.recycler_pending);
        mData = new ArrayList<>();


        if (typeUser.equals("a")) {
            FirebaseFirestore.getInstance().collection(COLLECTION_NOTIFICATIONS).
                    whereEqualTo(TO, ADMIN).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String title = document.get(TITLE).toString();
                        String date = document.get(DATE).toString();
                        String description = document.get(DESCRIPTION).toString();
                        int code = Integer.parseInt(document.get(CODE).toString());
                        boolean state = Boolean.parseBoolean(document.get(STATE_NOTIFY).toString());

                        mData.add(new NotificationsItem(title, date, description, document.getId(),
                                code, state));

                        NotificationsAdapter notificationsAdapter;
                        notificationsAdapter = new NotificationsAdapter(getContext(), mData,
                                NotificationFragment.this);
                        recyclerView.setAdapter(notificationsAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                                LinearLayoutManager.VERTICAL, false));
                    }
                }
            });
        }
        else {
            FirebaseFirestore.getInstance().collection(COLLECTION_NOTIFICATIONS).
                    whereEqualTo(TO, idUser).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String title = document.get(TITLE).toString();
                        String date = document.get(DATE).toString();
                        String description = document.get(DESCRIPTION).toString();
                        int code = Integer.parseInt(document.get(CODE).toString());
                        boolean state = Boolean.parseBoolean(document.get(STATE_NOTIFY).toString());

                        mData.add(new NotificationsItem(title, date, description, document.getId(),
                                code, state));

                        NotificationsAdapter notificationsAdapter;
                        notificationsAdapter = new NotificationsAdapter(getContext(), mData,
                                NotificationFragment.this);
                        recyclerView.setAdapter(notificationsAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                                LinearLayoutManager.VERTICAL, false));
                    }
                }
            });
        }

        return view;
    }

    @Override
    public void onMenuClick(int position) {
        final String idNotification = mData.get(position).getId();
        int codeNotification = mData.get(position).getCode();
        Fragment fragment;

        FirebaseFirestore.getInstance().collection(COLLECTION_NOTIFICATIONS).
                document(idNotification).get().
                addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Map<String, Object> data = documentSnapshot.getData();
                data.remove(STATE_NOTIFY);
                data.put(STATE_NOTIFY, true);
                FirebaseFirestore.getInstance().collection(COLLECTION_NOTIFICATIONS).
                        document(idNotification).update(data);
            }
        });

        switch (codeNotification) {
            case CODE_FRIEND_REQUEST:
                fragment = new MessageFragment(typeUser, CODE_FRIEND_REQUEST);
                getActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.container_main, fragment).commit();
                break;
            case CODE_ADMIN_REQUEST:
                fragment = new MessageFragment(typeUser, CODE_ADMIN_REQUEST);
                getActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.container_main, fragment).commit();
                break;
            case CODE_NEW_USER:
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + codeNotification);
        }
    }
}
