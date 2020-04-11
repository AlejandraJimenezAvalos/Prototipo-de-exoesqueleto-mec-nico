package com.example.exoesqueletov1;

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

import com.example.exoesqueletov1.clases.Authentication;
import com.example.exoesqueletov1.clases.NotificationsItem;
import com.example.exoesqueletov1.clases.NotificationsAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
    private static final String ID = "id";

    NotificationFragment(String typeUser) {
        this.typeUser = typeUser;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
                        mData.add(new NotificationsItem(title, date, description, document.getId()));
                        NotificationsAdapter notificationsAdapter;
                        notificationsAdapter = new NotificationsAdapter(getContext(), mData, NotificationFragment.this);
                        recyclerView.setAdapter(notificationsAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
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

                        mData.add(new NotificationsItem(title, date, description, document.getId()));

                        NotificationsAdapter notificationsAdapter;
                        notificationsAdapter = new NotificationsAdapter(getContext(), mData, NotificationFragment.this);
                        recyclerView.setAdapter(notificationsAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                    }
                }
            });
        }

        return view;
    }

    @Override
    public void onMenuClick(int position) {

    }
}
