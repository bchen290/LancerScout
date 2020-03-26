package com.robolancers.lancerscoutkotlin.utilities.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.robolancers.lancerscoutkotlin.R
import com.robolancers.lancerscoutkotlin.activities.TemplateEditingActivity
import com.robolancers.lancerscoutkotlin.models.template.*
import com.robolancers.lancerscoutkotlin.utilities.ItemTouchHelperSimpleCallbackNoSwipe
import com.robolancers.lancerscoutkotlin.utilities.StopwatchThread

class TemplateEditingAdapter<T: Any>(private val context: Context, private val templateModels: MutableList<T>) : LancerAdapter<T>(templateModels) {
    class HeaderHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private var headerText = itemView.findViewById<EditText>(R.id.header_title)

        fun bind(templateModel: Header) {
            headerText.setText(templateModel.text)

            headerText.setOnFocusChangeListener { _, hasFocus ->
                if(!hasFocus) {
                    templateModel.text = headerText.text.toString()
                }
            }
        }
    }

    class NoteHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private var noteTitle = itemView.findViewById<EditText>(R.id.note_title)
        private var noteText = itemView.findViewById<EditText>(R.id.note_text)

        fun bind(templateModel: Note) {
            noteTitle.setText(templateModel.title)
            noteText.setText(templateModel.text)

            noteTitle.setOnFocusChangeListener { _, hasFocus ->
                if(!hasFocus) {
                    templateModel.title = noteTitle.text.toString()
                }
            }

            noteText.setOnFocusChangeListener { _, hasFocus ->
                if(!hasFocus) {
                    templateModel.text = noteText.text.toString()
                }
            }
        }
    }

    inner class StopwatchHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private var stopwatchTitle = itemView.findViewById<EditText>(R.id.stopwatch_title)
        private var stopwatchButton = itemView.findViewById<Button>(R.id.stopwatch_button)

        private lateinit var stopWatchThread: StopwatchThread

        @SuppressLint("SetTextI18n")
        fun bind(templateModel: Stopwatch) {
            stopwatchTitle.setText(templateModel.title)
            stopwatchButton.text = "Start ${templateModel.time}"

            stopwatchButton.setOnClickListener {
                val stopwatchButtonText: String = stopwatchButton.text.toString()
                val splitText = stopwatchButtonText.split(" ")

                if (splitText[0] == "Start") {
                    stopWatchThread = StopwatchThread(stopwatchButton, context)
                    stopWatchThread.start()

                    stopwatchButton.setBackgroundColor(Color.RED)
                } else if (splitText[0] == "Stop") {
                    stopWatchThread.cancel = true

                    stopwatchButton.setBackgroundColor(Color.GREEN)

                    templateModel.time = splitText[1]
                }
            }

            stopwatchTitle.setOnFocusChangeListener { _, hasFocus ->
                if(!hasFocus) {
                    templateModel.title = stopwatchTitle.text.toString()
                }
            }
        }
    }

    class CounterHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private var counterTitle = itemView.findViewById<EditText>(R.id.counter_title)
        private var counterPlusButton = itemView.findViewById<Button>(R.id.counter_plus)
        private var counterMinusButton = itemView.findViewById<Button>(R.id.counter_minus)
        private var counterCount = itemView.findViewById<EditText>(R.id.counter_count)
        private var counterUnit = itemView.findViewById<EditText>(R.id.counter_unit)

        fun bind(templateModel: Counter) {
            counterTitle.setText(templateModel.title)
            counterCount.setText(templateModel.count.toString())
            counterUnit.setText(templateModel.unit)

            counterMinusButton.setOnClickListener {
                var count = counterCount.text.toString().toInt()
                count -= 1
                counterCount.setText(count.toString())

                templateModel.count = counterCount.text.toString().toInt()
            }

            counterPlusButton.setOnClickListener {
                var count = counterCount.text.toString().toInt()
                count += 1
                counterCount.setText(count.toString())

                templateModel.count = counterCount.text.toString().toInt()
            }

            counterTitle.setOnFocusChangeListener { _, hasFocus ->
                if(!hasFocus) {
                    templateModel.title = counterTitle.text.toString()
                }
            }

            counterCount.setOnFocusChangeListener { _, hasFocus ->
                if(!hasFocus) {
                    templateModel.count = counterCount.text.toString().toInt()
                }
            }

            counterUnit.setOnFocusChangeListener { _, hasFocus ->
                if(!hasFocus) {
                    templateModel.unit = counterUnit.text.toString()
                }
            }
        }
    }

    inner class ItemSelectorHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private var itemSelectorTitle = itemView.findViewById<EditText>(R.id.item_selector_title)
        private var itemSelectorAdd = itemView.findViewById<Button>(R.id.item_selector_add)
        private var itemSelectorRecyclerView = itemView.findViewById<RecyclerView>(R.id.item_selector_list)

        private var itemSelectorItems = mutableListOf<ItemSelectorItem>()

        private var itemSelectorAdapter = ItemSelectorAdapter(itemSelectorRecyclerView.context, itemSelectorItems, this)
        private var itemSelectorItemTouchHelper = ItemTouchHelper(ItemTouchHelperSimpleCallbackNoSwipe(itemSelectorRecyclerView.context, itemSelectorAdapter).simpleItemCallback)
        private val itemSelectorLinearLayoutManager = LinearLayoutManager(itemSelectorRecyclerView.context, RecyclerView.VERTICAL, false)

        fun bind(templateModel: ItemSelector) {
            itemSelectorTitle.setText(templateModel.title)
            itemSelectorItems.addAll(templateModel.list)
            itemSelectorAdapter.notifyDataSetChanged()

            itemSelectorAdd.setOnClickListener {
                itemSelectorItems.add(ItemSelectorItem(""))
                itemSelectorAdapter.notifyItemInserted(templateModel.list.size - 1)
            }

            itemSelectorRecyclerView.apply {
                layoutManager = itemSelectorLinearLayoutManager
                adapter = itemSelectorAdapter
                setRecycledViewPool(viewPool)
            }

            itemSelectorTitle.setOnFocusChangeListener { _, hasFocus ->
                if(!hasFocus) {
                    templateModel.title = itemSelectorTitle.text.toString()
                }
            }

            itemSelectorItemTouchHelper.attachToRecyclerView(itemSelectorRecyclerView)
        }

        fun startDragging(viewHolder: RecyclerView.ViewHolder) {
            itemSelectorItemTouchHelper.startDrag(viewHolder)
        }
    }

    class CheckboxHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val checkbox = itemView.findViewById<CheckBox>(R.id.checkbox)
        private val checkboxTitle = itemView.findViewById<EditText>(R.id.checkbox_title)

        fun bind(templateModel: Checkbox) {
            checkbox.isChecked = templateModel.checkedState
            checkboxTitle.setText(templateModel.title)

            checkboxTitle.setOnFocusChangeListener { _, hasFocus ->
                if(!hasFocus) {
                    templateModel.title = checkboxTitle.text.toString()
                }
            }

            checkbox.setOnClickListener {
                templateModel.checkedState = checkbox.isChecked
            }
        }
    }

    class EmptyHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private val viewPool = RecyclerView.RecycledViewPool()

    companion object {
        const val VIEW_TYPE_CHECKBOX = 1
        const val VIEW_TYPE_COUNTER = 2
        const val VIEW_TYPE_HEADER = 3
        const val VIEW_TYPE_ITEM_SELECTOR = 4
        const val VIEW_TYPE_NOTE = 5
        const val VIEW_TYPE_STOPWATCH = 6
    }

    override fun getItemViewType(position: Int): Int {
        return when(templateModels[position]) {
            is Header -> VIEW_TYPE_HEADER
            is Checkbox -> VIEW_TYPE_CHECKBOX
            is Counter -> VIEW_TYPE_COUNTER
            is ItemSelector -> VIEW_TYPE_ITEM_SELECTOR
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
            else -> {
                inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.item_empty, parent, false)
                viewHolder = EmptyHolder(inflatedView)
            }
        }

        val handleView = inflatedView.findViewById<ImageView>(R.id.handle_view)
        handleView.setOnTouchListener { _, event ->
            if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                if (context is TemplateEditingActivity) {
                    context.startDragging(viewHolder)
                }
            }

            return@setOnTouchListener true
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            VIEW_TYPE_HEADER -> (holder as HeaderHolder).bind(templateModels[position] as Header)
            VIEW_TYPE_NOTE -> (holder as NoteHolder).bind(templateModels[position] as Note)
            VIEW_TYPE_STOPWATCH -> (holder as TemplateEditingAdapter<*>.StopwatchHolder).bind(templateModels[position] as Stopwatch)
            VIEW_TYPE_COUNTER -> (holder as CounterHolder).bind(templateModels[position] as Counter)
            VIEW_TYPE_ITEM_SELECTOR -> (holder as TemplateEditingAdapter<*>.ItemSelectorHolder).bind(templateModels[position] as ItemSelector)
            VIEW_TYPE_CHECKBOX -> (holder as CheckboxHolder).bind(templateModels[position] as Checkbox)
        }
    }

    override fun getItemCount(): Int {
        return templateModels.size
    }

    override fun showUndoSnackbar() {
        if (context is TemplateEditingActivity) {
            val view = context.findViewById<CoordinatorLayout>(R.id.template_editing_coordinator_layout)
            val snackbar = Snackbar.make(view, "Deleted", Snackbar.LENGTH_LONG)
            snackbar.setAction("Undo") {
                undoDelete()
            }
            snackbar.show()
        }
    }
}