package com.example.exoesqueletov1.fragments;

import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.exoesqueletov1.R;

import java.io.IOException;

public class WalkFragment extends Fragment {

    private int code;
    private BluetoothSocket bluetoothSocket;

    public WalkFragment(int code, BluetoothSocket bluetoothSocket) {
        this.code = code;
        this.bluetoothSocket = bluetoothSocket;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_walk, container, false);

        Button buttonWalkfragment = view.findViewById(R.id.button_walk_fragment);
        NumberPicker numberPicker = view.findViewById(R.id.number_picker);
        TextView indicationWalk = view.findViewById(R.id.indication_walk);
        TextView titleWalk = view.findViewById(R.id.title_walk);

        buttonWalkfragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                if (bluetoothSocket !=null) {
                    try {
                        bluetoothSocket.getOutputStream().write("TF".getBytes());
                        Toast.makeText(getContext(), "Te mereces un cafe <3", Toast.LENGTH_SHORT).show();
                    }
                    catch (IOException e) {
                        Toast.makeText(getContext(), "e: " + e, Toast.LENGTH_SHORT).show();
                    }
                }
                 */
            }
        });

        return view;
    }

}
