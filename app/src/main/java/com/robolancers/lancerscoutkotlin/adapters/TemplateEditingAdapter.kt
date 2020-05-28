package com.robolancers.lancerscoutkotlin.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.robolancers.lancerscoutkotlin.R
import com.robolancers.lancerscoutkotlin.activities.template.TemplateEditingActivity
import com.robolancers.lancerscoutkotlin.models.template.*
import com.robolancers.lancerscoutkotlin.utilities.activity.StopwatchThread
import com.robolancers.lancerscoutkotlin.utilities.adapters.LancerAdapter
import com.robolancers.lancerscoutkotlin.utilities.callback.ItemTouchHelperSimpleCallbackReorderable
import com.robolancers.lancerscoutkotlin.utilities.callback.LancerTextWatcher
import com.robolancers.lancerscoutkotlin.utilities.view.EditTextUtil
import java.util.*

class TemplateEditingAdapter(
    val context: Context,
    private val templateModels: MutableList<TemplateModel>,
    val scouting: Boolean
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), LancerAdapter {
    inner class HeaderHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private var headerText = itemView.findViewById<EditText>(R.id.header_title)

        fun bind(templateModel: Header) {
            if (scouting) {
                EditTextUtil.disableEditText(headerText)
            }

            headerText.setText(templateModel.text)

            headerText.addTextChangedListener(object: LancerTextWatcher() {
                override fun afterTextChanged(s: Editable?) {
                    templateModel.text = s.toString()
                    hasTemplateChanged = true
                }
            })
        }
    }

    inner class NoteHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private var noteTitle = itemView.findViewById<EditText>(R.id.note_title)
        private var noteText = itemView.findViewById<EditText>(R.id.note_text)

        fun bind(templateModel: Note) {
            if (scouting) {
                EditTextUtil.disableEditText(noteTitle)
            }

            noteTitle.setText(templateModel.title)
            noteText.setText(templateModel.text)

            noteTitle.addTextChangedListener(object: LancerTextWatcher() {
                override fun afterTextChanged(s: Editable?) {
                    templateModel.title = s.toString()
                    hasTemplateChanged = true
                }
            })

            noteText.addTextChangedListener(object: LancerTextWatcher() {
                override fun afterTextChanged(s: Editable?) {
                    templateModel.text = s.toString()
                    hasTemplateChanged = true
                }
            })
        }
    }

    inner class StopwatchHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private var stopwatchTitle = itemView.findViewById<EditText>(R.id.stopwatch_title)
        private var stopwatchButton = itemView.findViewById<Button>(R.id.stopwatch_button)

        private lateinit var stopWatchThread: StopwatchThread

        @SuppressLint("SetTextI18n")
        fun bind(templateModel: Stopwatch) {
            if (scouting) {
                EditTextUtil.disableEditText(stopwatchTitle)
            }

            stopwatchTitle.setText(templateModel.title)
            stopwatchButton.text = "Start ${templateModel.time}"

            stopwatchButton.setOnClickListener {
                val stopwatchButtonText: String = stopwatchButton.text.toString()
                val splitText = stopwatchButtonText.split(" ")

                if (splitText[0] == "Start") {
                    stopWatchThread =
                        StopwatchThread(
                            stopwatchButton,
                            context
                        )
                    stopWatchThread.start()

                    stopwatchButton.setBackgroundColor(Color.RED)
                } else if (splitText[0] == "Stop") {
                    stopWatchThread.cancel = true

                    stopwatchButton.setBackgroundColor(Color.GREEN)

                    templateModel.time = splitText[1]
                }

                hasTemplateChanged = true
            }

            stopwatchTitle.addTextChangedListener(object: LancerTextWatcher() {
                override fun afterTextChanged(s: Editable?) {
                    templateModel.title = s.toString()
                    hasTemplateChanged = true
                }
            })
        }
    }

    inner class CounterHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private var counterTitle = itemView.findViewById<EditText>(R.id.counter_title)
        private var counterPlusButton = itemView.findViewById<Button>(R.id.counter_plus)
        private var counterMinusButton = itemView.findViewById<Button>(R.id.counter_minus)
        private var counterCount = itemView.findViewById<EditText>(R.id.counter_count)
        private var counterUnit = itemView.findViewById<EditText>(R.id.counter_unit)

        fun bind(templateModel: Counter) {
            if (scouting) {
                EditTextUtil.disableEditText(counterTitle)
                EditTextUtil.disableEditText(counterUnit)
            }

            counterTitle.setText(templateModel.title)
            counterCount.setText(templateModel.count.toString())
            counterUnit.setText(templateModel.unit)

            counterMinusButton.setOnClickListener {
                var count = counterCount.text.toString().toInt()
                count -= 1
                counterCount.setText(count.toString())

                templateModel.count = counterCount.text.toString().toInt()
                hasTemplateChanged = true
            }

            counterPlusButton.setOnClickListener {
                var count = counterCount.text.toString().toInt()
                count += 1
                counterCount.setText(count.toString())

                templateModel.count = counterCount.text.toString().toInt()
                hasTemplateChanged = true
            }

            counterTitle.addTextChangedListener(object: LancerTextWatcher() {
                override fun afterTextChanged(s: Editable?) {
                    templateModel.title = s.toString()
                    hasTemplateChanged = true
                }
            })

            counterCount.addTextChangedListener(object: LancerTextWatcher() {
                override fun afterTextChanged(s: Editable?) {
                    if (s.toString().isEmpty()){
                        templateModel.count = 0
                    } else {
                        templateModel.count = s.toString().toInt()
                    }

                    hasTemplateChanged = true
                }
            })

            counterUnit.addTextChangedListener(object: LancerTextWatcher() {
                override fun afterTextChanged(s: Editable?) {
                    templateModel.unit = s.toString()
                    hasTemplateChanged = true
                }
            })
        }
    }

    inner class ItemSelectorHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private var itemSelectorTitle = itemView.findViewById<EditText>(R.id.item_selector_title)
        private var itemSelectorAdd = itemView.findViewById<Button>(R.id.item_selector_add)
        private var itemSelectorRecyclerView = itemView.findViewById<RecyclerView>(R.id.item_selector_list)

        private var itemSelectorItems = mutableListOf<ItemSelectorItem>()

        private var itemSelectorAdapter = ItemSelectorAdapter(itemSelectorRecyclerView.context, itemSelectorItems, this)
        private var itemSelectorItemTouchHelper = ItemTouchHelper(
            ItemTouchHelperSimpleCallbackReorderable(
                itemSelectorAdapter
            ).simpleItemCallback)
        private val itemSelectorLinearLayoutManager = LinearLayoutManager(itemSelectorRecyclerView.context, RecyclerView.VERTICAL, false)

        fun bind(templateModel: ItemSelector) {
            itemSelectorTitle.setText(templateModel.title)
            itemSelectorItems.addAll(templateModel.list)
            templateModel.list = itemSelectorItems
            itemSelectorAdapter.notifyDataSetChanged()

            itemSelectorAdd.setOnClickListener {
                itemSelectorItems.add(ItemSelectorItem(""))
                itemSelectorAdapter.notifyItemInserted(itemSelectorItems.size - 1)

                hasTemplateChanged = true
            }

            itemSelectorRecyclerView.apply {
                layoutManager = itemSelectorLinearLayoutManager
                adapter = itemSelectorAdapter
                setRecycledViewPool(viewPool)
            }

            itemSelectorTitle.addTextChangedListener(object: LancerTextWatcher() {
                override fun afterTextChanged(s: Editable?) {
                    templateModel.title = s.toString()
                    hasTemplateChanged = true
                }
            })

            itemSelectorItemTouchHelper.attachToRecyclerView(itemSelectorRecyclerView)
        }

        fun startDragging(viewHolder: RecyclerView.ViewHolder) {
            itemSelectorItemTouchHelper.startDrag(viewHolder)
        }
    }

    inner class SpinnerHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var text = itemView.findViewById<EditText>(R.id.item_selector_title)

        private var spinner = itemView.findViewById<Spinner>(R.id.item_selector_spinner)
        private lateinit var arrayAdapter: ArrayAdapter<String>

        fun bind(templateModel: ItemSelector) {
            if (scouting) {
                EditTextUtil.disableEditText(text)
            }

            text.setText(templateModel.title)

            arrayAdapter = ArrayAdapter(
                context,
                android.R.layout.simple_list_item_1,
                templateModel.list.map { it.itemName })
            spinner.adapter = arrayAdapter
        }
    }

    inner class CheckboxHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val checkbox = itemView.findViewById<CheckBox>(R.id.checkbox)
        private val checkboxTitle = itemView.findViewById<EditText>(R.id.checkbox_title)

        fun bind(templateModel: Checkbox) {
            if (scouting) {
                EditTextUtil.disableEditText(checkboxTitle)
            }

            checkbox.isChecked = templateModel.checkedState
            checkboxTitle.setText(templateModel.title)

            checkboxTitle.addTextChangedListener(object: LancerTextWatcher() {
                override fun afterTextChanged(s: Editable?) {
                    templateModel.title = s.toString()
                    hasTemplateChanged = true
                }
            })

            checkbox.setOnClickListener {
                templateModel.checkedState = checkbox.isChecked
                hasTemplateChanged = true
            }
        }
    }

    inner class EmptyHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    var hasTemplateChanged = false

    private val viewPool = RecyclerView.RecycledViewPool()
    private lateinit var recentlyDeletedItem: TemplateModel
    private var recentlyDeletedItemPosition = 0

    companion object {
        const val VIEW_TYPE_CHECKBOX = 1
        const val VIEW_TYPE_COUNTER = 2
        const val VIEW_TYPE_HEADER = 3
        const val VIEW_TYPE_ITEM_SELECTOR = 4
        const val VIEW_TYPE_NOTE = 5
        const val VIEW_TYPE_STOPWATCH = 6
        const val VIEW_TYPE_SPINNER = 7
    }

    override fun getItemViewType(position: Int): Int {
        return when(templateModels[position]) {
            is Header -> VIEW_TYPE_HEADER
            is Checkbox -> VIEW_TYPE_CHECKBOX
            is Counter -> VIEW_TYPE_COUNTER
            is ItemSelector -> if (scouting) VIEW_TYPE_SPINNER else VIEW_TYPE_ITEM_SELECTOR
            is Note -> VIEW_TYPE_NOTE
            is Stopwatch -> VIEW_TYPE_STOPWATCH
            else -> super.getItemViewType(position)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflatedView: View
        val viewHolder: RecyclerView.ViewHolder

        when(viewType) {
            VIEW_TYPE_HEADER -> {
                inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.item_header, parent, false)
                viewHolder = HeaderHolder(inflatedView)
            }
            VIEW_TYPE_NOTE -> {
                inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
                viewHolder = NoteHolder(inflatedView)
            }
            VIEW_TYPE_STOPWATCH -> {
                inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.item_stopwatch, parent, false)
                viewHolder = StopwatchHolder(inflatedView)
            }
            VIEW_TYPE_COUNTER -> {
                inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.item_counter, parent, false)
                viewHolder = CounterHolder(inflatedView)
            }
            VIEW_TYPE_ITEM_SELECTOR -> {
                inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.item_selector, parent, false)
                viewHolder = ItemSelectorHolder(inflatedView)
            }
            VIEW_TYPE_CHECKBOX -> {
                inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.item_checkbox, parent, false)
                viewHolder = CheckboxHolder(inflatedView)
            }
            VIEW_TYPE_SPINNER -> {
                inflatedView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_selector_scouting, parent, false)
                viewHolder = SpinnerHolder(inflatedView)
            }
            else -> {
                inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.item_empty, parent, false)
                viewHolder = EmptyHolder(inflatedView)
            }
        }

        val handleView = inflatedView.findViewById<ImageView>(R.id.handle_view)

        if (scouting) {
            handleView.visibility = View.GONE
        } else {
            handleView.setOnTouchListener { _, event ->
                if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                    if (context is TemplateEditingActivity) {
                        context.startDragging(viewHolder)
                    }
                }

                return@setOnTouchListener true
            }
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            VIEW_TYPE_HEADER -> (holder as HeaderHolder).bind(templateModels[position] as Header)
            VIEW_TYPE_NOTE -> (holder as NoteHolder).bind(templateModels[position] as Note)
            VIEW_TYPE_STOPWATCH -> (holder as StopwatchHolder).bind(templateModels[position] as Stopwatch)
            VIEW_TYPE_COUNTER -> (holder as CounterHolder).bind(templateModels[position] as Counter)
            VIEW_TYPE_ITEM_SELECTOR -> (holder as ItemSelectorHolder).bind(templateModels[position] as ItemSelector)
            VIEW_TYPE_CHECKBOX -> (holder as CheckboxHolder).bind(templateModels[position] as Checkbox)
            VIEW_TYPE_SPINNER -> (holder as SpinnerHolder).bind(templateModels[position] as ItemSelector)
        }
    }

    override fun getItemCount(): Int = templateModels.size

    override fun moveItem(from: Int, to: Int){
        Collections.swap(templateModels, from, to)
    }

    override fun deleteItem(position: Int) {
        recentlyDeletedItem = templateModels[position]
        templateModels.removeAt(position)
        recentlyDeletedItemPosition = position
        notifyItemRemoved(position)
        showUndoSnackbar()
    }

    override fun undoDelete() {
        templateModels.add(recentlyDeletedItemPosition, recentlyDeletedItem)
        notifyItemInserted(recentlyDeletedItemPosition)
    }

    override fun showUndoSnackbar() {
        if (context is TemplateEditingActivity) {
            val view = context.findViewById<LinearLayout>(R.id.template_editing_linear_layout)
            Snackbar.make(view, "Deleted", Snackbar.LENGTH_LONG)
                .setTextColor(Color.WHITE)
                .setBackgroundTint(Color.RED)
                .setAction("Undo") {
                    undoDelete()
                }.show()
        }
    }
}