package com.example.exoesqueletov1.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exoesqueletov1.R;
import com.example.exoesqueletov1.clases.Authentication;
import com.example.exoesqueletov1.clases.adapters.WorkAdapter;
import com.example.exoesqueletov1.clases.bleutooth.Constants;
import com.example.exoesqueletov1.clases.items.WorkItem;
import com.example.exoesqueletov1.ui.dialogs.DialogInfo;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.example.exoesqueletov1.ui.ConstantsDatabase.ACTION;
import static com.example.exoesqueletov1.ui.ConstantsDatabase.COLLECTION_USERS;
import static com.example.exoesqueletov1.ui.ConstantsDatabase.DATE;
import static com.example.exoesqueletov1.ui.ConstantsDatabase.ID_EXOESQUELETO;
import static com.example.exoesqueletov1.ui.ConstantsDatabase.NO;
import static com.example.exoesqueletov1.ui.ConstantsDatabase.NUMBER;
import static com.example.exoesqueletov1.ui.ConstantsDatabase.STATE;
import static com.example.exoesqueletov1.ui.ControlActivity.getActionButtonHelp;
import static com.example.exoesqueletov1.ui.ControlActivity.getActionButtonPause;
import static com.example.exoesqueletov1.ui.ControlActivity.getActionButtonReport;
import static com.example.exoesqueletov1.ui.ControlActivity.getActionButtonStop;
import static com.example.exoesqueletov1.ui.ControlActivity.setActionButtonHelp;
import static com.example.exoesqueletov1.ui.ControlActivity.setActionButtonPause;
import static com.example.exoesqueletov1.ui.ControlActivity.setActionButtonReport;
import static com.example.exoesqueletov1.ui.ControlActivity.setActionButtonStop;
import static com.example.exoesqueletov1.ui.fragments.UpAndDownFragment.CODE_NO_REGULAR;
import static com.example.exoesqueletov1.ui.fragments.WalkFragment.CODE_WALK_MINUTES;
import static com.example.exoesqueletov1.ui.fragments.WalkFragment.CODE_WALK_STEPS;

public class WorksFragment extends Fragment implements WorkAdapter.OnWorkListener{

    private String address;
    private String typeUser;

    private WorkAdapter workAdapter;
    private RecyclerView recyclerView;
    private List<WorkItem> workItems;
    private FirebaseFirestore db;

    WorksFragment(String address, String typeUser) {
        this.address = address;
        this.typeUser = typeUser;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_works, container, false);
        setActionButtonReport(getActivity().findViewById(R.id.flotating_button_report_problem));
        setActionButtonStop(getActivity().findViewById(R.id.flotating_button_stop));
        setActionButtonPause(getActivity().findViewById(R.id.flotating_button_pause));
        setActionButtonHelp(getActivity().findViewById(R.id.flotating_button_help));

        db = FirebaseFirestore.getInstance();
        recyclerView = view.findViewById(R.id.recyclerView_pending);
        ImageView imageViewBack = view.findViewById(R.id.button_back);

        imageViewBack.setOnClickListener(v -> getFragmentManager().beginTransaction()
                .replace(R.id.content, new MenuFragment(address, typeUser)).commit()
        );

        db.collection(COLLECTION_USERS).document(new Authentication().getCurrentUser().getEmail())
                .get().addOnCompleteListener(task ->
                getPendingWork(task.getResult().getData().get(ID_EXOESQUELETO).toString())
        );

        getActionButtonReport().setEnabled(false);
        getActionButtonStop().setEnabled(false);
        getActionButtonPause().setEnabled(false);
        getActionButtonHelp().setEnabled(true);
        getActionButtonHelp().setOnClickListener(v -> {
            DialogInfo dialogInfo = new DialogInfo(R.mipmap.ss_pendings);
            dialogInfo.show(getFragmentManager(), "");
        });
        return view;
    }

    private void getPendingWork(String idCollection) {
        Query query = db.collection(idCollection).orderBy(NO, Query.Direction.DESCENDING);
        workItems = new ArrayList<>();
        query.get().addOnCompleteListener(task -> {
            for (QueryDocumentSnapshot document : task.getResult()) {
                Map<String, Object> data = document.getData();
                if (!Boolean.parseBoolean(String.valueOf(data.get(STATE)))) {
                    String action = data.get(ACTION).toString();
                    String date = data.get(DATE).toString();
                    int number = Integer.parseInt(data.get(NUMBER).toString());
                    boolean state = Boolean.parseBoolean(data.get(STATE).toString());
                    workItems.add(new WorkItem(action, date, number, state, document.getId()));
                }
            }
            workAdapter = new WorkAdapter(getContext(), workItems, this, true);
            recyclerView.setAdapter(workAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                    LinearLayoutManager.VERTICAL, false));
        });
    }

    @Override
    public void onWorkClick(int position) {
        Fragment fragment = null;
        int n = workItems.get(position).getNumber();
        String action = workItems.get(position).getAction();
        String idDocument = workItems.get(position).getId();
        switch (action) {
            case Constants.WALK_MINUTES:
                fragment = new WalkFragment(CODE_WALK_MINUTES, address, idDocument, n, typeUser);
                break;
            case Constants.WALK_STEPS:
                fragment = new WalkFragment(CODE_WALK_STEPS, address, idDocument, n, typeUser);
                break;
            case Constants.UP_LEFT:
            case Constants.UP_RIGHT:
                fragment = new UpAndDownFragment(address, CODE_NO_REGULAR, n, idDocument, action
                        , typeUser);
                break;
        }
        getFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
    }
}
