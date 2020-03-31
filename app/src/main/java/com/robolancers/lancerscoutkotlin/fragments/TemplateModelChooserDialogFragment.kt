package com.robolancers.lancerscoutkotlin.fragments

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.robolancers.lancerscoutkotlin.utilities.LancerDialogFragment
import com.robolancers.lancerscoutkotlin.utilities.enums.TemplateModelType

class TemplateModelChooserDialogFragment : LancerDialogFragment() {
    lateinit var listener: TemplateModelChooserDialogListener

    interface TemplateModelChooserDialogListener {
        fun onDialogClicked(clickedItem: TemplateModelType)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        listener = context as TemplateModelChooserDialogListener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val arrayItem: Array<String> = TemplateModelType.values().map { templateType ->
                templateType.toString()
            }.toTypedArray()

            builder.setTitle("Select the type of template")
                .setItems(arrayItem) { _, which ->
                    listener.onDialogClicked(TemplateModelType.values()[which])
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}