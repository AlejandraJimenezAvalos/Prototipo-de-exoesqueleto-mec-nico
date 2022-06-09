package com.example.exoesqueletov1.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.*
import androidx.core.app.NotificationCompat
import com.example.exoesqueletov1.R
import java.io.IOException
import java.util.*


class SerialService : Service(), SerialListener {

    internal class SerialBinder : Binder() {
        val service: SerialService get() = this@SerialService
    }

    private class QueueItem(
        var type: ConstantsBluetooth.QueueType,
        var data: ByteArray?,
        var e: Exception?
    )

    private var mainLooper: Handler = Handler(Looper.getMainLooper())
    private var binder: IBinder = SerialBinder()
    private var queue1: Queue<QueueItem> = LinkedList()
    private var queue2: Queue<QueueItem> = LinkedList()

    private var socket: SerialSocket? = null
    private var listener: SerialListener? = null
    private var connected = false

    override fun onDestroy() {
        cancelNotification()
        disconnect()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    /**
     * Api
     */
    @Throws(IOException::class)
    fun connect(socket: SerialSocket) {
        socket.connect(this)
        this.socket = socket
        connected = true
    }

    fun disconnect() {
        connected = false // ignore data,errors while disconnecting
        cancelNotification()
        if (socket != null) {
            socket!!.disconnect()
            socket = null
        }
    }

    @Throws(IOException::class)
    fun write(data: ByteArray?) {
        if (!connected) throw IOException("not connected")
        socket!!.write(data)
    }

    fun attach(listener: SerialListener) {
        require(!(Looper.getMainLooper().thread !== Thread.currentThread())) { "not in main thread" }
        cancelNotification()
        // use synchronized() to prevent new items in queue2
        // new items will not be added to queue1 because mainLooper.post and attach() run in main thread
        synchronized(this) { this.listener = listener }
        for (item in queue1) {
            when (item.type) {
                ConstantsBluetooth.QueueType.Connect -> listener.onSerialConnect()
                ConstantsBluetooth.QueueType.ConnectError -> listener.onSerialConnectError(item.e)
                ConstantsBluetooth.QueueType.Read -> listener.onSerialRead(item.data)
                ConstantsBluetooth.QueueType.IoError -> listener.onSerialIoError(item.e)
            }
        }
        for (item in queue2) {
            when (item.type) {
                ConstantsBluetooth.QueueType.Connect -> listener.onSerialConnect()
                ConstantsBluetooth.QueueType.ConnectError -> listener.onSerialConnectError(item.e)
                ConstantsBluetooth.QueueType.Read -> listener.onSerialRead(item.data)
                ConstantsBluetooth.QueueType.IoError -> listener.onSerialIoError(item.e)
            }
        }
        queue1.clear()
        queue2.clear()
    }

    fun detach() {
        if (connected) createNotification()
        // items already in event queue (posted before detach() to mainLooper) will end up in queue1
        // items occurring later, will be moved directly to queue2
        // detach() and mainLooper.post run in the main thread, so all items are caught
        listener = null
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun createNotification() {
        val nc = NotificationChannel(
            ConstantsBluetooth.NOTIFICATION_CHANNEL,
            "Background service",
            NotificationManager.IMPORTANCE_LOW
        )
        nc.setShowBadge(false)
        val nm = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        nm.createNotificationChannel(nc)
        val disconnectIntent = Intent()
            .setAction(ConstantsBluetooth.INTENT_ACTION_DISCONNECT)
        val restartIntent = Intent()
            .setClassName(this, ConstantsBluetooth.INTENT_CLASS_MAIN_ACTIVITY)
            .setAction(Intent.ACTION_MAIN)
            .addCategory(Intent.CATEGORY_LAUNCHER)
        val disconnectPendingIntent =
            PendingIntent.getBroadcast(this, 1, disconnectIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val restartPendingIntent =
            PendingIntent.getActivity(this, 1, restartIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val builder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, ConstantsBluetooth.NOTIFICATION_CHANNEL)
                .setSmallIcon(R.drawable.ic_notification)
                .setColor(resources.getColor(R.color.pinkDark, null))
                .setContentTitle(resources.getString(R.string.app_name))
                .setContentText(if (socket != null) "Connected to " + socket!!.getName() else "Background Service")
                .setContentIntent(restartPendingIntent)
                .setOngoing(true)
                .addAction(
                    NotificationCompat.Action(
                        R.drawable.ic_clear,
                        "Disconnect",
                        disconnectPendingIntent
                    )
                )
        // @drawable/ic_notification created with Android Studio -> New -> Image Asset using @color/colorPrimaryDark as background color
        // Android < API 21 does not support vectorDrawables in notifications, so both drawables used here, are created as .png instead of .xml
        val notification = builder.build()
        startForeground(ConstantsBluetooth.NOTIFY_MANAGER_START_FOREGROUND_SERVICE, notification)
    }

    private fun cancelNotification() {
        stopForeground(true)
    }

    /**
     * SerialListener
     */
    override fun onSerialConnect() {
        if (connected) {
            synchronized(this) {
                if (listener != null) {
                    mainLooper.post {
                        if (listener != null) {
                            listener!!.onSerialConnect()
                        } else {
                            queue1.add(QueueItem(ConstantsBluetooth.QueueType.Connect, null, null))
                        }
                    }
                } else {
                    queue2.add(QueueItem(ConstantsBluetooth.QueueType.Connect, null, null))
                }
            }
        }
    }

    override fun onSerialConnectError(e: Exception?) {
        if (connected) {
            synchronized(this) {
                if (listener != null) {
                    mainLooper.post {
                        if (listener != null) {
                            listener!!.onSerialConnectError(e)
                        } else {
                            queue1.add(
                                QueueItem(
                                    ConstantsBluetooth.QueueType.ConnectError,
                                    null,
                                    e
                                )
                            )
                            cancelNotification()
                            disconnect()
                        }
                    }
                } else {
                    queue2.add(QueueItem(ConstantsBluetooth.QueueType.ConnectError, null, e))
                    cancelNotification()
                    disconnect()
                }
            }
        }
    }

    override fun onSerialRead(data: ByteArray?) {
        if (connected) {
            synchronized(this) {
                if (listener != null) {
                    mainLooper.post {
                        if (listener != null) {
                            listener!!.onSerialRead(data)
                        } else {
                            queue1.add(QueueItem(ConstantsBluetooth.QueueType.Read, data, null))
                        }
                    }
                } else {
                    queue2.add(QueueItem(ConstantsBluetooth.QueueType.Read, data, null))
                }
            }
        }
    }

    override fun onSerialIoError(e: Exception?) {
        if (connected) {
            synchronized(this) {
                if (listener != null) {
                    mainLooper.post {
                        if (listener != null) {
                            listener!!.onSerialIoError(e)
                        } else {
                            queue1.add(QueueItem(ConstantsBluetooth.QueueType.IoError, null, e))
                            cancelNotification()
                            disconnect()
                        }
                    }
                } else {
                    queue2.add(QueueItem(ConstantsBluetooth.QueueType.IoError, null, e))
                    cancelNotification()
                    disconnect()
                }
            }
        }
    }
}