package com.robolancers.lancerscoutkotlin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.robolancers.lancerscoutkotlin.activities.TemplateActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val templateButton = findViewById<Button>(R.id.template_button)
        templateButton.setOnClickListener {
            startActivity(Intent(this, TemplateActivity::class.java))
        }
    }
}
