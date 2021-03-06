package com.robolancers.lancerscoutkotlin.activities.template

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.list.listItems
import com.github.salomonbrys.kotson.fromJson
import com.github.salomonbrys.kotson.gsonTypeToken
import com.google.android.material.snackbar.Snackbar
import com.robolancers.lancerscoutkotlin.R
import com.robolancers.lancerscoutkotlin.adapters.TemplateEditingAdapter
import com.robolancers.lancerscoutkotlin.models.template.*
import com.robolancers.lancerscoutkotlin.room.entities.ScoutData
import com.robolancers.lancerscoutkotlin.room.entities.Template
import com.robolancers.lancerscoutkotlin.room.viewmodels.ScoutDataViewModel
import com.robolancers.lancerscoutkotlin.room.viewmodels.TemplateViewModel
import com.robolancers.lancerscoutkotlin.utilities.GsonHelper.Companion.gson
import com.robolancers.lancerscoutkotlin.utilities.activity.ToolbarActivity
import com.robolancers.lancerscoutkotlin.utilities.callback.ItemTouchHelperSimpleCallback
import com.robolancers.lancerscoutkotlin.utilities.callback.LancerTextWatcher
import com.robolancers.lancerscoutkotlin.utilities.enums.TemplateModelType.*
import kotlinx.android.synthetic.main.activity_template_editing.*

class TemplateEditingActivity : ToolbarActivity() {
    private lateinit var templateEditingAdapter: TemplateEditingAdapter
    private var templateEditingList = mutableListOf<TemplateModel>()

    private var template: Template? = null
    private var templateName: String? = ""

    private lateinit var templateViewModel: TemplateViewModel
    private lateinit var scoutDataViewModel: ScoutDataViewModel

    private var userWantToSave = true

    private var scoutData: ScoutData? = null
    private var scouting = false

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

        template = intent.getParcelableExtra("Template")
        if (template != null) {
            templateName = template?.name
        }

        scoutData = intent.getParcelableExtra("ScoutData")
        if (scoutData != null) {
            templateName = scoutData?.scoutDataName
        }

        scouting = scoutData != null

        val templateData: String? = if (scouting) scoutData?.data else template?.data
        if (templateData != null && templateData != "") {
            templateEditingList = gson.fromJson(templateData)
        }

        templateEditingAdapter =
            TemplateEditingAdapter(this@TemplateEditingActivity, templateEditingList, scouting)

        template_editing_list.apply {
            layoutManager = LinearLayoutManager(this@TemplateEditingActivity, RecyclerView.VERTICAL, false)
            adapter = templateEditingAdapter

            setOnTouchListener { v, _ ->
                (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(v.windowToken, 0)
                false
            }
        }.also {
            if (!scouting) {
                templateEditingHelper.attachToRecyclerView(it)
            }
        }

        templateViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application)).get(
            TemplateViewModel::class.java)

        scoutDataViewModel =
            ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application)).get(
                ScoutDataViewModel::class.java
            )

        template_editing_title.apply {
            setText(templateName)
            addTextChangedListener(object: LancerTextWatcher() {
                override fun afterTextChanged(s: Editable?) {
                    templateEditingAdapter.hasTemplateChanged = true
                }
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (scouting) {
            menuInflater.inflate(R.menu.item_scouting_menu, menu)
        } else {
            menuInflater.inflate(R.menu.item_editing_menu, menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.item_editing_save -> {
                save()
                true
            }
            R.id.item_editing_add -> {
                MaterialDialog(this, BottomSheet(LayoutMode.WRAP_CONTENT)).show {
                    listItems(items = values().map { it.toString() }.toList()) { _, _, text ->
                        when(text) {
                            HEADER.toString() -> templateEditingList.add(Header())
                            NOTE.toString() -> templateEditingList.add(Note())
                            COUNTER.toString() -> templateEditingList.add(Counter())
                            ITEM_SELECTOR.toString() -> templateEditingList.add(ItemSelector())
                            STOPWATCH.toString() -> templateEditingList.add(Stopwatch())
                            CHECKBOX.toString() -> templateEditingList.add(Checkbox())
                        }

                        templateEditingAdapter.notifyItemInserted(templateEditingList.size - 1)
                    }
                }
                true
            }
            android.R.id.home -> {
                if (templateEditingAdapter.hasTemplateChanged && userWantToSave) {
                    MaterialDialog(this).show {
                        title(text = "Do you want to save?")
                        positiveButton(text = "Yes") {
                            save()
                            userWantToSave = false
                            finishAfterTransition()
                        }
                        negativeButton(text = "No") {
                            userWantToSave = false
                            finishAfterTransition()
                        }
                    }
                }else {
                    userWantToSave = false
                    finishAfterTransition()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onStop() {
        super.onStop()

        if (userWantToSave) save()
    }

    fun startDragging(viewHolder: RecyclerView.ViewHolder) {
        templateEditingHelper.startDrag(viewHolder)
    }

    private fun save() {
        val json = gson.toJson(templateEditingList, gsonTypeToken<MutableList<TemplateModel>>())
        templateName = template_editing_title.text.toString()

        if (scouting) {
            val scoutDataCopy = scoutData
            if (scoutDataCopy != null) {
                scoutDataCopy.data = json
                scoutDataCopy.scoutDataName = templateName

                scoutDataViewModel.insert(scoutDataCopy)
            }
        } else {
            val templateCopy = template
            if (templateCopy != null) {
                templateCopy.data = json
                templateCopy.name = templateName

                templateViewModel.insert(templateCopy)
            }
        }

        userWantToSave = false

        Snackbar.make(template_editing_linear_layout, "Saved", Snackbar.LENGTH_LONG)
            .setBackgroundTint(Color.RED)
            .setTextColor(Color.WHITE)
            .show()
    }
}
