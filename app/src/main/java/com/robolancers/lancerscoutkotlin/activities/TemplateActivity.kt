package com.robolancers.lancerscoutkotlin.activities

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.robolancers.lancerscoutkotlin.R
import com.robolancers.lancerscoutkotlin.fragments.TemplateChooserDialogFragment
import com.robolancers.lancerscoutkotlin.utilities.ToolbarActivity

class TemplateActivity : ToolbarActivity(), TemplateChooserDialogFragment.TemplateChooserListener {
    private var templateClicked = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_template)

        setupToolbar()

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val newFragment = TemplateChooserDialogFragment()
            newFragment.show(supportFragmentManager, "templates")
        }
    }

    override fun onClick(clickedItem: String) {
        templateClicked = clickedItem
    }
}
