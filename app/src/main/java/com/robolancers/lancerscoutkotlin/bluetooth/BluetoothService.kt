package com.robolancers.lancerscoutkotlin.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.robolancers.lancerscoutkotlin.utilities.Constants
import com.robolancers.lancerscoutkotlin.utilities.Util
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*

class BluetoothService(val context: Context, val handler: Handler){
    private var adapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    private var connectThread: ConnectThread? = null
    private var connectedThread: ConnectedThread? = null

    private var serviceState = STATE_NONE
        @Synchronized get

    companion object {
        const val STATE_NONE = 0
        const val STATE_CONNECTING = 2
        const val STATE_CONNECTED = 3

        val APP_BLUETOOTH_UUID: UUID = UUID.fromString("981e6604-5feb-4403-82a5-ba5822119997")
        val DEFAULT_BLUETOOTH_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

        const val TAG = "BT"
    }

    @Synchronized
    fun start() {
        Log.d(TAG, "start")

        connectedThread?.cancel()
        connectedThread = null

        connectThread?.cancel()
        connectThread = null
    }

    @Synchronized
    fun connect(device: BluetoothDevice, secure: Boolean) {
        Log.d(TAG, "connect to: $device")

        if (serviceState == STATE_CONNECTING) {
            connectThread?.cancel()
            connectThread = null
        }

        connectedThread?.cancel()
        connectedThread = null

        connectThread = ConnectThread(device, secure)
        connectThread!!.start()
    }

    @Synchronized
    fun connected(socket: BluetoothSocket?, device: BluetoothDevice, socketType: String) {
        Log.d(TAG, "connected, Socket Type: $socketType")

        connectThread?.cancel()
        connectThread = null

        connectedThread?.cancel()
        connectedThread = null

        connectedThread = ConnectedThread(socket, socketType)
        connectedThread!!.start()

        val msg = handler.obtainMessage(Constants.MESSAGE_DEVICE_NAME)
        val bundle = Bundle()
        bundle.putString(Constants.DEVICE_NAME, device.name)
        msg.data = bundle
        handler.sendMessage(msg)
    }

    @Synchronized
    fun stop() {
        Log.d(TAG, "stop")

        connectThread?.cancel()
        connectThread = null

        connectedThread?.cancel()
        connectedThread = null

        serviceState = STATE_NONE
    }

    fun write(out: ByteArray) {
        val tempConnectedThread: ConnectedThread?

        synchronized(this) {
            if (serviceState != STATE_CONNECTED) return
            tempConnectedThread = connectedThread
        }

        tempConnectedThread?.write(out)
    }

    fun connectionFailed() {
        val msg = handler.obtainMessage(Constants.MESSAGE_TOAST)
        val bundle = Bundle()
        bundle.putString(Constants.TOAST, "Unable to connect device")
        msg.data = bundle
        handler.sendMessage(msg)

        serviceState = STATE_NONE

        this@BluetoothService.start()
    }

    fun connectionLost() {
        val msg = handler.obtainMessage(Constants.MESSAGE_TOAST)
        val bundle = Bundle()
        bundle.putString(Constants.TOAST, "Device connection was lost")
        msg.data = bundle
        handler.sendMessage(msg)

        serviceState = STATE_NONE
        this@BluetoothService.start()
    }

    private inner class ConnectThread(val device: BluetoothDevice, secure: Boolean): Thread() {
        var socket: BluetoothSocket?
        var socketType: String

        init {
            var tmp: BluetoothSocket? = null
            socketType = if (secure) "Secure" else "Insecure"

            try {
                tmp = if(secure) {
                    device.createRfcommSocketToServiceRecord(APP_BLUETOOTH_UUID)
                } else {
                    device.createInsecureRfcommSocketToServiceRecord(APP_BLUETOOTH_UUID)
                }
            } catch (e: IOException) {
                Log.e(TAG, "Socket Type: $")
            }

            socket = tmp
            serviceState = STATE_CONNECTING
        }

        override fun run() {
            adapter.cancelDiscovery()

            try {
                socket?.connect()
            } catch (e: IOException) {
                try {
                    socket?.close()
                } catch (e: IOException) {
                    Log.e(TAG, "unable to close() $socketType socket during connection failure", e)
                }
                connectionFailed()
                return
            }

            synchronized(this@BluetoothService) {
                connectThread = null
            }

            connected(socket, device, socketType)
        }

        fun cancel() {
            try {
                socket?.close()
            } catch (e: IOException) {
                Log.e(TAG, "close() of connect $socketType failed", e)
            }
        }
    }

    private inner class ConnectedThread(val socket: BluetoothSocket?, var socketType: String): Thread() {
        private var inputStream: InputStream? = null
        private var outputStream: OutputStream? = null

        init {
            var tmpIn: InputStream? = null
            var tmpOut: OutputStream? = null

            try {
                tmpIn = socket?.inputStream
                tmpOut = socket?.outputStream
            } catch (e: IOException) {
                Log.e(TAG, "temp sockets not created", e)
            }

            inputStream = tmpIn
            outputStream = tmpOut
            serviceState = STATE_CONNECTED
        }

        override fun run() {
            Log.i(TAG, "BEGIN connected thread")
            val buffer = ByteArray(1024)
            var bytes: Int?

            while (serviceState == STATE_CONNECTED) {
                try {
                    bytes = inputStream?.read(buffer)
                    handler.obtainMessage(Constants.MESSAGE_READ, bytes!!, -1, buffer).sendToTarget()
                } catch (e: IOException) {
                    Log.e(TAG, "disconnected", e)
                    connectionLost()
                    break
                }
            }
        }

        fun write(buffer: ByteArray){
            try {
                outputStream?.write(buffer)
                handler.obtainMessage(Constants.MESSAGE_WRITE, -1, -1, buffer)
            } catch (e: IOException) {
                Log.e(TAG, "Exception during write", e)
            }
        }

        fun cancel() {
            try {
                socket?.close()
            } catch (e: IOException) {
                Log.e(TAG, "close() of connect socket failed", e)
            }
        }
    }
}