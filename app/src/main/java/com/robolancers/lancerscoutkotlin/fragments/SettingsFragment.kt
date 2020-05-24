package com.robolancers.lancerscoutkotlin.fragments

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.robolancers.lancerscoutkotlin.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
    }
}