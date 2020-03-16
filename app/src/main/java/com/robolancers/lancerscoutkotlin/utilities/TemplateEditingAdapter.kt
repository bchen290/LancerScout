package com.robolancers.lancerscoutkotlin.utilities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.robolancers.lancerscoutkotlin.R
import com.robolancers.lancerscoutkotlin.models.template.*

class TemplateEditingAdapter(private val templateModels: MutableList<TemplateModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
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

    class HeaderHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var headerText: TextView = itemView.findViewById(R.id.header_text_view)

        fun bind(templateModel: TemplateModel) {
            headerText.text = (templateModel as Header).text
        }
    }

    class EmptyHolder(itemView: View): RecyclerView.ViewHolder(itemView)
}