package com.example.exoesqueletov1.hello;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.exoesqueletov1.R;

import java.io.IOException;
import java.util.UUID;

public class ledControl extends AppCompatActivity {

    Button btnOn, btnOff, btnDis;
    SeekBar brightness;
    TextView lumn;
    String address = null;
    private ProgressDialog progress;
    BluetoothAdapter bluetoothAdapter = null;
    BluetoothSocket bluetoothSocket = null;
    private boolean isBtConnected = false;
    //SPP UUID. Look for it
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent newint = getIntent();
      //  address = newint.getStringExtra(DeviceList.EXTRA_ADDRESS); //receive the address of the bluetooth device

        //view of the ledControl
      //  setContentView(R.layout.activity_led_control);

        //call the widgtes
        btnOn = (Button)findViewById(R.id.button2);
        btnOff = (Button)findViewById(R.id.button3);
        btnDis = (Button)findViewById(R.id.button4);
     //   brightness = (SeekBar)findViewById(R.id.seekBar);
     //   lumn = (TextView)findViewById(R.id.lumn);

        new ConnectBT().execute(); //Call the class to connect

        //commands to be sent to bluetooth
        btnOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { turnOnLed(); }      //method to turn on
        });

        btnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { turnOffLed(); }   //method to turn off
        });

        btnDis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { Disconnect(); } //close connection
        });

        brightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    lumn.setText(String.valueOf(progress));
                    try { bluetoothSocket.getOutputStream().
                            write(String.valueOf(progress).getBytes()); }
                    catch (IOException e) { msg("Algo salio mal, muy mal con el brig"); }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { msg("onStartTracking SeekBar"); }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { msg("onStopTraking SeekBar"); }
        });
    }

    private void Disconnect() {
        if (bluetoothSocket !=null) {//If the btSocket is busy
            try { bluetoothSocket.close(); }//close connection
            catch (IOException e) { msg("Error");}
        }
        finish(); //return to the first layout

    }

    private void turnOffLed() {
        if (bluetoothSocket !=null) {
            try { bluetoothSocket.getOutputStream().write("TF".getBytes()); }
            catch (IOException e) { msg("Error"); }
        }
    }

    private void turnOnLed() {
        if (bluetoothSocket !=null) {
            try { bluetoothSocket.getOutputStream().write("TO".getBytes()); }
            catch (IOException e) { msg("Error"); }
        }
    }

    // fast way to call Toast
    private void msg(String s) {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }

    private class ConnectBT extends AsyncTask<Void, Void, Void> { // UI thread
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(ledControl.this, "Connecting...", "Please wait!!!");
            //show a progress dialog
        }

        @Override
        protected Void doInBackground(Void... devices) {
            //while the progress dialog is shown, the connection is done in background
            try{
                if (bluetoothSocket == null || !isBtConnected) {
                    bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    //get the mobile bluetooth device
                    BluetoothDevice dispositivo = bluetoothAdapter.getRemoteDevice(address);
                    //connects to the device's address and checks if it's available
                    bluetoothSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);
                    //create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    bluetoothSocket.connect();//start connection
                }
            }
            catch (IOException e) { ConnectSuccess = false; }
            //if the try failed, you can check the exception here
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            //after the doInBackground, it checks if everything went fine
            super.onPostExecute(result);

            if (!ConnectSuccess) {
                msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
                finish();
            }
            else {
                msg("Connected.");
                isBtConnected = true;
            }
            progress.dismiss();
        }
    }
}

