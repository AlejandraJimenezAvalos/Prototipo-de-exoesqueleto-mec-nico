package com.example.exoesqueletov1.ui.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.exoesqueletov1.databinding.FragmentPairedDevisesBinding
import com.example.exoesqueletov1.utils.Constants


class PairedDevisesFragment : Fragment() {

    private var state = false
    private lateinit var binding: FragmentPairedDevisesBinding
    private var bluetoothAdapter: BluetoothAdapter? = null
    private var bondedDevices: MutableSet<BluetoothDevice>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPairedDevisesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bluetoothAdapter =
            (requireContext().getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter

        if (bluetoothAdapter != null) {
            if (!bluetoothAdapter!!.isEnabled) {
                val enableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                isEnableBluetooth.launch(enableIntent)
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                startProcess()
            }
        } else binding.textStatus.text =
            "El dispositivo no cuenta con soporte para conectarse al exoesqueleto."
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun startProcess() {
        state = true
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(), Constants.PERMISSIONS,
                1
            )
        } else {
            bondedDevices = bluetoothAdapter!!.bondedDevices
            val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
            requireActivity().registerReceiver(broadcastReceiver, filter)
            bluetoothAdapter!!.startDiscovery()
        }
    }

    override fun onResume() {
        super.onResume()
        if (state && (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)) {
            startProcess()
        }
    }

    @SuppressLint("MissingPermission")
    private var isEnableBluetooth =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    startProcess()
                }
            }
        }

    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (BluetoothDevice.ACTION_FOUND == action) {
                val device =
                    intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                bondedDevices!!.add(device!!)
                Log.e("Bluetooth device: ", "ADDRESS: ${device.address} \nNAME: ${device.name}")
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun onPause() {
        super.onPause()
        bluetoothAdapter!!.cancelDiscovery()
        requireActivity().unregisterReceiver(broadcastReceiver)
    }

}
