package com.robolancers.lancerscoutkotlin.utilities

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Button
import com.robolancers.lancerscoutkotlin.activities.TemplateEditingActivity

class StopwatchThread(val stopwatchButton: Button, val context: Context) : Thread() {
    private var startTime: Long = 0
    var difference: Long = 0
    var minutes: Int = 0
    var cancel: Boolean = false
    val mainHandler = Handler(Looper.getMainLooper())

    @SuppressLint("SetTextI18n")
    override fun run() {
        startTime = System.currentTimeMillis()

        mainHandler.post(object : Runnable {
            override fun run() {
                (context as TemplateEditingActivity).runOnUiThread {
                    stopwatchButton.text = "Stop " + "%02d:%02d".format(minutes, difference)
                }

                mainHandler.postDelayed(this, 1000)
            }
        })

        while (!cancel) {
            difference = (System.currentTimeMillis() - startTime) / 1000

            minutes = (difference / 60).toInt()
            difference %= 60
        }

        mainHandler.removeCallbacksAndMessages(null)
        (context as TemplateEditingActivity).runOnUiThread {
            stopwatchButton.text = "Start " + "%02d:%02d".format(minutes, difference)
        }
    }
}