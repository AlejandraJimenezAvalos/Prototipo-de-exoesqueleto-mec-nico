package com.example.exoesqueletov1.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.exoesqueletov1.ControlActivity;
import com.example.exoesqueletov1.R;

public class WorksFragment extends Fragment implements View.OnClickListener {

    private View view;

    private String address;

    WorksFragment(String address) {
        this.address = address;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_works, container, false);
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
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.flotating_button_report_problem:
            case R.id.flotating_button_help:
            case R.id.flotating_button_stop:
            case R.id.flotating_button_pause:
                Toast.makeText(getContext(), R.string.advertencia_click_flotating_buttons, Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
