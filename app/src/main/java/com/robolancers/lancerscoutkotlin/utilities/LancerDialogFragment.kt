package com.robolancers.lancerscoutkotlin.utilities

import android.content.Context
import android.view.WindowManager
import androidx.fragment.app.DialogFragment

abstract class LancerDialogFragment : DialogFragment() {
    lateinit var listener: LancerDialogListener

    interface LancerDialogListener {
        fun onClick(vararg clickedItems: String)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        listener = context as LancerDialogListener
    }

    override fun onStart() {
        super.onStart()

        val window = dialog?.window
        val windowParams = window?.attributes
        windowParams?.dimAmount = 0f
        windowParams?.flags = windowParams?.flags?.or(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        window?.attributes = windowParams
    }
}