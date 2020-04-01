package com.robolancers.lancerscoutkotlin.utilities.fragment

import android.view.WindowManager
import androidx.fragment.app.DialogFragment

abstract class LancerDialogFragment : DialogFragment() {
    override fun onStart() {
        super.onStart()

        val window = dialog?.window
        val windowParams = window?.attributes
        windowParams?.dimAmount = 0f
        windowParams?.flags = windowParams?.flags?.or(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        window?.attributes = windowParams
    }
}