package com.robolancers.lancerscoutkotlin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.robolancers.lancerscoutkotlin.activities.SettingsActivity
import com.robolancers.lancerscoutkotlin.activities.scouting.TeamChooserActivity
import com.robolancers.lancerscoutkotlin.activities.template.TemplateActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        template_button.setOnClickListener {
            startActivity(Intent(this, TemplateActivity::class.java))
        }


        scouting_button.setOnClickListener {
            startActivity(Intent(this, TeamChooserActivity::class.java))
        }

        settings_button.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }
}
