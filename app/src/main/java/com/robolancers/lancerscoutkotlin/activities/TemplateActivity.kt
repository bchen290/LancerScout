package com.robolancers.lancerscoutkotlin.activities

import android.os.Bundle
import com.robolancers.lancerscoutkotlin.R

class TemplateActivity : ToolbarActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_template)

        setupToolbar(R.string.templates)
    }
}
