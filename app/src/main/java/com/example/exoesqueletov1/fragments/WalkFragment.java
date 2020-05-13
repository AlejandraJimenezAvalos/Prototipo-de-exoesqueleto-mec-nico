package com.example.exoesqueletov1.fragments;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
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
import com.example.exoesqueletov1.bleutooth.SerialListener;
import com.example.exoesqueletov1.bleutooth.SerialService;
import com.example.exoesqueletov1.bleutooth.SerialSocket;
import com.example.exoesqueletov1.dialogs.DialogOops;

public class WalkFragment extends Fragment implements ServiceConnection, SerialListener {

    public static final int CODE_MINUTES = 0;
    public static final int CODE_WALK = 1;

    private TextView indicationWalk;
    private TextView titleWalk;
    private int code;

    private enum Connected { False, Pending, True } // type of variable

    private String deviceAddress;
    private String newline = "\r\n";

    private SerialSocket serialSocket;
    private SerialService serialService;
    private boolean initialStart = true;
    private Connected connected = Connected.False;

    public WalkFragment(int code) {
        this.code = code;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_walk, container, false);

        Button buttonWalkfragment = view.findViewById(R.id.button_walk_fragment);
        NumberPicker numberPicker = view.findViewById(R.id.number_picker);
        indicationWalk = view.findViewById(R.id.indication_walk);
        titleWalk = view.findViewById(R.id.title_walk);

        numberPicker.setMaxValue(30);
        numberPicker.setMinValue(1);

        if (code == CODE_WALK) { initWalk(); }

        // TODO

        buttonWalkfragment.setOnClickListener(v -> {
            if(code == CODE_MINUTES) { send("a"); }
            else { send("b"); }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        if (connected != Connected.False) {
            disconnect();
        }
        getActivity().stopService(new Intent(getActivity(), SerialService.class));
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
        if(serialService != null) {
            serialService.attach(this);
        }
        else {
            getActivity().startService(new Intent(getActivity(), SerialService.class));
        }
    }

    @Override
    public void onStop() {
        if(serialService != null && !getActivity().isChangingConfigurations())
            serialService.detach();
        super.onStop();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        getActivity().bindService(new Intent(getActivity(), SerialService.class),
                this, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onDetach() {
        try { getActivity().unbindService(this); } catch(Exception ignored) {}
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(initialStart && serialService !=null) {
            initialStart = false;
            getActivity().runOnUiThread(this::connect);
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        serialService = ((SerialService.SerialBinder) service).getService();
        if(initialStart && isResumed()) {
            initialStart = false;
            getActivity().runOnUiThread(this::connect);
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        serialService = null;
    }

    @Override
    public void onSerialConnect() {
        status("connected");
        connected = Connected.True;
    }

    @Override
    public void onSerialConnectError(Exception e) {
        DialogOops dialogOops = new DialogOops(e.getMessage());
        dialogOops.show(getFragmentManager(), "WalkFragment");
        disconnect();
    }

    @Override
    public void onSerialRead(byte[] data) {
        Toast.makeText(getContext(), "Recivido: " + new String(data), Toast.LENGTH_SHORT).show();
        switch (new String(data)) {
        }
    }

    @Override
    public void onSerialIoError(Exception e) {
        DialogOops dialogOops = new DialogOops(e.getMessage());
        dialogOops.show(getFragmentManager(), "WalkFragment");
        disconnect();
    }

    private void initWalk() {
        titleWalk.setText(R.string.walk_title2);
        indicationWalk.setText(R.string.walk_indication2);
    }

    private void connect() {
        try {
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            BluetoothDevice device = bluetoothAdapter.getRemoteDevice(deviceAddress);
            String deviceName = device.getName() != null ? device.getName() : device.getAddress();
            status("connecting...");
            connected = Connected.Pending;
            serialSocket = new SerialSocket();
            serialService.connect(this, "Connected to " + deviceName);
            serialSocket.connect(getContext(), serialService, device);
        } catch (Exception e) {
            onSerialConnectError(e);
        }
    }

    private void disconnect() {
        connected = Connected.False;
        serialService.disconnect();
        serialSocket.disconnect();
        serialSocket = null;
    }

    private void send(String str) {
        if(connected != Connected.True) {
            Toast.makeText(getActivity(), "not connected", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            Toast.makeText(getContext(), "mensaje: " + str, Toast.LENGTH_SHORT).show();
            byte[] data = (str + newline).getBytes();
            serialSocket.write(data);
        } catch (Exception e) {
            onSerialIoError(e);
        }
    }

    private void status(String str) {
        Toast.makeText(getContext(), "estatus: " + str, Toast.LENGTH_SHORT).show();
    }
}


