package com.robolancers.lancerscoutkotlin.utilities

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.robolancers.lancerscoutkotlin.R
import com.robolancers.lancerscoutkotlin.activities.TemplateEditingActivity
import com.robolancers.lancerscoutkotlin.models.template.*

class TemplateEditingAdapter<T: Any>(private val context: Context, private val templateModels: MutableList<T>) : LancerAdapter<T>(templateModels) {
    class HeaderHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private var headerText = itemView.findViewById<EditText>(R.id.header_title)

        fun bind(templateModel: Header) {
            headerText.setText(templateModel.text)
        }
    }

    class NoteHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private var noteTitle = itemView.findViewById<EditText>(R.id.note_title)
        private var noteText = itemView.findViewById<EditText>(R.id.note_text)

        fun bind(templateModel: Note) {
            noteText.setText(templateModel.title)
            noteTitle.setText(templateModel.text)
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
                } else if (splitText[0] == "Stop") {
                    stopWatchThread.cancel = true
                }
            }
        }
    }

    class EmptyHolder(itemView: View): RecyclerView.ViewHolder(itemView)

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