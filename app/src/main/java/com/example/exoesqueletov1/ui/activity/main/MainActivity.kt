package com.example.exoesqueletov1.ui.activity.main

import android.annotation.SuppressLint
import android.bluetooth.BluetoothManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.exoesqueletov1.R
import com.example.exoesqueletov1.data.local.query.ExoskeletonQuery
import com.example.exoesqueletov1.databinding.ActivityMainBinding
import com.example.exoesqueletov1.service.SerialListener
import com.example.exoesqueletov1.service.SerialService
import com.example.exoesqueletov1.service.SerialService.SerialBinder
import com.example.exoesqueletov1.service.SerialSocket
import com.example.exoesqueletov1.ui.activity.sing_in.SingInActivity
import com.example.exoesqueletov1.ui.dialogs.DialogLoading
import com.example.exoesqueletov1.ui.dialogs.DialogOops
import com.example.exoesqueletov1.utils.Constants
import com.example.exoesqueletov1.utils.Constants.Connected
import com.example.exoesqueletov1.utils.Utils.createLoadingDialog
import com.example.exoesqueletov1.utils.Utils.getTypeUser
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), ServiceConnection, SerialListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    private var serialSocket: SerialSocket? = null
    private var serialService: SerialService? = null
    private var connected = Connected.False
    private var deviceAddress = ""
    private var initialStart = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val loading = DialogLoading()
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.userModel.observe(this) {
            val navView = when (it.user.getTypeUser()) {
                Constants.TypeUser.Admin -> {
                    binding.navViewPatient.visibility = View.GONE
                    binding.navViewSpecialist.visibility = View.GONE
                    binding.navView.visibility = View.VISIBLE
                    binding.navView
                }
                Constants.TypeUser.Specialist -> {
                    binding.navViewPatient.visibility = View.GONE
                    binding.navViewSpecialist.visibility = View.VISIBLE
                    binding.navView.visibility = View.GONE
                    binding.navViewSpecialist
                }
                Constants.TypeUser.Patient -> {
                    binding.navViewPatient.visibility = View.VISIBLE
                    binding.navViewSpecialist.visibility = View.GONE
                    binding.navView.visibility = View.GONE
                    binding.navViewPatient
                }
            }
            val navController = findNavController(R.id.nav_host_fragment_activity_main_bottom)
            navView.setupWithNavController(navController)
            navController.addOnDestinationChangedListener { _, destination, _ ->
                if (destination.id == R.id.chatActivity)
                    Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show()
            }
        }

        /*binding.motion.setTransitionDuration(1000)
        binding.motion.setTransition(R.id.start, R.id.open_terminal)
        binding.motion.transitionToEnd()

        supportFragmentManager.beginTransaction()
            .replace(R.id.bluetooth_content, ConnectionFragment()).commit()
*/
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

        binding.topAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.sing_out -> {
                    viewModel.singOut()
                    startActivity(Intent(this, SingInActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }

        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) && checkPermissions(Constants.PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, Constants.PERMISSIONS, 1)
        }
        onAttach()
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onPairedDevice(device: ExoskeletonQuery) {
        deviceAddress = device.mac
        if (initialStart && serialService != null && deviceAddress.isNotEmpty()) {
            initialStart = false
            runOnUiThread { connect() }
        }
    }

    override fun onDestroy() {
        if (connected !== Connected.False) {
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

    fun onAttach() {
        bindService(
            Intent(this, SerialService::class.java),
            this, BIND_AUTO_CREATE
        )
    }

    fun onDetach() {
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
    }

    override fun onSerialConnect() {
        status("connected")
        connected = Connected.True

        send("WALK_MINUTES")

    }

    override fun onSerialConnectError(e: Exception?) {
        setError(e!!)
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
            status("connecting...")
            connected = Connected.Pending
            serialSocket = SerialSocket()
            serialService!!.connect(this, "Connected to $deviceName")
            serialSocket!!.connect(this, serialService, device)
        } catch (e: Exception) {
            onSerialConnectError(e)
        }
    }

    private fun disconnect() {
        connected = Connected.False
        serialService!!.disconnect()
        serialSocket!!.disconnect()
        serialSocket = null
    }

    private fun send(str: String) {
        if (connected !== Connected.True) {
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
            val newline = "\r\n"
            val data = (str + newline).toByteArray()
            serialSocket!!.write(data)
        } catch (e: Exception) {
            onSerialIoError(e)
        }
    }

    private fun status(str: String) {
        Log.d("Status: ", str)
    }

    private fun setError(e: Exception) {
        val dialogOops = DialogOops(e.message)
        dialogOops.show(supportFragmentManager, "WalkFragment")
        disconnect()
    }

}