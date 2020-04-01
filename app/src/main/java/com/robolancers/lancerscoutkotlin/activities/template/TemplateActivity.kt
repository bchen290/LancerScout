package com.robolancers.lancerscoutkotlin.activities.template

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.robolancers.lancerscoutkotlin.R
import com.robolancers.lancerscoutkotlin.adapters.TemplateAdapter
import com.robolancers.lancerscoutkotlin.room.entities.Template
import com.robolancers.lancerscoutkotlin.room.viewmodels.TemplateViewModel
import com.robolancers.lancerscoutkotlin.utilities.activity.ToolbarActivity
import com.robolancers.lancerscoutkotlin.utilities.callback.ItemTouchHelperSimpleCallbackDeletable
import kotlinx.android.synthetic.main.activity_template.*

class TemplateActivity : ToolbarActivity() {
    private lateinit var templateAdapter: TemplateAdapter

    private lateinit var templateViewModel: TemplateViewModel

    private val templateHelper by lazy {
        ItemTouchHelper(
            ItemTouchHelperSimpleCallbackDeletable(
                applicationContext,
                templateAdapter
            ).simpleItemCallback)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_template)

        setupToolbar()

        fab.setOnClickListener {
            val intent = Intent(this, TemplateEditingActivity::class.java)
            intent.putExtra("Template", Template("", ""))

            startActivity(intent)
        }

        val templateManager = LinearLayoutManager(this)
        templateAdapter = TemplateAdapter(this@TemplateActivity)

        template_recycler_view.apply {
            layoutManager = templateManager
            adapter = templateAdapter
        }.also {
            templateHelper.attachToRecyclerView(it)
        }

        templateViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application)).get(
            TemplateViewModel::class.java)

        templateViewModel.allTemplates.observe(this, Observer { templates ->
            templateAdapter.setTemplates(templates)
        })
    }
}
