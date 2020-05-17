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
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.exoesqueletov1.ControlActivity;
import com.example.exoesqueletov1.R;
import com.example.exoesqueletov1.clases.bleutooth.Constants;
import com.example.exoesqueletov1.clases.bleutooth.SerialListener;
import com.example.exoesqueletov1.clases.bleutooth.SerialService;
import com.example.exoesqueletov1.clases.bleutooth.SerialSocket;
import com.example.exoesqueletov1.dialogs.DialogOops;


public class UpAndDownFragment extends Fragment implements ServiceConnection, SerialListener,
        View.OnClickListener {

    private NumberPicker numberPicker;
    private Button buttonLeft;
    private Button buttonRight;
    private TextView terminal;

    private enum Connected { False, Pending, True } // type of variable

    private String address;

    private SerialSocket serialSocket;
    private SerialService serialService;
    private Connected connected = Connected.False;
    private boolean initialStart = true;
    private boolean pause = false;

    UpAndDownFragment(String address) {
        this.address = address;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_up_and_down, container, false);

        ImageView buttonBack = view.findViewById(R.id.image_back_up);
        buttonLeft = view.findViewById(R.id.button_left);
        buttonRight = view.findViewById(R.id.button_right);
        numberPicker = view.findViewById(R.id.numberPicker);
        terminal = view.findViewById(R.id.terminal);
        terminal.setMovementMethod(ScrollingMovementMethod.getInstance());

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
        ControlActivity.getActionButtonPause().setEnabled(false);
        ControlActivity.getActionButtonStop().setEnabled(false);

        buttonBack.setOnClickListener(this);
        buttonLeft.setOnClickListener(this);
        buttonRight.setOnClickListener(this);

        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(9);

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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.flotating_button_report_problem:
            case R.id.flotating_button_help:
                Toast.makeText(getContext(), "Estamos trabajando en ello", Toast.LENGTH_SHORT).show();
                break;
            case R.id.flotating_button_stop:
                ControlActivity.getActionButtonStop().setEnabled(false);
                ControlActivity.getActionButtonPause().setEnabled(false);
                buttonRight.setEnabled(true);
                buttonLeft.setEnabled(true);
                send(Constants.STOP);
                break;
            case R.id.flotating_button_pause:
                if (pause) {
                    send(Constants.PLAY);
                    ControlActivity.getActionButtonPause().setLabelText(getString(R.string.pausar));
                    ControlActivity.getActionButtonPause()
                            .setImageDrawable(getActivity().getDrawable(R.drawable.ic_pause));
                    pause = false;
                }
                else {
                    ControlActivity.getActionButtonPause().setLabelText(getString(R.string.play));
                    ControlActivity.getActionButtonPause()
                            .setImageDrawable(getActivity().getDrawable(R.drawable.ic_play));
                    send(Constants.PAUSE);
                    pause = true;
                }
                break;
            case R.id.button_left:
                ControlActivity.getActionButtonPause().setEnabled(true);
                ControlActivity.getActionButtonStop().setEnabled(true);
                buttonRight.setEnabled(false);
                buttonLeft.setEnabled(false);
                buttonLeft.setEnabled(false);
                send(Constants.UP_LEFT);
                break;
            case R.id.button_right:
                ControlActivity.getActionButtonPause().setEnabled(true);
                ControlActivity.getActionButtonStop().setEnabled(true);
                buttonRight.setEnabled(false);
                buttonLeft.setEnabled(false);
                send(Constants.UP_RIGHT);
                break;
            case R.id.image_back_up:
                getFragmentManager().beginTransaction()
                        .replace(R.id.content, new MenuFragment(address))
                        .commit();
                break;
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
        setError(e);
    }

    @Override
    public void onSerialRead(byte[] data) {
        terminal.append(new String(data));
        try {
            switch (new String(data)) {
                case Constants.LISTEN: {
                    terminal.append("LISTEN");
                    send(String.valueOf(numberPicker.getValue()));
                    break;
                }
                case Constants.FINALISE: {
                    terminal.append("FINALISE");
                    buttonRight.setEnabled(true);
                    buttonLeft.setEnabled(true);
                    ControlActivity.getActionButtonPause().setEnabled(false);
                    ControlActivity.getActionButtonStop().setEnabled(false);
                }
                case Constants.RECEIVED:
                    terminal.append("RECEIVED");
                    break;
            }
        } catch (Exception e) {
            DialogOops oops = new DialogOops(e.getMessage());
            oops.show(getFragmentManager(), "hi");
        }
    }

    @Override
    public void onSerialIoError(Exception e) {
        setError(e);
    }

    private void connect() {
        try {
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
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

    private void send(String string) {
        if(connected != Connected.True) {
            Toast.makeText(getActivity(), "not connected", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            String newline = "\r\n";
            byte[] data = (string + newline).getBytes();
            serialSocket.write(data);
        } catch (Exception e) {
            onSerialIoError(e);
        }
    }

    private void status(String str) {
        Toast.makeText(getContext(), "estatus: " + str, Toast.LENGTH_SHORT).show();
    }

    private void setError(Exception e) {
        DialogOops dialogOops = new DialogOops(e.getMessage());
        dialogOops.show(getFragmentManager(), "WalkFragment");
        disconnect();
        getFragmentManager().beginTransaction().replace(R.id.content, new MenuFragment(address));
    }

}
