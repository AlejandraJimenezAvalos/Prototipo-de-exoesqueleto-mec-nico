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
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.format.DateFormat;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ForegroundColorSpan;
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

import com.example.exoesqueletov1.ConstantsDatabase;
import com.example.exoesqueletov1.R;
import com.example.exoesqueletov1.clases.Authentication;
import com.example.exoesqueletov1.clases.bleutooth.SerialListener;
import com.example.exoesqueletov1.clases.bleutooth.SerialService;
import com.example.exoesqueletov1.clases.bleutooth.SerialSocket;
import com.example.exoesqueletov1.dialogs.DialogOops;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.exoesqueletov1.ConstantsDatabase.ACTION;
import static com.example.exoesqueletov1.ConstantsDatabase.COLLECTION_USERS;
import static com.example.exoesqueletov1.ConstantsDatabase.ID_EXOESQUELETO;
import static com.example.exoesqueletov1.ConstantsDatabase.NO;
import static com.example.exoesqueletov1.ConstantsDatabase.NUMBER;
import static com.example.exoesqueletov1.ConstantsDatabase.STATE;
import static com.example.exoesqueletov1.ControlActivity.getActionButtonHelp;
import static com.example.exoesqueletov1.ControlActivity.getActionButtonPause;
import static com.example.exoesqueletov1.ControlActivity.getActionButtonReport;
import static com.example.exoesqueletov1.ControlActivity.getActionButtonStop;
import static com.example.exoesqueletov1.ControlActivity.setActionButtonHelp;
import static com.example.exoesqueletov1.ControlActivity.setActionButtonPause;
import static com.example.exoesqueletov1.ControlActivity.setActionButtonReport;
import static com.example.exoesqueletov1.ControlActivity.setActionButtonStop;
import static com.example.exoesqueletov1.clases.bleutooth.Constants.FINALISE;
import static com.example.exoesqueletov1.clases.bleutooth.Constants.LISTEN;
import static com.example.exoesqueletov1.clases.bleutooth.Constants.PAUSE;
import static com.example.exoesqueletov1.clases.bleutooth.Constants.PLAY;
import static com.example.exoesqueletov1.clases.bleutooth.Constants.RECEIVED;
import static com.example.exoesqueletov1.clases.bleutooth.Constants.STOP;
import static com.example.exoesqueletov1.clases.bleutooth.Constants.WALK_MINUTES;
import static com.example.exoesqueletov1.clases.bleutooth.Constants.WALK_STEPS;

public class WalkFragment extends Fragment implements ServiceConnection, SerialListener, View.OnClickListener {

    private enum Connected { False, Pending, True; }
    private TextView indicationWalk;

    private ImageView imageViewBack;
    private TextView titleWalk;
    private NumberPicker numberPicker;
    private Button buttonWalkfragment;
    private TextView terminal;
    private SerialSocket serialSocket;

    private SerialService serialService;
    private Connected connected = Connected.False;

    private boolean initialStart = true;
    private boolean pause = false;
    private boolean state = false;
    private int code;
    private String address;
    private String idDocument;
    private String typeUser;
    private int n;

    static final int CODE_WALK_MINUTES = 0;
    static final int CODE_WALK_STEPS = 1;

    WalkFragment(int code, String address, String typeUser) {
        this.code = code;
        this.address = address;
        this.typeUser = typeUser;
    }

    WalkFragment(int code, String address, String idDocument, int n, String typeUser) {
        this.code = code;
        this.address = address;
        this.idDocument = idDocument;
        this.n = n;
        this.typeUser = typeUser;
        state = true;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_walk, container, false);

        imageViewBack = view.findViewById(R.id.image_back_walk);
        buttonWalkfragment = view.findViewById(R.id.button_walk_fragment);
        numberPicker = view.findViewById(R.id.number_picker);
        indicationWalk = view.findViewById(R.id.indication_walk);
        titleWalk = view.findViewById(R.id.title_walk);
        terminal = view.findViewById(R.id.terminal);
        setActionButtonReport(getActivity().findViewById(R.id.flotating_button_report_problem));
        setActionButtonStop(getActivity().findViewById(R.id.flotating_button_stop));
        setActionButtonPause(getActivity().findViewById(R.id.flotating_button_pause));
        setActionButtonHelp(getActivity().findViewById(R.id.flotating_button_help));

        numberPicker.setMaxValue(9);
        numberPicker.setMinValue(2);

        getActionButtonReport().setOnClickListener(this);
        getActionButtonStop().setOnClickListener(this);
        getActionButtonPause().setOnClickListener(this);
        getActionButtonHelp().setOnClickListener(this);
        getActionButtonStop().setEnabled(false);
        getActionButtonPause().setEnabled(false);
        getActionButtonReport().setEnabled(true);
        getActionButtonHelp().setEnabled(true);
        imageViewBack.setOnClickListener(this);
        buttonWalkfragment.setOnClickListener(this);

