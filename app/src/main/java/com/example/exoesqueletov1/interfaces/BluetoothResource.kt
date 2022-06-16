package com.example.exoesqueletov1.interfaces

import com.example.exoesqueletov1.service.model.BluetoothResponse
import com.example.exoesqueletov1.ui.activity.main.MainActivity
import com.example.exoesqueletov1.ui.fragments.connection.ConnectionFragment
import com.example.exoesqueletov1.ui.fragments.pairedDevises.PairedDevisesFragment
import com.example.exoesqueletov1.utils.Constants

/**
 * This interface is used in [MainActivity.onSerialConnect] and [MainActivity.onSerialConnectError] to [PairedDevisesFragment.getStatus]
 * and [ConnectionFragment.getStatus] for send:
 * - Data of Bluetooth Device
 * - Status of the Connection with Bluetooth Device
 *
 * @param status as a object type [Constants.StatusBluetoothDevice] and contains status of the connection.
 * @param data as a object type [BluetoothResponse] and contains of the Bluetooth Device.
 * @param exception is a object type [Exception] and used to send errors to
 * [PairedDevisesFragment] and [ConnectionFragment]
 */
data class BluetoothResource(
    val status: Constants.StatusBluetoothDevice,
    val data: BluetoothResponse?,
    val exception: Exception?,
) {
    companion object {
        fun connect() =
            BluetoothResource(Constants.StatusBluetoothDevice.Connected, null, null)

        fun pending() =
            BluetoothResource(Constants.StatusBluetoothDevice.Pending, null, null)

        fun disconnected() =
            BluetoothResource(Constants.StatusBluetoothDevice.Disconnected, null, null)

        fun connectionFailed(exception: Exception) =
            BluetoothResource(Constants.StatusBluetoothDevice.ConnectionFailed, null, exception)

        fun error(exception: Exception) =
            BluetoothResource(Constants.StatusBluetoothDevice.Error, null, exception)

        fun read(data: BluetoothResponse) =
            BluetoothResource(Constants.StatusBluetoothDevice.Read, null, null)
    }
}