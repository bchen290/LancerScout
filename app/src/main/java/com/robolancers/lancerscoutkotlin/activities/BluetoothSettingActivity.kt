package com.robolancers.lancerscoutkotlin.activities

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.robolancers.lancerscoutkotlin.R
import com.robolancers.lancerscoutkotlin.utilities.sharedpreference.BluetoothSharedPreference

class BluetoothSettingActivity : AppCompatActivity() {
    lateinit var materialDialog: MaterialDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth_setting)
    }

    override fun onResume() {
        super.onResume()

        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        if (bluetoothAdapter == null) {
            MaterialDialog(this).show {
                positiveButton(text = "Exit") {
                    finish()
                }
            }
        } else if (bluetoothAdapter.isEnabled) {
            val devices = bluetoothAdapter.bondedDevices.toMutableList()
            val deviceNames = devices.map { it.name }.toMutableList()
            deviceNames.add("Not on screen")

            materialDialog = MaterialDialog(this).show {
                listItemsSingleChoice(items = deviceNames) { _, index, text ->
                    when (text) {
                        "Not on screen" -> {
                            val intent = Intent()
                            intent.action = android.provider.Settings.ACTION_BLUETOOTH_SETTINGS
                            startActivity(intent)
                        }
                        else -> {
                            BluetoothSharedPreference.setName(
                                this@BluetoothSettingActivity,
                                devices[index].name
                            )
                            BluetoothSharedPreference.setMacAddress(
                                this@BluetoothSettingActivity,
                                devices[index].address
                            )
                            Toast.makeText(
                                this@BluetoothSettingActivity,
                                "Default device set to ${devices[index].name}",
                                Toast.LENGTH_LONG
                            ).show()

                            startActivity(
                                Intent(
                                    this@BluetoothSettingActivity,
                                    SettingsActivity::class.java
                                )
                            )
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