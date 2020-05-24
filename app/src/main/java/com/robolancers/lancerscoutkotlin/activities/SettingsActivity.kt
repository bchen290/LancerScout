package com.robolancers.lancerscoutkotlin.activities

import android.os.Bundle
import com.robolancers.lancerscoutkotlin.R
import com.robolancers.lancerscoutkotlin.fragments.SettingsFragment
import com.robolancers.lancerscoutkotlin.utilities.activity.ToolbarActivity

class SettingsActivity : ToolbarActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        setupToolbar()

        supportFragmentManager.beginTransaction()
            .replace(R.id.settings_container, SettingsFragment()).commit()
    }
}