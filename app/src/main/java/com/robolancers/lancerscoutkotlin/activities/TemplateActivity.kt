package com.robolancers.lancerscoutkotlin.activities

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.robolancers.lancerscoutkotlin.R
import com.robolancers.lancerscoutkotlin.fragments.TemplateChooserDialogFragment
import com.robolancers.lancerscoutkotlin.utilities.ItemTouchHelperSimpleCallback
import com.robolancers.lancerscoutkotlin.utilities.TemplateAdapter
import com.robolancers.lancerscoutkotlin.utilities.ToolbarActivity
import kotlinx.android.synthetic.main.activity_template.*

class TemplateActivity : ToolbarActivity(), TemplateChooserDialogFragment.TemplateChooserListener {
    private var templateClicked = ""
    private var matchTemplates = mutableListOf("Default", "Match 1")
    private var pitTemplates = mutableListOf("Default", "TEST")

    private lateinit var matchAdapter: TemplateAdapter
    private lateinit var pitAdapter: TemplateAdapter

    private val matchItemTouchHelper by lazy {
        ItemTouchHelper(ItemTouchHelperSimpleCallback(applicationContext, matchAdapter).simpleItemCallback)
    }

    private val pitItemTouchHelper by lazy {
        ItemTouchHelper(ItemTouchHelperSimpleCallback(applicationContext, pitAdapter).simpleItemCallback)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_template)

        setupToolbar()

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val newFragment = TemplateChooserDialogFragment()
            newFragment.show(supportFragmentManager, "templates")
        }

        val matchTemplateManager = LinearLayoutManager(this)
        matchAdapter = TemplateAdapter(this@TemplateActivity, matchTemplates, false)
        val matchTemplateRecyclerView = findViewById<RecyclerView>(R.id.match_template_recycler_view).apply {
            layoutManager = matchTemplateManager
            adapter = matchAdapter
        }
        matchItemTouchHelper.attachToRecyclerView(matchTemplateRecyclerView)

        val pitTemplateManager = LinearLayoutManager(this)
        pitAdapter = TemplateAdapter(this@TemplateActivity, pitTemplates, true)
        val pitTemplateRecyclerView = findViewById<RecyclerView>(R.id.pit_template_recycler_view).apply {
            layoutManager = pitTemplateManager
            adapter = pitAdapter
        }
        pitItemTouchHelper.attachToRecyclerView(pitTemplateRecyclerView)
    }

    override fun onClick(clickedItem: String) {
        templateClicked = clickedItem
    }

    fun startDragging(viewHolder: RecyclerView.ViewHolder, isPit: Boolean) {
        if(isPit){
            pitItemTouchHelper.startDrag(viewHolder)
        }else{
            matchItemTouchHelper.startDrag(viewHolder)
        }

    }
}
