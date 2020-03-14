package com.robolancers.lancerscoutkotlin.bluetooth.fallback

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log
import java.io.IOException
import java.util.*

class BluetoothConnector(private val bluetoothDevice: BluetoothDevice, private val secure: Boolean, private val adapter: BluetoothAdapter, private val uuidCandidates: List<UUID>){
    private var candidate: Int = 0
    private lateinit var bluetoothSocket: BluetoothSocketWrapper

    @Throws(IOException::class)
    fun connect(): BluetoothSocketWrapper {
        var success = false
        while (selectSocket()) {
            adapter.cancelDiscovery()

            try {
                bluetoothSocket.connect()
                success = true
                break
            } catch (e: IOException) {
                try {
                    bluetoothSocket =
                        FallbackBluetoothSocket(
                            bluetoothSocket.getUnderlyingSocket()
                        )
                    Thread.sleep(500)
                    bluetoothSocket.connect()
                    success = true
                    break
                } catch (e1: FallbackException) {
                    Log.w("BT", "Could not initialize FallbackBluetoothSocket classes.", e)
                } catch (e1: InterruptedException) {
                    Log.w("BT", e1.message, e1)
                } catch (e1: IOException) {
                    Log.w("BT", "Fallback failed. Cancelling.", e1)
                }
            }
        }

        if (!success) {
            throw IOException("Could not connect to device: ${bluetoothDevice.address}")
        }

        return bluetoothSocket
    }

    @Throws(IOException::class)
    private fun selectSocket(): Boolean {
        if (candidate >= uuidCandidates.size) {
            return false
        }

        val tmp: BluetoothSocket
        val uuid = uuidCandidates[candidate++]

        Log.i("BT", "Attempting to connect to Protocol: $uuid")
        tmp = if (secure) {
            bluetoothDevice.createRfcommSocketToServiceRecord(uuid)
        } else {
            bluetoothDevice.createInsecureRfcommSocketToServiceRecord(uuid)
        }

        bluetoothSocket =
            NativeBluetoothSocket(
                tmp
            )

        return true
    }
}