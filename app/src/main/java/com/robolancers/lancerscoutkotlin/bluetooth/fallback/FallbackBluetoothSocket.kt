package com.robolancers.lancerscoutkotlin.bluetooth.fallback

import android.bluetooth.BluetoothSocket
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.lang.reflect.Method


class FallbackBluetoothSocket(tmp: BluetoothSocket) : NativeBluetoothSocket(tmp) {
    private var fallbackSocket: BluetoothSocket? = null

    @Throws(IOException::class)
    override fun getInputStream(): InputStream {
        return fallbackSocket!!.inputStream
    }

    @Throws(IOException::class)
    override fun getOutputStream(): OutputStream {
        return fallbackSocket!!.outputStream
    }

    @Throws(IOException::class)
    override fun connect() {
        fallbackSocket!!.connect()
    }

    @Throws(IOException::class)
    override fun close() {
        fallbackSocket!!.close()
    }

    init {
        try {
            val clazz: Class<*> = tmp.remoteDevice.javaClass
            val paramTypes = arrayOf<Class<*>>(Integer.TYPE)
            val m: Method = clazz.getMethod("createRfcommSocket", *paramTypes)
            val params = arrayOf<Any>(Integer.valueOf(1))
            fallbackSocket = m.invoke(tmp.remoteDevice, params) as BluetoothSocket
        } catch (e: Exception) {
            throw FallbackException(
                e
            )
        }
    }
}