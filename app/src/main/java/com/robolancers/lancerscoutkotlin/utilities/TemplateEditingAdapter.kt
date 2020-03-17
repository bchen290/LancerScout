package com.robolancers.lancerscoutkotlin.utilities

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.robolancers.lancerscoutkotlin.R
import com.robolancers.lancerscoutkotlin.activities.TemplateEditingActivity
import com.robolancers.lancerscoutkotlin.models.template.*

class TemplateEditingAdapter<T: Any>(private val context: Context, private val templateModels: MutableList<T>) : LancerAdapter<T>(templateModels) {
    class HeaderHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private var headerText: TextView = itemView.findViewById(R.id.header_text_view)

        fun bind(templateModel: Any) {
            headerText.text = (templateModel as Header).text
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            VIEW_TYPE_HEADER -> HeaderHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_header, parent, false))
            else -> EmptyHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_empty, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            VIEW_TYPE_HEADER -> (holder as HeaderHolder).bind(templateModels[position])
        }
    }

    override fun getItemCount(): Int {
        return templateModels.size
    }

    override fun showUndoSnackbar() {
        if (context is TemplateEditingActivity) {
            val view = context.findViewById<CoordinatorLayout>(R.id.template_coordinator_layout)
            val snackbar = Snackbar.make(view, "Deleted", Snackbar.LENGTH_LONG)
            snackbar.setAction("Undo") {
                undoDelete()
            }
            snackbar.show()
        }
    }
}