package com.robolancers.lancerscoutkotlin.bluetooth.fallback

import android.bluetooth.BluetoothSocket
import com.robolancers.lancerscoutkotlin.bluetooth.fallback.BluetoothSocketWrapper
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream


open class NativeBluetoothSocket(private val socket: BluetoothSocket) :
    BluetoothSocketWrapper {
    @Throws(IOException::class)
    override fun getInputStream(): InputStream {
        return socket.inputStream
    }

    @Throws(IOException::class)
    override fun getOutputStream(): OutputStream {
        return socket.outputStream
    }

    override fun getRemoteDeviceName(): String {
        return socket.remoteDevice.name
    }

    @Throws(IOException::class)
    override fun connect() {
        socket.connect()
    }

    override fun getRemoteDeviceAddress(): String {
        return socket.remoteDevice.address
    }

    @Throws(IOException::class)
    override fun close() {
        socket.close()
    }

    override fun getUnderlyingSocket(): BluetoothSocket {
        return socket
    }
}