        if (code == CODE_WALK_STEPS) { initWalk(); }
        if (typeUser.equals(ConstantsDatabase.PATIENT)) { terminal.setVisibility(View.INVISIBLE); }
        else { terminal.setMovementMethod(ScrollingMovementMethod.getInstance()); }

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
        if (state) {
            getActionButtonStop().setEnabled(true);
            getActionButtonPause().setEnabled(true);
            buttonWalkfragment.setEnabled(false);
            imageViewBack.setEnabled(false);
            if(code == CODE_WALK_MINUTES) {
                send(WALK_MINUTES);
            }
            else { send(WALK_STEPS); }
        }
    }

    @Override
    public void onSerialConnectError(Exception e) {
        setError(e);
    }

    @Override
    public void onSerialRead(byte[] data) {
        try {
            switch (new String(data)) {
                case LISTEN: {
                    terminal.append("LISTEN\n");
                    if (state) { send(String.valueOf(n)); }
                    else { send(String.valueOf(numberPicker.getValue())); }
                    break;
                }
                case FINALISE: {
                    terminal.append("FINALISE\n");
                    buttonWalkfragment.setEnabled(true);
                    imageViewBack.setEnabled(true);
                    getActionButtonPause().setEnabled(false);
                    getActionButtonStop().setEnabled(false);
                    if (state) { updateItem(); }
                    else { createNewItem(); }
                }
                case RECEIVED:
                    terminal.append("RECEIVED\n");
                    break;
            }
        } catch (Exception e) {
            DialogOops oops = new DialogOops(e.getMessage());
            oops.show(getFragmentManager(), "hi");
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
                getActionButtonStop().setEnabled(false);
                getActionButtonPause().setEnabled(false);
                getActionButtonPause().setLabelText(getString(R.string.pausar));
                getActionButtonPause()
                        .setImageDrawable(getActivity().getDrawable(R.drawable.ic_pause));
                pause = false;
                buttonWalkfragment.setEnabled(true);
                imageViewBack.setEnabled(true);
                send(STOP);
                break;
            case R.id.flotating_button_pause:
                if (pause) {
                    send(PLAY);
                    getActionButtonPause().setLabelText(getString(R.string.pausar));
                    getActionButtonPause()
                            .setImageDrawable(getActivity().getDrawable(R.drawable.ic_pause));
                    pause = false;
                }
                else {
                    getActionButtonPause().setLabelText(getString(R.string.play));
                    getActionButtonPause()
                            .setImageDrawable(getActivity().getDrawable(R.drawable.ic_play));
                    send(PAUSE);
                    pause = true;
                }
                break;
            case R.id.image_back_walk:
                getFragmentManager().beginTransaction()
                        .replace(R.id.content, new MenuFragment(address, typeUser))
                        .commit();
                break;
            case R.id.button_walk_fragment:
                getActionButtonStop().setEnabled(true);
                getActionButtonPause().setEnabled(true);
                buttonWalkfragment.setEnabled(false);
                imageViewBack.setEnabled(false);
                if(code == CODE_WALK_MINUTES) { send(WALK_MINUTES); }
                else { send(WALK_STEPS); }
                break;
        }
    }

    private void updateItem() {
        String id = new Authentication().getCurrentUser().getEmail();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference reference = db.collection(COLLECTION_USERS).document(id);
        reference.get().addOnCompleteListener(task -> {
            DocumentReference documentReference = db.collection(task.getResult().getData()
                    .get(ID_EXOESQUELETO).toString()).document(idDocument);
            documentReference.get().addOnCompleteListener(task1 -> {
                Map<String, Object> data = task1.getResult().getData();
                data.remove(STATE);
                data.put(STATE, true);
                documentReference.update(data);
            });
        });
    }

    private void createNewItem() {
        String id = new Authentication().getCurrentUser().getEmail();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference reference = db.collection(COLLECTION_USERS).document(id);
        reference.get().addOnCompleteListener(task -> {
            String idCollection = task.getResult().getData().get(ID_EXOESQUELETO).toString();
            CollectionReference collection = db.collection(idCollection);
            collection.get().addOnCompleteListener(task1 -> {
                Map <String, Object> data = new HashMap<>();
                if(code == CODE_WALK_MINUTES) { data.put(ACTION, WALK_MINUTES); }
                else { data.put(ACTION, WALK_STEPS); }
                data.put(NUMBER, n);
                data.put(ConstantsDatabase.STATE, true);
                data.put(NO, task1.getResult().size());
                data.put(ConstantsDatabase.DATE,
                        DateFormat.format("MMMM d, yyyy, HH:mm:ss", new Date().getTime()));
                db.collection(idCollection).add(data);
            });
        });
    }

    @Override
    public void onSerialIoError(Exception e) {
        setError(e);
    }

    private void initWalk() {
        titleWalk.setText(R.string.walk_title2);
        indicationWalk.setText(R.string.walk_indication2);
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

    private void send(String str) {
        if(connected != Connected.True) {
            Toast.makeText(getActivity(), "not connected", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            SpannableStringBuilder spn = new SpannableStringBuilder("SEND: ");
            spn.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.pinkDark)), 0,
                    spn.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            terminal.append(spn);
            terminal.append(str + "\n");
            String newline = "\r\n";
            byte[] data = (str + newline).getBytes();
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
        getFragmentManager().beginTransaction().replace(R.id.content, new MenuFragment(address, address));
    }

}


