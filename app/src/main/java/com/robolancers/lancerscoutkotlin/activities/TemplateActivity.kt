package com.robolancers.lancerscoutkotlin.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.robolancers.lancerscoutkotlin.R
import com.robolancers.lancerscoutkotlin.adapters.MatchTemplateAdapter
import com.robolancers.lancerscoutkotlin.adapters.PitTemplateAdapter
import com.robolancers.lancerscoutkotlin.fragments.TemplateChooserDialogFragment
import com.robolancers.lancerscoutkotlin.room.MatchTemplate
import com.robolancers.lancerscoutkotlin.utilities.*
import com.robolancers.lancerscoutkotlin.room.MatchTemplateViewModel
import com.robolancers.lancerscoutkotlin.room.PitTemplate
import com.robolancers.lancerscoutkotlin.room.PitTemplateViewModel

class TemplateActivity : ToolbarActivity(), LancerDialogFragment.LancerDialogListener {
    inner class PitTemplateListener : RecyclerViewOnClickListener<String> {
        override fun onItemClicked(itemClicked: String) {
            val intent = Intent(this@TemplateActivity, TemplateEditingActivity::class.java)
            intent.putExtra("Type", "PIT")
            intent.putExtra("ItemClicked", itemClicked)
            this@TemplateActivity.startActivity(intent)
        }
    }

    private lateinit var matchAdapter: MatchTemplateAdapter
    private lateinit var pitAdapter: PitTemplateAdapter

    private val matchItemTouchHelper by lazy {
        ItemTouchHelper(ItemTouchHelperSimpleCallback(applicationContext, matchAdapter).simpleItemCallback)
    }

    private val pitItemTouchHelper by lazy {
        ItemTouchHelper(ItemTouchHelperSimpleCallback(applicationContext, pitAdapter).simpleItemCallback)
    }

    private lateinit var matchTemplateViewModel: MatchTemplateViewModel
    private lateinit var pitTemplateViewModel: PitTemplateViewModel

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
        matchAdapter = MatchTemplateAdapter(this@TemplateActivity)

        val matchTemplateRecyclerView = findViewById<RecyclerView>(R.id.match_template_recycler_view).apply {
            layoutManager = matchTemplateManager
            adapter = matchAdapter
        }
        matchItemTouchHelper.attachToRecyclerView(matchTemplateRecyclerView)

        val pitTemplateManager = LinearLayoutManager(this)
        pitAdapter =
            PitTemplateAdapter(
                this@TemplateActivity
            )
        val pitTemplateRecyclerView = findViewById<RecyclerView>(R.id.pit_template_recycler_view).apply {
            layoutManager = pitTemplateManager
            adapter = pitAdapter
        }
        pitItemTouchHelper.attachToRecyclerView(pitTemplateRecyclerView)

        matchTemplateViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application)).get(MatchTemplateViewModel::class.java)
        pitTemplateViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application)).get(PitTemplateViewModel::class.java)

        matchTemplateViewModel.allMatchTemplates.observe(this, Observer { templates ->
            templates?.let {
                matchAdapter.setTemplates(it)
            }
        })

        pitTemplateViewModel.allPitTemplates.observe(this, Observer { templates ->
            templates.let {
                pitAdapter.setTemplates(it)
            }
        })
    }

    override fun onDialogClicked(vararg clickedItems: String) {
        startActivity(Intent(this, TemplateEditingActivity::class.java).putExtra("Type", clickedItems[0]).putExtra("ItemClicked", ""))
    }

    fun startPitDrag(viewHolder: RecyclerView.ViewHolder) {
        pitItemTouchHelper.startDrag(viewHolder)
    }

    fun startMatchDrag(viewHolder: RecyclerView.ViewHolder) {
        matchItemTouchHelper.startDrag(viewHolder)
    }
}
