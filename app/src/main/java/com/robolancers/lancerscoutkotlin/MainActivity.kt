package com.robolancers.lancerscoutkotlin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    companion object {
        val APP_BLUETOOTH_UUID = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66")
        val DEFAULT_BLUETOOTH_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    }
}
