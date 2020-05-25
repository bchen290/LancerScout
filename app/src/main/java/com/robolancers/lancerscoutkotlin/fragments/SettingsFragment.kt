package com.robolancers.lancerscoutkotlin.fragments

import android.content.Context
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.robolancers.lancerscoutkotlin.R
import com.robolancers.lancerscoutkotlin.utilities.sharedpreference.BluetoothSharedPreference

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)

        val context: Context = this.context ?: return

        val defaultDevicePreference = findPreference<Preference>("default")
        if (BluetoothSharedPreference.getName(context) != "") {
            defaultDevicePreference?.summary =
                "${BluetoothSharedPreference.getName(context)} - ${BluetoothSharedPreference.getMacAddress(
                    context
                )}"
            activity?.onContentChanged()
        }
    }
}