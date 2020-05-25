package com.robolancers.lancerscoutkotlin.utilities.sharedpreference

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

class BluetoothSharedPreference {
    companion object {
        private const val NAME = "Bluetooth"

        private fun getPreference(context: Context): SharedPreferences {
            return context.getSharedPreferences(NAME, MODE_PRIVATE)
        }

        fun setMacAddress(context: Context, macAddress: String) {
            getPreference(context).edit().putString("MacAddress", macAddress).apply()
        }

        fun getMacAddress(context: Context): String {
            return getPreference(context).getString("MacAddress", "") ?: ""
        }

        fun setName(context: Context, name: String) {
            getPreference(context).edit().putString("Name", name).apply()
        }

        fun getName(context: Context): String {
            return getPreference(context).getString("Name", "") ?: ""
        }
    }
}