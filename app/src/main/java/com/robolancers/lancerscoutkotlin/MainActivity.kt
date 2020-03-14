package com.robolancers.lancerscoutkotlin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.robolancers.lancerscoutkotlin.activities.TemplateActivity
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val templateButton = findViewById<Button>(R.id.template_button)
        templateButton.setOnClickListener {
            startActivity(Intent(this, TemplateActivity::class.java))
        }
    }

    companion object {
        val APP_BLUETOOTH_UUID = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66")
        val DEFAULT_BLUETOOTH_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    }
}
