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

class TemplateAdapter<T: Any>(private val context: Context, private val templates: MutableList<T>, private val isPit: Boolean) : LancerAdapter<T>(templates) {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView = itemView.findViewById(R.id.list_content)
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
        (holder as ViewHolder).textView.text = templates[position].toString()
    }

    override fun showUndoSnackbar() {
        if (context is TemplateActivity) {
            val view = context.findViewById<CoordinatorLayout>(R.id.template_coordinator_layout)
            val snackbar = Snackbar.make(view, "Deleted", Snackbar.LENGTH_LONG)
            snackbar.setAction("Undo") {
                undoDelete()
            }
            snackbar.show()
        }
    }
}