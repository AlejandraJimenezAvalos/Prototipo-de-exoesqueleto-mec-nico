package com.example.exoesqueletov1;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.exoesqueletov1.clases.ViewPagerAdapter;
import com.example.exoesqueletov1.dialogs.DialogOops;
import com.example.exoesqueletov1.fragments.PairedDevisesFragment;
import com.example.exoesqueletov1.fragments.UpAndDownFragment;
import com.example.exoesqueletov1.fragments.WalkFragment;
import com.example.exoesqueletov1.fragments.WorksFragment;
import com.github.clans.fab.FloatingActionButton;

import java.io.IOException;
import java.util.UUID;

public class ControlActivity extends AppCompatActivity implements View.OnClickListener {

    private String address = null;
    private boolean stateConnection = false;
    private boolean stateButtonConnect = true;
    private boolean stateButtonPause = true;

    private ViewPagerAdapter adapter;
    private ProgressDialog progress;
    private BluetoothSocket bluetoothSocket = null;

    private FloatingActionButton buttonPause;
    private FloatingActionButton buttonConnection;
    private ViewPager viewPager;
    private ImageView radio1;
    private ImageView radio2;
    private ImageView radio3;
    private ImageView radio4;

    private static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    public static final String CODE_PAUSE = "0";
    public static final String CODE_STOP = "1";
    public static final String CODE_PLAY = "2";
    public static final String CODE_WALK_MINUTES = "3";
    public static final String CODE_WALK_STEPS = "4";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        ImageView buttonFinish;

        radio1 = findViewById(R.id.radio_1);
        radio2 = findViewById(R.id.radio_2);
        radio3 = findViewById(R.id.radio_3);
        radio4 = findViewById(R.id.radio_4);
        buttonFinish = findViewById(R.id.button_finish_control);
        FloatingActionButton buttonHelp = findViewById(R.id.menu_item_help);
        buttonConnection = findViewById(R.id.menu_item_connection);
        buttonPause = findViewById(R.id.menu_item_pause);
        FloatingActionButton buttonStop = findViewById(R.id.menu_item_stop);
        FloatingActionButton buttonReport = findViewById(R.id.menu_item_report);
        viewPager = findViewById(R.id.viewPager2);

        Bundle bundle = this.getIntent().getExtras();
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        address = bundle.getString(PairedDevisesFragment.DEVICE_ADDRESS);

        radio1.setOnClickListener(this);
        radio2.setOnClickListener(this);
        radio3.setOnClickListener(this);
        radio4.setOnClickListener(this);
        buttonHelp.setOnClickListener(this);
        buttonConnection.setOnClickListener(this);
        buttonPause.setOnClickListener(this);
        buttonStop.setOnClickListener(this);
        buttonReport.setOnClickListener(this);
        buttonFinish.setOnClickListener(this);
    }

    private void initViewPager() {
        adapter.addFragment(new WorksFragment());
        adapter.addFragment(new WalkFragment(WalkFragment.CODE_MINUTES));
        adapter.addFragment(new UpAndDownFragment(address));
        adapter.addFragment(new WalkFragment(WalkFragment.CODE_WALK));
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        radio1.setImageResource(R.drawable.ic_radio_button_checked);
                        radio2.setImageResource(R.drawable.ic_radio_button_unchecked);
                        break;
                    case 1:
                        radio1.setImageResource(R.drawable.ic_radio_button_unchecked);
                        radio2.setImageResource(R.drawable.ic_radio_button_checked);
                        radio3.setImageResource(R.drawable.ic_radio_button_unchecked);
                        break;
                    case 2:
                        radio2.setImageResource(R.drawable.ic_radio_button_unchecked);
                        radio3.setImageResource(R.drawable.ic_radio_button_checked);
                        radio4.setImageResource(R.drawable.ic_radio_button_unchecked);
                        break;
                    case 3:
                        radio3.setImageResource(R.drawable.ic_radio_button_unchecked);
                        radio4.setImageResource(R.drawable.ic_radio_button_checked);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private class ConnectBT extends AsyncTask<Void, Void, Void> {
        private boolean ConnectSuccess = true;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(ControlActivity.this,
                    "Conectando...", "Espere un momento");
        }

        @Override
        protected Void doInBackground(Void... devices) {
            try{
                if (bluetoothSocket == null || !stateConnection) {
                    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    BluetoothDevice dispositivo = bluetoothAdapter.getRemoteDevice(address);
                    bluetoothSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    bluetoothSocket.connect();
                }
            }
            catch (IOException e) { ConnectSuccess = false; }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (!ConnectSuccess) {
                DialogOops dialogOops = new DialogOops(getString(R.string.connection_failed));
                dialogOops.show(getSupportFragmentManager(), "");
            }
            else {
                initViewPager();
                stateConnection = true;
                stateButtonConnect = false;
                changeButton(buttonConnection, getDrawable(R.drawable.ic_bluetooth),
                        getString(R.string.desconectar));
            }
            progress.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.radio_1:
                viewPager.setCurrentItem(0);
                break;
            case R.id.radio_2:
                viewPager.setCurrentItem(1);
                break;
            case R.id.radio_3:
                viewPager.setCurrentItem(2);
                break;
            case R.id.radio_4:
                viewPager.setCurrentItem(3);
                break;
            case R.id.button_finish_control:
                finish();
                break;
            case R.id.menu_item_connection: {
                if (stateButtonConnect) {
                    new ConnectBT().execute();
                }
                else {
                    try {
                        bluetoothSocket.close();
                        finish();
                    }
                    catch (IOException e) {
                        DialogOops dialogOops = new DialogOops(e.getMessage());
                        dialogOops.show(getSupportFragmentManager(), "");
                    }
                }
                break;
            }
            case R.id.menu_item_pause: {
                if (stateButtonPause) {
                    send(CODE_PAUSE);
                }
                else {
                    changeButton(buttonPause, getDrawable(R.drawable.ic_pause),
                            getString(R.string.pausar));
                    stateButtonPause = true;
                    send(CODE_PLAY);
                }
                break;
            }
            case R.id.menu_item_stop:
                send(CODE_STOP);
                break;
            case R.id.menu_item_help:
            case R.id.menu_item_report:
                Toast.makeText(this, "Estamos trabajando en ello", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void send(String code) {
        if (bluetoothSocket !=null) {
            try {
                bluetoothSocket.getOutputStream().write(code.getBytes());
                if (code.equals(CODE_PAUSE) && stateButtonPause) {
                    changeButton(buttonPause, getDrawable(R.drawable.ic_play), getString(R.string.play));
                    stateButtonPause = false;
                }
            }
            catch (IOException e) {
                DialogOops dialogOops = new DialogOops(e.getMessage());
                dialogOops.show(getSupportFragmentManager(), "");
            }
        } else {
            Toast.makeText(this, R.string.advertencia_bluetooth, Toast.LENGTH_SHORT).show();
        }
    }

    private void changeButton(FloatingActionButton button, Drawable drawable, String label) {
        button.setLabelText(label);
        button.setImageDrawable(drawable);
    }

}
