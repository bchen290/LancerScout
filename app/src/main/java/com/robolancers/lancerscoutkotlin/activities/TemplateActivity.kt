package com.robolancers.lancerscoutkotlin.activities

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.robolancers.lancerscoutkotlin.R
import com.robolancers.lancerscoutkotlin.fragments.TemplateChooserDialogFragment
import com.robolancers.lancerscoutkotlin.utilities.*

class TemplateActivity : ToolbarActivity(), LancerDialogFragment.LancerDialogListener {
    inner class MatchTemplateListener : RecyclerViewOnClickListener<String> {
        override fun onItemClicked(itemClicked: String) {
            val intent = Intent(this@TemplateActivity, TemplateEditingActivity::class.java)
            intent.putExtra("Type", "Match")
            intent.putExtra("ItemClicked", itemClicked)
            this@TemplateActivity.startActivity(intent)
        }
    }

    inner class PitTemplateListener : RecyclerViewOnClickListener<String> {
        override fun onItemClicked(itemClicked: String) {
            val intent = Intent(this@TemplateActivity, TemplateEditingActivity::class.java)
            intent.putExtra("Type", "Pit")
            intent.putExtra("ItemClicked", itemClicked)
            this@TemplateActivity.startActivity(intent)
        }
    }

    private var templateClicked = ""
    private var matchTemplates = mutableListOf("Default", "Match 1")
    private var pitTemplates = mutableListOf("Default", "TEST")

    private lateinit var matchAdapter: TemplateAdapter<String>
    private lateinit var pitAdapter: TemplateAdapter<String>

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
        matchAdapter = TemplateAdapter(this@TemplateActivity, MatchTemplateListener(), matchTemplates, false)
        val matchTemplateRecyclerView = findViewById<RecyclerView>(R.id.match_template_recycler_view).apply {
            layoutManager = matchTemplateManager
            adapter = matchAdapter
        }
        matchItemTouchHelper.attachToRecyclerView(matchTemplateRecyclerView)

        val pitTemplateManager = LinearLayoutManager(this)
        pitAdapter = TemplateAdapter(this@TemplateActivity, PitTemplateListener(), pitTemplates, true)
        val pitTemplateRecyclerView = findViewById<RecyclerView>(R.id.pit_template_recycler_view).apply {
            layoutManager = pitTemplateManager
            adapter = pitAdapter
        }
        pitItemTouchHelper.attachToRecyclerView(pitTemplateRecyclerView)
    }

    override fun onDialogClicked(vararg clickedItems: String) {
        templateClicked = clickedItems[0]
    }

    fun startDragging(viewHolder: RecyclerView.ViewHolder, isPit: Boolean) {
        if(isPit){
            pitItemTouchHelper.startDrag(viewHolder)
        }else{
            matchItemTouchHelper.startDrag(viewHolder)
        }
    }
}
