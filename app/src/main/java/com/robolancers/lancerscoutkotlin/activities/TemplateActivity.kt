package com.robolancers.lancerscoutkotlin.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.robolancers.lancerscoutkotlin.R
import com.robolancers.lancerscoutkotlin.fragments.TemplateChooserDialogFragment
import com.robolancers.lancerscoutkotlin.utilities.*
import com.robolancers.lancerscoutkotlin.utilities.adapters.TemplateAdapter

class TemplateActivity : ToolbarActivity(), LancerDialogFragment.LancerDialogListener {
    inner class MatchTemplateListener : RecyclerViewOnClickListener<String> {
        override fun onItemClicked(itemClicked: String) {
            val intent = Intent(this@TemplateActivity, TemplateEditingActivity::class.java)
            intent.putExtra("Type", "MATCH")
            intent.putExtra("ItemClicked", itemClicked)
            this@TemplateActivity.startActivity(intent)
        }
    }

    inner class PitTemplateListener : RecyclerViewOnClickListener<String> {
        override fun onItemClicked(itemClicked: String) {
            val intent = Intent(this@TemplateActivity, TemplateEditingActivity::class.java)
            intent.putExtra("Type", "PIT")
            intent.putExtra("ItemClicked", itemClicked)
            this@TemplateActivity.startActivity(intent)
        }
    }

    private var matchTemplates = mutableListOf<String>()
    private var pitTemplates = mutableListOf<String>()

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

        val sharedPreferences = getSharedPreferences(getString(R.string.template_preferences), Context.MODE_PRIVATE)
        val allSharedPreferences = sharedPreferences.all

        Log.e("TEST", allSharedPreferences.toString())

        for((key, _) in allSharedPreferences) {
            if (key.startsWith("PIT")) {
                pitTemplates.add(key.split("~")[1])
            } else if (key.startsWith("MATCH")) {
                matchTemplates.add(key.split("~")[1])
            }
        }

        val matchTemplateManager = LinearLayoutManager(this)
        matchAdapter =
            TemplateAdapter(
                this@TemplateActivity,
                MatchTemplateListener(),
                matchTemplates,
                false
            )

        val matchTemplateRecyclerView = findViewById<RecyclerView>(R.id.match_template_recycler_view).apply {
            layoutManager = matchTemplateManager
            adapter = matchAdapter
        }
        matchItemTouchHelper.attachToRecyclerView(matchTemplateRecyclerView)

        val pitTemplateManager = LinearLayoutManager(this)
        pitAdapter =
            TemplateAdapter(
                this@TemplateActivity,
                PitTemplateListener(),
                pitTemplates,
                true
            )
        val pitTemplateRecyclerView = findViewById<RecyclerView>(R.id.pit_template_recycler_view).apply {
            layoutManager = pitTemplateManager
            adapter = pitAdapter
        }
        pitItemTouchHelper.attachToRecyclerView(pitTemplateRecyclerView)
    }

    override fun onDialogClicked(vararg clickedItems: String) {
        startActivity(Intent(this, TemplateEditingActivity::class.java).putExtra("Type", clickedItems[0]).putExtra("ItemClicked", ""))
    }

    fun startDragging(viewHolder: RecyclerView.ViewHolder, isPit: Boolean) {
        if(isPit){
            pitItemTouchHelper.startDrag(viewHolder)
        }else{
            matchItemTouchHelper.startDrag(viewHolder)
        }
    }
}
