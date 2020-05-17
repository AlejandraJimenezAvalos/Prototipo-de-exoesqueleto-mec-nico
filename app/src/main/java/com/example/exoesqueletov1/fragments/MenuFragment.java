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

import com.example.exoesqueletov1.ControlActivity;
import com.example.exoesqueletov1.R;

public class MenuFragment extends Fragment implements View.OnClickListener {

    private String address;

    public MenuFragment(String address) {
        this.address = address;
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

        ControlActivity.setActionButtonReport(getActivity()
                .findViewById(R.id.flotating_button_report_problem));
        ControlActivity.setActionButtonStop(getActivity()
                .findViewById(R.id.flotating_button_stop));
        ControlActivity.setActionButtonPause(getActivity()
                .findViewById(R.id.flotating_button_pause));
        ControlActivity.setActionButtonHelp(getActivity()
                .findViewById(R.id.flotating_button_help));

        ControlActivity.getActionButtonReport().setOnClickListener(this);
        ControlActivity.getActionButtonStop().setOnClickListener(this);
        ControlActivity.getActionButtonPause().setOnClickListener(this);
        ControlActivity.getActionButtonHelp().setOnClickListener(this);

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
                replaceFragment(new WalkFragment(WalkFragment.CODE_WALK_STEPS, address));
                break;
            case R.id.button_walk_time:
                replaceFragment(new WalkFragment(WalkFragment.CODE_WALK_MINUTES, address));
                break;
            case R.id.button_exercise_legs:
                replaceFragment(new UpAndDownFragment(address));
                break;
            case R.id.button_works:
                replaceFragment(new WorksFragment(address));
                break;
            case R.id.flotating_button_report_problem:
            case R.id.flotating_button_help:
            case R.id.flotating_button_stop:
            case R.id.flotating_button_pause:
                Toast.makeText(getContext(), R.string.advertencia_click_flotating_buttons, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void replaceFragment(Fragment fragment) {
        getFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
    }

}
