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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.exoesqueletov1.R
import com.example.exoesqueletov1.data.local.query.ExoskeletonQuery
import com.example.exoesqueletov1.databinding.FragmentPairedDevisesBinding
import com.example.exoesqueletov1.interfaces.BluetoothResource
import com.example.exoesqueletov1.service.model.BluetoothResponse
import com.example.exoesqueletov1.ui.activity.main.MainActivity
import com.example.exoesqueletov1.ui.fragments.pairedDevises.adapter.PairedDevicesAdapter
import com.example.exoesqueletov1.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

@AndroidEntryPoint
class PairedDevisesFragment : Fragment() {

    private lateinit var binding: FragmentPairedDevisesBinding
    private var bluetoothAdapter: BluetoothAdapter? = null
    private var bondedDevices: MutableSet<BluetoothDevice>? = null
    private lateinit var viewModel: PairedDeviceViewModel
    private val listExoskeletonQuery = mutableListOf<ExoskeletonQuery>()
    private val listDevices = mutableListOf<ExoskeletonQuery>()
    private lateinit var adapter: PairedDevicesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[PairedDeviceViewModel::class.java]
        binding = FragmentPairedDevisesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("ServiceCast")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = PairedDevicesAdapter(listDevices) {
            binding.textStatus.text = "Conectando..."
        }
        bluetoothAdapter =
            (requireContext().getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter

        viewModel.bluetoothDevice.observe(viewLifecycleOwner) {
            listExoskeletonQuery.clear()
            listExoskeletonQuery.addAll(it)
        }

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.BLUETOOTH_CONNECT
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            if (bluetoothAdapter != null) {
                if (!bluetoothAdapter!!.isEnabled) {
                    binding.textStatus.text = getString(R.string.bluetooth_inavil)
                    val enableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                    isEnableBluetooth.launch(enableIntent)
                } else setListeners()
            } else binding.textStatus.text = getString(R.string.bluetooth_not_found)
        } else binding.textStatus.text = getString(R.string.bluetooth_permission)

        binding.apply {
            recyclerBluetoothDevices.setHasFixedSize(true)
            recyclerBluetoothDevices.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            recyclerBluetoothDevices.adapter = adapter
        }
    }

    @SuppressLint("MissingPermission", "NotifyDataSetChanged")
    private fun startProcess() {
        if (listDevices.isNotEmpty() && (listDevices.size == 1) && bluetoothAdapter!!.isDiscovering) {
            EventBus.getDefault().post(listDevices[0])
            binding.textStatus.text = "Conectando..."
            binding.materialCardView3.visibility = View.GONE
            bluetoothAdapter!!.cancelDiscovery()
            requireActivity().unregisterReceiver(broadcastReceiver)
            return
        }
        listDevices.clear()
        bondedDevices = bluetoothAdapter!!.bondedDevices
        bondedDevices!!.forEach { device ->
            listExoskeletonQuery.forEach { exoskeletonModel ->
                if (device.address == exoskeletonModel.mac) {
                    exoskeletonModel.status = Constants.StatusDevice.Emparejado.name
                    exoskeletonModel.name = device.name
                    listDevices.add(exoskeletonModel)
                }
            }
        }
        if (!bluetoothAdapter!!.isDiscovering) {
            val filter = IntentFilter()
            filter.addAction(BluetoothDevice.ACTION_FOUND)
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
            requireActivity().registerReceiver(broadcastReceiver, filter)
            bluetoothAdapter!!.startDiscovery()
        } else {
            binding.textStatus.text = getString(R.string.listo)
            bluetoothAdapter!!.cancelDiscovery()
            requireActivity().unregisterReceiver(broadcastReceiver)
        }
        if (listDevices.isNotEmpty()) adapter.notifyDataSetChanged()

    }

    private fun setListeners() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            binding.textStatus.text = getString(R.string.listo)
            binding.imageView9.setOnClickListener {
                startProcess()
            }
        }
    }

    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        @SuppressLint("MissingPermission", "NotifyDataSetChanged")
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                BluetoothDevice.ACTION_FOUND -> {
                    if (listDevices.size == 1) binding.materialCardView3.visibility = View.GONE
                    if (listDevices.size > 1) binding.materialCardView3.visibility = View.VISIBLE
                    val device =
                        intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                    listExoskeletonQuery.forEach { exoskeletonModel ->
                        if (device!!.address == exoskeletonModel.mac) {
                            exoskeletonModel.status = Constants.StatusDevice.Cercano.name
                            exoskeletonModel.name = device.name
                            listDevices.add(exoskeletonModel)
                        }
                    }
                    if (listDevices.isNotEmpty()) adapter.notifyDataSetChanged()
                }
                BluetoothAdapter.ACTION_DISCOVERY_STARTED -> {
                    binding.textStatus.text = "Da click para cancelar"
                }
                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                    binding.textStatus.text = getString(R.string.reintentar)
                    if (listDevices.isNotEmpty() && listDevices.size == 1) {
                        EventBus.getDefault().post(listDevices[0])
                        binding.textStatus.text = "Conectando..."
                        binding.recyclerBluetoothDevices.visibility = View.GONE
                    }
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private var isEnableBluetooth =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                setListeners()
            }
        }

    /**
     * Method used by **get Status** and data of bluetooth connection in
     * [MainActivity.onSerialConnect], [MainActivity.onSerialConnectError] ...
     * @param resource Is a object type [BluetoothResource] and contains the status
     * in a object type [Constants.StatusBluetoothDevice] and Data in object type [BluetoothResponse].
     *
     * **[BluetoothResource.status] just receive:**
     * - [Constants.StatusBluetoothDevice.Connected]
     * - [Constants.StatusBluetoothDevice.ConnectionFailed]
     *
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun getStatus(resource: BluetoothResource) {
        when (resource.status) {
            Constants.StatusBluetoothDevice.Connected -> {
                //findNavController().navigate(R.id.action_navigation_paired_device_to_connectionFragment)
            }
            Constants.StatusBluetoothDevice.ConnectionFailed -> {
                binding.textStatus.text =
                    "No se pudo conectar con el exoeskeleto.\nIntente otra vez."
                if (listDevices.size > 1) binding.recyclerBluetoothDevices.visibility = View.VISIBLE
            }
            else -> {}
        }
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onPause() {
        super.onPause()
        EventBus.getDefault().unregister(this)
    }

}
