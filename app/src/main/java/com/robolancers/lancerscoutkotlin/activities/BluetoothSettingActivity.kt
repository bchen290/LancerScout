package com.robolancers.lancerscoutkotlin.activities

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.robolancers.lancerscoutkotlin.R
import kotlin.system.exitProcess

class BluetoothSettingActivity : AppCompatActivity() {
    lateinit var materialDialog: MaterialDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
    }

    override fun onResume() {
        super.onResume()

        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        if (bluetoothAdapter == null) {
            MaterialDialog(this).show {
                positiveButton(text = "Exit") {
                    exitProcess(0)
                }
            }
        } else if (bluetoothAdapter.isEnabled) {
            val devices = bluetoothAdapter.bondedDevices.map { it.name }.toMutableList()
            devices.add("Not on screen")

            materialDialog = MaterialDialog(this).show {
                listItemsSingleChoice(items = devices) { _, _, text ->
                    when (text) {
                        "Not on screen" -> {
                            val intent = Intent()
                            intent.action = android.provider.Settings.ACTION_BLUETOOTH_SETTINGS
                            startActivity(intent)
                        }
                        else -> {

                        }
                    }
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        materialDialog.dismiss()
    }
}