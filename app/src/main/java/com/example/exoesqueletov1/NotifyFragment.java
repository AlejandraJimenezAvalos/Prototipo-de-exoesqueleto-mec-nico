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

import com.example.exoesqueletov1.clases.NotifyItem;
import com.example.exoesqueletov1.clases.MenuAdapter;
import com.example.exoesqueletov1.clases.NotifyAdapter;

import java.util.ArrayList;
import java.util.List;

public class NotifyFragment extends Fragment implements NotifyAdapter.OnMenuListener {

    private RecyclerView recyclerView;
    private List<NotifyItem> mData;

    public NotifyFragment() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notify, container, false);

        recyclerView = view.findViewById(R.id.recycler_pending);
        mData = new ArrayList<>();
        mData.add(new NotifyItem("Hola", "17/01/2020", "ya no se que más poner"));
        mData.add(new NotifyItem("Hola", "17/01/2020", "ya no se que más poner"));
        mData.add(new NotifyItem("Hola", "17/01/2020", "ya no se que más poner"));
        mData.add(new NotifyItem("Hola", "17/01/2020", "ya no se que más poner"));
        mData.add(new NotifyItem("Hola", "17/01/2020", "ya no se que más poner"));
        mData.add(new NotifyItem("Hola", "17/01/2020", "ya no se que más poner"));
        mData.add(new NotifyItem("Hola", "17/01/2020", "ya no se que más poner"));
        mData.add(new NotifyItem("Hola", "17/01/2020", "ya no se que más poner"));
        mData.add(new NotifyItem("Hola", "17/01/2020", "ya no se que más poner"));
        mData.add(new NotifyItem("Hola", "17/01/2020", "ya no se que más poner"));
        mData.add(new NotifyItem("Hola", "17/01/2020", "ya no se que más poner"));
        mData.add(new NotifyItem("Hola", "17/01/2020", "ya no se que más poner"));
        mData.add(new NotifyItem("Hola", "17/01/2020", "ya no se que más poner"));
        mData.add(new NotifyItem("Hola", "17/01/2020", "ya no se que más poner"));
        mData.add(new NotifyItem("Hola", "17/01/2020", "ya no se que más poner"));
        mData.add(new NotifyItem("Hola", "17/01/2020", "ya no se que más poner"));
        mData.add(new NotifyItem("Hola", "17/01/2020", "ya no se que más poner"));
        mData.add(new NotifyItem("Hola", "17/01/2020", "ya no se que más poner"));

        NotifyAdapter notifyAdapter = new NotifyAdapter(getContext(), mData, this);

        recyclerView.setAdapter(notifyAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        return view;
    }

    @Override
    public void onMenuClick(int position) {

    }
}
