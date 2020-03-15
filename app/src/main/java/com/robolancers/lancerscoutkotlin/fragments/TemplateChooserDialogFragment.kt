package com.robolancers.lancerscoutkotlin.fragments

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.robolancers.lancerscoutkotlin.R

class TemplateChooserDialogFragment : DialogFragment() {
    private lateinit var listener: TemplateChooserListener

    interface TemplateChooserListener {
        fun onClick(clickedItem: String)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        listener = context as TemplateChooserListener
    }

    override fun onStart() {
        super.onStart()

        val window = dialog?.window
        val windowParams = window?.attributes
        windowParams?.dimAmount = 0f
        windowParams?.flags = windowParams?.flags?.or(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        window?.attributes = windowParams
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val alertDialog = activity?.let {
            val builder = AlertDialog.Builder(it)
            val arrayItem: Array<String> = arrayOf("Match Template", "Pit Template")
            builder.setTitle("Select the type of template")
                .setItems(arrayItem) { _, which ->
                listener.onClick(arrayItem[which])
            }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")

        return alertDialog
    }
}