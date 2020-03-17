package com.robolancers.lancerscoutkotlin.fragments

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.robolancers.lancerscoutkotlin.utilities.LancerDialogFragment

class TemplateModelChooserDialogFragment : LancerDialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val arrayItem: Array<String> = arrayOf("Checkbox", "Counter", "Header", "Item Selector", "Note", "Stopwatch")
            builder.setTitle("Select the type of template")
                .setItems(arrayItem) { _, which ->
                    listener.onDialogClicked(arrayItem[which])
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}