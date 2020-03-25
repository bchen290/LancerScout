package com.robolancers.lancerscoutkotlin.activities

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.robolancers.lancerscoutkotlin.R
import com.robolancers.lancerscoutkotlin.fragments.TemplateModelChooserDialogFragment
import com.robolancers.lancerscoutkotlin.models.template.*
import com.robolancers.lancerscoutkotlin.utilities.ItemTouchHelperSimpleCallback
import com.robolancers.lancerscoutkotlin.utilities.LancerDialogFragment
import com.robolancers.lancerscoutkotlin.utilities.adapters.TemplateEditingAdapter
import com.robolancers.lancerscoutkotlin.utilities.ToolbarActivity

class TemplateEditingActivity : ToolbarActivity(), LancerDialogFragment.LancerDialogListener {
    private lateinit var templateEditingRecyclerView: RecyclerView
    private lateinit var templateEditingAdapter: TemplateEditingAdapter<TemplateModel>
    private val templateEditingList = mutableListOf<TemplateModel>()

    private lateinit var templateType: String
    private lateinit var templateName: String

    private val templateEditingHelper by lazy {
        ItemTouchHelper(ItemTouchHelperSimpleCallback(applicationContext, templateEditingAdapter).simpleItemCallback)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_template_editing)

        setupToolbar()

        templateType = intent.getStringExtra("Type")
        templateName = intent.getStringExtra("ItemClicked")

        val templateEditingTitle = findViewById<TextView>(R.id.template_editing_title)
        templateEditingTitle.text = templateName

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val newFragment = TemplateModelChooserDialogFragment()
            newFragment.show(supportFragmentManager, "templateModelChooser")
        }
        
        templateEditingAdapter = TemplateEditingAdapter(this@TemplateEditingActivity, templateEditingList)
        templateEditingRecyclerView = findViewById<RecyclerView>(R.id.template_editing_list).apply {
            layoutManager = LinearLayoutManager(this@TemplateEditingActivity, RecyclerView.VERTICAL, false)
            adapter = templateEditingAdapter
        }
        templateEditingHelper.attachToRecyclerView(templateEditingRecyclerView)
    }

    override fun onDialogClicked(vararg clickedItems: String) {
        when(clickedItems[0]) {
            "Header" -> templateEditingList.add(Header())
            "Note" -> templateEditingList.add(Note())
            "Counter" -> templateEditingList.add(Counter())
            "Item Selector" -> templateEditingList.add(ItemSelector())
            "Stopwatch" -> templateEditingList.add(Stopwatch())
            "Checkbox" -> templateEditingList.add(Checkbox())
        }

        templateEditingAdapter.notifyItemInserted(templateEditingList.size - 1)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.item_editing_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.item_editing_save -> {
                val sharedPreferences = getSharedPreferences(getString(R.string.template_preferences), Context.MODE_PRIVATE)
                with (sharedPreferences.edit()) {
                    putString("$templateType-$templateName", Gson().toJson(templateEditingList))
                    commit()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun startDragging(viewHolder: RecyclerView.ViewHolder) {
        templateEditingHelper.startDrag(viewHolder)
    }
}
