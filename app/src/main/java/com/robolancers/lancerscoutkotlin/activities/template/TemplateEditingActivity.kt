package com.robolancers.lancerscoutkotlin.activities.template

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.robolancers.lancerscoutkotlin.R
import com.robolancers.lancerscoutkotlin.fragments.TemplateModelChooserDialogFragment
import com.robolancers.lancerscoutkotlin.models.template.*
import com.robolancers.lancerscoutkotlin.adapters.TemplateEditingAdapter
import com.github.salomonbrys.kotson.*
import com.google.android.material.snackbar.Snackbar
import com.robolancers.lancerscoutkotlin.room.entities.Template
import com.robolancers.lancerscoutkotlin.room.viewmodels.TemplateViewModel
import com.robolancers.lancerscoutkotlin.utilities.GsonHelper.Companion.gson
import com.robolancers.lancerscoutkotlin.utilities.activity.ToolbarActivity
import com.robolancers.lancerscoutkotlin.utilities.callback.ItemTouchHelperSimpleCallback
import com.robolancers.lancerscoutkotlin.utilities.enums.TemplateModelType
import com.robolancers.lancerscoutkotlin.utilities.enums.TemplateModelType.*

class TemplateEditingActivity : ToolbarActivity(), TemplateModelChooserDialogFragment.TemplateModelChooserDialogListener {
    private lateinit var parentLayout: LinearLayout
    private lateinit var templateEditingTitle: EditText

    private lateinit var templateEditingRecyclerView: RecyclerView
    private lateinit var templateEditingAdapter: TemplateEditingAdapter<TemplateModel>
    private var templateEditingList = mutableListOf<TemplateModel>()

    private var template: Template? = null
    private var templateName: String? = ""

    private lateinit var templateViewModel: TemplateViewModel

    private val templateEditingHelper by lazy {
        ItemTouchHelper(
            ItemTouchHelperSimpleCallback(
                applicationContext,
                templateEditingAdapter
            ).simpleItemCallback)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_template_editing)

        setupToolbar()

        parentLayout = findViewById(R.id.template_editing_linear_layout)

        template = intent.getParcelableExtra("Template")
        templateName = template?.name

        val templateData: String? = template?.data
        if (templateData != null && templateData != "") {
            templateEditingList = gson.fromJson(templateData)
        }

        templateEditingTitle = findViewById(R.id.template_editing_title)
        templateEditingTitle.setText(templateName)
        
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

        templateViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application)).get(
            TemplateViewModel::class.java)
    }

    override fun onDialogClicked(clickedItem: TemplateModelType) {
        when(clickedItem) {
            HEADER -> templateEditingList.add(Header())
            NOTE -> templateEditingList.add(Note())
            COUNTER -> templateEditingList.add(Counter())
            ITEM_SELECTOR -> templateEditingList.add(ItemSelector())
            STOPWATCH -> templateEditingList.add(Stopwatch())
            CHECKBOX -> templateEditingList.add(Checkbox())
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

    override fun onStop() {
        super.onStop()
        save()
    }

    fun startDragging(viewHolder: RecyclerView.ViewHolder) {
        templateEditingHelper.startDrag(viewHolder)
    }

    private fun save() {
        val json = gson.toJson(templateEditingList, gsonTypeToken<MutableList<TemplateModel>>())
        templateName = templateEditingTitle.text.toString()

        val templateCopy = template
        if (templateCopy != null) {
            templateCopy.data = json
            templateCopy.name = templateName

            templateViewModel.insert(templateCopy)
        }

        Snackbar.make(parentLayout, "Saved", Snackbar.LENGTH_LONG).show()
    }
}
