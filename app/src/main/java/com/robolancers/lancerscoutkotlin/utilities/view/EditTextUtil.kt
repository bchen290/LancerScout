package com.robolancers.lancerscoutkotlin.utilities.view

import android.widget.EditText

class EditTextUtil {
    companion object {
        fun disableEditText(editText: EditText) {
            editText.isEnabled = false
        }
    }
}