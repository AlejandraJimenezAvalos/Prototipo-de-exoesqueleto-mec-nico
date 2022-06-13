package com.example.exoesqueletov1.ui.fragments.pairedDevises

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
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.exoesqueletov1.databinding.FragmentPairedDevisesBinding

class PairedDevisesFragment : Fragment() {

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

    @SuppressLint("ServiceCast")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bluetoothAdapter =
            (requireContext().getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.BLUETOOTH_CONNECT
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            if (bluetoothAdapter != null) {
                if (!bluetoothAdapter!!.isEnabled) {
                    binding.textStatus.text =
                        "El Bluetooth esta deshabilitado."
                    val enableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                    isEnableBluetooth.launch(enableIntent)
                } else setListeners()
            } else binding.textStatus.text =
                "El dispositivo no cuenta con soporte para conectarse al exoesqueleto."
        } else binding.textStatus.text =
            "La aplicación no cuenta con permisos de Bluetooth."

    }

    @SuppressLint("MissingPermission")
    private fun startProcess() {
        bondedDevices = bluetoothAdapter!!.bondedDevices
        if (!bluetoothAdapter!!.isDiscovering) {
            val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
            requireActivity().registerReceiver(broadcastReceiver, filter)
            bluetoothAdapter!!.startDiscovery()
            binding.textStatus.text =
                "Buscando conexion..."
            binding.buttonScan.setText("Cancelar")
        } else {
            binding.textStatus.text =
                "Listo para comenzar."
            binding.buttonScan.setText("Buscar conexión")
            bluetoothAdapter!!.cancelDiscovery()
            requireActivity().unregisterReceiver(broadcastReceiver)
        }

    }

    @SuppressLint("MissingPermission")
    private var isEnableBluetooth =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                setListeners()
            }
        }

    private fun setListeners() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            binding.textStatus.text = "Listo para comenzar."
            binding.buttonScan.setOnClickListener {
                startProcess()
            }
            binding.imageView9.setOnClickListener {
                startProcess()
            }
        }
    }

    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (BluetoothDevice.ACTION_FOUND == action) {
                val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                Log.e("Bluetooth device: ", "ADDRESS: ${device!!.address} \nNAME: ${device.name}")
            }
        }
    }

}
