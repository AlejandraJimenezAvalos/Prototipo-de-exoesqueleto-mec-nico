package com.example.exoesqueletov1.ui.activity.main

import android.annotation.SuppressLint
import android.bluetooth.BluetoothManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.method.ScrollingMovementMethod
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.exoesqueletov1.R
import com.example.exoesqueletov1.data.local.query.ExoskeletonQuery
import com.example.exoesqueletov1.databinding.ActivityMainBinding
import com.example.exoesqueletov1.interfaces.BluetoothResource
import com.example.exoesqueletov1.service.SerialService
import com.example.exoesqueletov1.service.SerialService.SerialBinder
import com.example.exoesqueletov1.service.SerialSocket
import com.example.exoesqueletov1.service.interfaces.SerialListener
import com.example.exoesqueletov1.ui.activity.sing_in.SingInActivity
import com.example.exoesqueletov1.ui.dialogs.DialogLoading
import com.example.exoesqueletov1.ui.dialogs.DialogOops
import com.example.exoesqueletov1.ui.fragments.pairedDevises.adapter.PairedDevicesViewHolder
import com.example.exoesqueletov1.utils.Constants
import com.example.exoesqueletov1.utils.Constants.Connection
import com.example.exoesqueletov1.utils.Utils.createLoadingDialog
import com.example.exoesqueletov1.utils.Utils.getTypeUser
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.math.MathUtils
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), ServiceConnection, SerialListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private var previusId = R.id.navigation_home

    private var serialSocket: SerialSocket? = null
    private var serialService: SerialService? = null
    private var connection = Connection.False
    private var deviceAddress = ""
    private var initialStart = true
    private var terminalState = false
    private var terminalVisibility = false
    private var menuInflate = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val loading = DialogLoading()
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navController = findNavController(R.id.nav_host_fragment_activity_main_bottom_other)

        viewModel.userModel.observe(this) {
            if (menuInflate) {
                menuInflate = false
                binding.navigationView.inflateMenu(
                    when (it.user.getTypeUser()) {
                        Constants.TypeUser.Admin -> R.menu.bottom_nav_menu
                        Constants.TypeUser.Specialist -> R.menu.bottom_nav_menu_specialist
                        Constants.TypeUser.Patient -> R.menu.bottom_nav_menu_patient
                    }
                )
            }
            binding.navigationView.setupWithNavController(navController)

            val bottomSheetBehavior = BottomSheetBehavior.from(binding.navigationView)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

            binding.bottomAppBar.setNavigationOnClickListener {
                bottomSheetBehavior.state =
                    if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_HIDDEN) {
                        binding.scrim.visibility = View.GONE
                        BottomSheetBehavior.STATE_HIDDEN
                    } else {
                        binding.scrim.visibility = View.VISIBLE
                        BottomSheetBehavior.STATE_COLLAPSED
                    }
            }

            binding.navigationView.setNavigationItemSelectedListener { menuItem ->
                val destinationId = when (menuItem.itemId) {
                    R.id.navigation_home -> R.id.action_global_navigation_home
                    R.id.navigation_profile -> R.id.action_global_navigation_profile
                    R.id.navigation_message -> R.id.action_global_navigation_message
                    R.id.navigation_paired_device -> R.id.action_global_navigation_paired_device
                    R.id.navigation_work -> R.id.action_global_navigation_work
                    else -> R.id.action_global_navigation_home
                }

                if (destinationId != previusId) {
                    navController.navigate(destinationId)
                }

                menuItem.isChecked = true
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                previusId = destinationId
                binding.scrim.visibility = View.GONE
                true
            }

            binding.scrim.setOnClickListener {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                binding.scrim.visibility = View.GONE
            }

            bottomSheetBehavior.addBottomSheetCallback(object :
                BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    val baseColor = Color.BLACK
                    // 60% opacity
                    val baseAlpha =
                        ResourcesCompat.getFloat(resources, R.dimen.material_emphasis_medium)
                    // Map slideOffset from [-1.0, 1.0] to [0.0, 1.0]
                    val offset = (slideOffset - (-1f)) / (1f - (-1f)) * (1f - 0f) + 0f
                    val alpha = MathUtils.lerp(0f, 255f, offset * baseAlpha).toInt()
                    val color = Color.argb(alpha, baseColor.red, baseColor.green, baseColor.blue)
                    binding.scrim.setBackgroundColor(color)
                }

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                }
            })
        }
        viewModel.result.observe(this) {
            it.status.createLoadingDialog(
                supportFragmentManager,
                SingInActivity::class.java.name,
                loading
            )
            if (it.status == Constants.Status.Failure) {
                val errorDialog = DialogOops(it.exception!!.message)
                errorDialog.show(supportFragmentManager, MainActivity::class.java.name)
            }
        }
        binding.bottomAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.sing_out -> {
                    if (connection == Connection.False) {
                        viewModel.singOut()
                        startActivity(Intent(this, SingInActivity::class.java))
                        finish()
                    }
                    true
                }
                R.id.disconect -> {
                    if (connection != Connection.True) {
                        disconnect()
                        it.icon = getDrawable(R.drawable.ic_round_bluetooth_disabled_24)
                    } else if (deviceAddress.isNotEmpty()) {
                        connect()
                        it.icon = getDrawable(R.drawable.ic_bluetooth_connected)
                    }

                    true
                }
                R.id.stop -> {
                    send("stop")
                    false
                }
                else -> false
            }
        }
        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) && checkPermissions(Constants.PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, Constants.PERMISSIONS, 1)
        } else onAttach()
        //binding.buttonStop.setOnClickListener { send("stop") }
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val destinationId = when (destination.id) {
                R.id.navigation_home -> destination.id
                R.id.navigation_profile -> destination.id
                R.id.navigation_message -> destination.id
                R.id.navigation_paired_device -> destination.id
                R.id.navigation_work -> destination.id
                else -> previusId
            }
            previusId = destinationId

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if ((requestCode == 1) && (grantResults.isNotEmpty())) {
            grantResults.forEach {
                if (it != PackageManager.PERMISSION_GRANTED) {
                    Snackbar.make(
                        binding.root,
                        "No se puede continuar por no contar con los permisos requeridos.",
                        Snackbar.LENGTH_INDEFINITE
                    ).setAction("Aceptar") {
                        finish()
                    }.show()
                }
            }
        }
    }

    private fun checkPermissions(permissions: Array<String>): Boolean {
        var permissionResult = true
        permissions.forEach {
            val permissionState = ContextCompat.checkSelfPermission(this, it)
            permissionResult =
                permissionResult && (permissionState == PackageManager.PERMISSION_GRANTED)
        }
        return !permissionResult
    }

    /**
     * Method used by get device on [PairedDevicesViewHolder.bind]
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun getDevice(device: ExoskeletonQuery) {
        if (terminalState) {
            /*binding.buttonCloseTerminal.setOnClickListener {
                binding.terminal.visibility =
                    if (binding.terminal.visibility == View.VISIBLE) View.GONE else View.VISIBLE
                terminalVisibility = !terminalVisibility
            }*/
        }
        binding.terminal.movementMethod = ScrollingMovementMethod.getInstance()
        deviceAddress = device.mac
        val spn = SpannableStringBuilder("Address selected:\n")
        spn.setSpan(
            ForegroundColorSpan(getColor(R.color.green)), 0,
            spn.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        //binding.textNameDevice.text = device.name
        binding.terminal.append(spn)
        binding.terminal.append("   ID:      ${device.id}\n")
        binding.terminal.append("   Name:    ${device.name}\n")
        binding.terminal.append("   Address: ${device.mac}\n")
        if (initialStart && serialService != null && deviceAddress.isNotEmpty()) {
            initialStart = false
            runOnUiThread { connect() }
        }
    }

    override fun onDestroy() {
        if (connection !== Connection.False) {
            disconnect()
        }
        stopService(Intent(this, SerialService::class.java))
        onDetach()
        super.onDestroy()
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
        if (serialService != null) {
            serialService!!.attach(this)
        } else {
            startService(Intent(this, SerialService::class.java))
        }
    }

    override fun onStop() {
        EventBus.getDefault().unregister(this)
        if (serialService != null && !isChangingConfigurations) serialService!!.detach()
        super.onStop()
    }

    /**
     * This method replace [Fragment.onAttach] of Lifecycle of a Fragment.
     */
    private fun onAttach() {
        bindService(
            Intent(this, SerialService::class.java),
            this, BIND_AUTO_CREATE
        )
    }

    /**
     * This method replace [Fragment.onDetach] of Lifecycle of a Fragment.
     */
    private fun onDetach() {
        try {
            unbindService(this)
        } catch (ignored: Exception) {
        }
    }

    override fun onResume() {
        super.onResume()
        if (initialStart && serialService != null && deviceAddress.isNotEmpty()) {
            initialStart = false
            runOnUiThread { connect() }
        }
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder) {
        serialService = (service as SerialBinder).service
        if (initialStart && lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED) && deviceAddress.isNotEmpty()) {
            initialStart = false
            runOnUiThread { connect() }
        }
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        serialService = null
        //binding.buttonStop.visibility = View.GONE
    }

    /**
     * In this method used [EventBus] for send Bluetooth
     * Status on: [Constants.StatusBluetoothDevice.Connected]
     */
    override fun onSerialConnect() {
        status("connected")
        connection = Connection.True
        EventBus.getDefault().post(BluetoothResource.connect())
        //binding.buttonStop.visibility = View.VISIBLE
        //binding.constraintLayout2.visibility = View.GONE
        binding.terminal.visibility = if (terminalVisibility) View.VISIBLE else View.GONE
        //binding.buttonCloseTerminal.visibility = View.VISIBLE
        //send("WALK_MINUTES")
    }

    /**
     * In this method used [EventBus] for send Bluetooth
     * Status on: [Constants.StatusBluetoothDevice.ConnectionFailed]
     */
    override fun onSerialConnectError(e: Exception?) {
        EventBus.getDefault().post(BluetoothResource.connectionFailed(e!!))
        setError(e)
    }

    override fun onSerialRead(data: ByteArray?) {
        try {
            status(String(data!!))
        } catch (e: Exception) {
            val oops = DialogOops(e.message)
            oops.show(supportFragmentManager, "hi")
        }
    }

    override fun onSerialIoError(e: Exception?) {
        setError(e!!)
    }

    @SuppressLint("MissingPermission")
    private fun connect() {
        try {
            val bluetoothAdapter =
                (getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
            val device = bluetoothAdapter.getRemoteDevice(deviceAddress)!!
            val deviceName = if (device.name != null) device.name else device.address
            status("Connecting...")
            connection = Connection.Pending
            serialSocket = SerialSocket()
            serialService!!.connect(this, serialSocket)
            serialSocket!!.connect(this, serialService, device)
        } catch (e: Exception) {
            onSerialConnectError(e)
        }
    }

    private fun disconnect() {
        connection = Connection.False
        initialStart = true
        status("Disconnected")
        serialService!!.disconnect()
        serialSocket!!.disconnect()
        serialSocket = null
        EventBus.getDefault().post(BluetoothResource.disconnected())
        //binding.buttonStop.visibility = View.GONE
        //binding.buttonCloseTerminal.visibility = View.GONE
        binding.terminal.visibility = View.GONE
        //binding.constraintLayout2.visibility = View.VISIBLE
    }

    private fun send(str: String) {
        if (connection !== Connection.True) {
            status("not connected")
            return
        }
        try {
            val spn = SpannableStringBuilder("SEND: ")
            spn.setSpan(
                ForegroundColorSpan(getColor(R.color.pinkDark)), 0,
                spn.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            Log.d("Status: ", spn.toString())
            Log.d("Status: ", str)
            binding.terminal.append(spn)
            binding.terminal.append("$str\n")
            val newline = "\r\n"
            val data = (str + newline).toByteArray()
            serialSocket!!.write(data)
        } catch (e: Exception) {
            onSerialIoError(e)
        }
    }

    private fun status(str: String) {
        val spn = SpannableStringBuilder("STATUS: ")
        spn.setSpan(
            ForegroundColorSpan(getColor(R.color.pink)), 0,
            spn.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.terminal.append(spn)
        binding.terminal.append("$str\n")
        Log.d("Status: ", str)
    }

    private fun setError(e: Exception) {
        val spn = SpannableStringBuilder("Error: ")
        spn.setSpan(
            ForegroundColorSpan(getColor(R.color.error)), 0,
            spn.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.terminal.append(spn)
        binding.terminal.append("${e.message}\n")
        val dialogOops = DialogOops(e.message)
        dialogOops.show(supportFragmentManager, "WalkFragment")
        disconnect()
    }

}