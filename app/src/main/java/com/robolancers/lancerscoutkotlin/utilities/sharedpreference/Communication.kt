package com.robolancers.lancerscoutkotlin.utilities.sharedpreference

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

class Communication {
    companion object {
        private const val NAME = "Communication"

        fun getPreference(context: Context): SharedPreferences {
            return context.getSharedPreferences(NAME, MODE_PRIVATE)
        }
    }
}