package com.robolancers.lancerscoutkotlin.utilities

import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.robolancers.lancerscoutkotlin.R
import com.robolancers.lancerscoutkotlin.activities.TemplateActivity
import kotlinx.android.synthetic.main.list_item_white_text.view.*
import java.util.*

class TemplateAdapter(private val context: Context, private val templates: MutableList<String>, private val isPit: Boolean) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var recentlyDeletedItem: String = ""
    private var recentlyDeletedItemPosition: Int = 0

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView = itemView.findViewById(R.id.list_content)

        init {
            textView.compoundDrawables
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_white_text, parent, false))
        viewHolder.itemView.handle_view.setOnTouchListener { _, event ->
            if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                if (context is TemplateActivity) {
                    context.startDragging(viewHolder, isPit)
                }
            }

            return@setOnTouchListener true
        }

        return viewHolder
    }

    override fun getItemCount(): Int {
        return templates.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).textView.text = templates[position]
    }

    fun moveItem(from: Int, to: Int){
        Collections.swap(templates, from, to)
    }

    fun deleteItem(position: Int) {
        recentlyDeletedItem = templates[position]
        recentlyDeletedItemPosition = position
        templates.removeAt(position)
        notifyItemRemoved(position)
        showUndoSnackbar();
    }

    private fun showUndoSnackbar() {
        if (context is TemplateActivity) {
            val view = context.findViewById<CoordinatorLayout>(R.id.template_coordinator_layout)
            val snackbar = Snackbar.make(view, "Deleted", Snackbar.LENGTH_LONG)
            snackbar.setAction("Undo") {
                undoDelete()
            }
            snackbar.show()
        }
    }

    private fun undoDelete() {
        templates.add(recentlyDeletedItemPosition, recentlyDeletedItem);
        notifyItemInserted(recentlyDeletedItemPosition)
    }
}