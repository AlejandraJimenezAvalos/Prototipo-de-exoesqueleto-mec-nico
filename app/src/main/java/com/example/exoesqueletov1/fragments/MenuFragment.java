package com.example.exoesqueletov1.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.exoesqueletov1.ConstantsDatabase;
import com.example.exoesqueletov1.R;

import static com.example.exoesqueletov1.ControlActivity.getActionButtonHelp;
import static com.example.exoesqueletov1.ControlActivity.getActionButtonPause;
import static com.example.exoesqueletov1.ControlActivity.getActionButtonReport;
import static com.example.exoesqueletov1.ControlActivity.getActionButtonStop;
import static com.example.exoesqueletov1.ControlActivity.setActionButtonHelp;
import static com.example.exoesqueletov1.ControlActivity.setActionButtonPause;
import static com.example.exoesqueletov1.ControlActivity.setActionButtonReport;
import static com.example.exoesqueletov1.ControlActivity.setActionButtonStop;
import static com.example.exoesqueletov1.fragments.UpAndDownFragment.CODE_REGULAR;

public class MenuFragment extends Fragment implements View.OnClickListener {

    private String address;
    private String typeUser;

    public MenuFragment(String address, String typeUser) {
        this.address = address;
        this.typeUser = typeUser;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        Button buttonWorks = view.findViewById(R.id.button_works);
        Button buttonWalktime = view.findViewById(R.id.button_walk_time);
        Button buttonWalksteps = view.findViewById(R.id.button_walk_steps);
        Button buttonExercise = view.findViewById(R.id.button_exercise_legs);
        ImageView imageBack = view.findViewById(R.id.image_back);

        setActionButtonReport(getActivity().findViewById(R.id.flotating_button_report_problem));
        setActionButtonStop(getActivity().findViewById(R.id.flotating_button_stop));
        setActionButtonPause(getActivity().findViewById(R.id.flotating_button_pause));
        setActionButtonHelp(getActivity().findViewById(R.id.flotating_button_help));

        getActionButtonReport().setEnabled(false);
        getActionButtonStop().setEnabled(false);
        getActionButtonPause().setEnabled(false);
        getActionButtonHelp().setEnabled(false);

        buttonWorks.setOnClickListener(this);
        buttonWalktime.setOnClickListener(this);
        buttonWalksteps.setOnClickListener(this);
        buttonExercise.setOnClickListener(this);
        imageBack.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_back:
                getActivity().finish();
            case R.id.button_walk_steps:
                replaceFragment(new WalkFragment(WalkFragment.CODE_WALK_STEPS, address, typeUser));
                break;
            case R.id.button_walk_time:
                replaceFragment(new WalkFragment(WalkFragment.CODE_WALK_MINUTES, address, typeUser));
                break;
            case R.id.button_exercise_legs:
                replaceFragment(new UpAndDownFragment(address, CODE_REGULAR, typeUser));
                break;
            case R.id.button_works:
                if (!typeUser.equals(ConstantsDatabase.ADMIN)) {
                    replaceFragment(new WorksFragment(address, typeUser));
                } else {
                    Toast.makeText(getContext(), R.string.no_tiene_acceso, Toast.LENGTH_SHORT)
                            .show();
                }
                break;
        }
    }

    private void replaceFragment(Fragment fragment) {
        getFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
    }

}
