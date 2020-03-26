package com.robolancers.lancerscoutkotlin.activities

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.robolancers.lancerscoutkotlin.R
import com.robolancers.lancerscoutkotlin.fragments.TemplateModelChooserDialogFragment
import com.robolancers.lancerscoutkotlin.models.template.*
import com.robolancers.lancerscoutkotlin.utilities.adapters.TemplateEditingAdapter
import com.github.salomonbrys.kotson.*
import com.robolancers.lancerscoutkotlin.utilities.*
import com.robolancers.lancerscoutkotlin.utilities.Util.Companion.gson

class TemplateEditingActivity : ToolbarActivity(), LancerDialogFragment.LancerDialogListener {
    private lateinit var templateEditingRecyclerView: RecyclerView
    private lateinit var templateEditingAdapter: TemplateEditingAdapter<TemplateModel>
    private var templateEditingList = mutableListOf<TemplateModel>()

    private lateinit var templateType: String
    private lateinit var templateName: String

    private val templateEditingHelper by lazy {
        ItemTouchHelper(ItemTouchHelperSimpleCallback(applicationContext, templateEditingAdapter).simpleItemCallback)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_template_editing)

        setupToolbar()

        templateType = intent.getStringExtra("Type")
        templateName = intent.getStringExtra("ItemClicked")

        val templateEditingTitle = findViewById<EditText>(R.id.template_editing_title)
        templateEditingTitle.setText(templateName)

        templateEditingTitle.setOnFocusChangeListener { _, hasFocus ->
            if(!hasFocus) {
                templateName = templateEditingTitle.text.toString()
            }
        }

        val sharedPreferences = getSharedPreferences(getString(R.string.template_preferences), Context.MODE_PRIVATE)

        if (sharedPreferences.getString("$templateType-$templateName", "") != "") {
            templateEditingList = gson.fromJson(sharedPreferences.getString("$templateType-$templateName", "")!!)
        }
        
        templateEditingAdapter = TemplateEditingAdapter(this@TemplateEditingActivity, templateEditingList)
        templateEditingRecyclerView = findViewById<RecyclerView>(R.id.template_editing_list).apply {
            layoutManager = LinearLayoutManager(this@TemplateEditingActivity, RecyclerView.VERTICAL, false)
            adapter = templateEditingAdapter
        }

        templateEditingRecyclerView.setOnTouchListener { v, _ ->
            (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(v.windowToken, 0)
            false
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
                save()
                true
            }
            R.id.item_editing_add -> {
                val newFragment = TemplateModelChooserDialogFragment()
                newFragment.show(supportFragmentManager, "templateModelChooser")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun startDragging(viewHolder: RecyclerView.ViewHolder) {
        templateEditingHelper.startDrag(viewHolder)
    }

    private fun save() {
        val sharedPreferences = getSharedPreferences(getString(R.string.template_preferences), Context.MODE_PRIVATE)
        with (sharedPreferences.edit()) {
            putString("$templateType-$templateName", gson.toJson(templateEditingList, gsonTypeToken<MutableList<TemplateModel>>()))
            commit()
        }
    }
}
