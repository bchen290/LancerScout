package com.robolancers.lancerscoutkotlin.activities

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.robolancers.lancerscoutkotlin.R

abstract class ToolbarActivity : AppCompatActivity() {
    lateinit var toolbar: Toolbar

    fun setupToolbar(resource: Int) {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(resource)
    }
}