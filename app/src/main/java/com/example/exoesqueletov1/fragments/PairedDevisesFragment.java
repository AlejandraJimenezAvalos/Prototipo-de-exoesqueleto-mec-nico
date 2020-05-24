package com.example.exoesqueletov1.fragments;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.exoesqueletov1.ControlActivity;
import com.example.exoesqueletov1.R;

import java.util.ArrayList;
import java.util.Set;

public class PairedDevisesFragment extends Fragment {

    private String typeUser;
    private BluetoothAdapter bluetoothAdapter = null;
    public static final String DEVICE_ADDRESS = "deviceAddress";
    public static final String TYPE_USER = "typeUser";
    private static final int REQUEST_CODE = 1;

    private ListView listView;


    public PairedDevisesFragment(String typeUser) {
        this.typeUser = typeUser;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_paired_devises, container, false);

        Button buttonPaired = view.findViewById(R.id.button_paired_devises);
        listView = view.findViewById(R.id.listView);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null) {
            Toast.makeText(getContext(), R.string.no_bluetooth, Toast.LENGTH_LONG).show();
            getActivity().finish();
        }
        if (!bluetoothAdapter.isEnabled()) {
            Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnBTon, REQUEST_CODE);
        }

        pairedDevicesList();

        buttonPaired.setOnClickListener(v -> pairedDevicesList());

        return view;
    }

    private void pairedDevicesList() {
        Set<BluetoothDevice> bluetoothDevices = bluetoothAdapter.getBondedDevices();
        ArrayList list = new ArrayList();

        if (bluetoothDevices.size() > 0) {
            for (BluetoothDevice bt : bluetoothDevices) {
                list.add(bt.getName() + "\n" + bt.getAddress());
                //Get the device's name and the address
            }
        } else {
            Toast.makeText(getContext(), "No hay dispositivos",
                    Toast.LENGTH_LONG).show();
        }

        final ArrayAdapter adapter;
        adapter = new ArrayAdapter(getContext(), R.layout.list_item_withe, R.id.list_content, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(myListClickListener);
    }

    private AdapterView.OnItemClickListener myListClickListener = (av, view, arg2, arg3) -> {
        TextView textView = view.findViewById(R.id.list_content);
        String info = textView.getText().toString();
        String address = info.substring(info.length() - 17);
        Intent intent = new Intent(getContext(), ControlActivity.class);
        intent.putExtra(DEVICE_ADDRESS, address);
        intent.putExtra(TYPE_USER, typeUser);
        startActivity(intent);
    };
}
