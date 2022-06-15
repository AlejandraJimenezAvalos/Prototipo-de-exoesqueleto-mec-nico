package com.example.exoesqueletov1.interfaces

import com.example.exoesqueletov1.service.model.BluetoothResponse
import com.example.exoesqueletov1.ui.activity.main.MainActivity
import com.example.exoesqueletov1.ui.fragments.connection.ConnectionFragment
import com.example.exoesqueletov1.ui.fragments.pairedDevises.PairedDevisesFragment
import com.example.exoesqueletov1.utils.ConstantsBluetooth

/**
 * This interface is used in [MainActivity.onSerialConnect] and [MainActivity.onSerialConnectError] to [PairedDevisesFragment.getStatus]
 * and [ConnectionFragment.getStatus] for send:
 * - Data of Bluetooth Device
 * - Status of the Connection with Bluetooth Device
 *
 * @param status as a object type [ConstantsBluetooth.Status] and contains status of the connection.
 * @param data as a object type [BluetoothResponse] and contains of the Bluetooth Device.
 * @param exception is a object type [Exception] and used to send errors to
 * [PairedDevisesFragment] and [ConnectionFragment]
 */
data class BluetoothResource(
    val status: ConstantsBluetooth.Status,
    val data: BluetoothResponse?,
    val exception: Exception?,
) {
    companion object {
        fun connect() =
            BluetoothResource(ConstantsBluetooth.Status.Connected, null, null)

        fun pending() =
            BluetoothResource(ConstantsBluetooth.Status.Pending, null, null)

        fun disconnected() =
            BluetoothResource(ConstantsBluetooth.Status.Disconnected, null, null)

        fun connectionFailed(exception: Exception) =
            BluetoothResource(ConstantsBluetooth.Status.ConnectionFailed, null, exception)

        fun error(exception: Exception) =
            BluetoothResource(ConstantsBluetooth.Status.Error, null, exception)

        fun read(data: BluetoothResponse) =
            BluetoothResource(ConstantsBluetooth.Status.Read, null, null)
    }
}