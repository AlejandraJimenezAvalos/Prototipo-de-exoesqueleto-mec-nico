package com.example.exoesqueletov1.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.exoesqueletov1.R;

public class WelcomeFragment extends Fragment {

    private View view;

    public WelcomeFragment() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_welcome, container, false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        TextView t1 = view.findViewById(R.id.textHola);
        TextView t2 = view.findViewById(R.id.textVamos);
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.transition);
        t1.setAnimation(animation);
        t2.setAnimation(animation);
    }
}
