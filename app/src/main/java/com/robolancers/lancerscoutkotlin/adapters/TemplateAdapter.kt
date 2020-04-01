package com.robolancers.lancerscoutkotlin.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.robolancers.lancerscoutkotlin.R
import com.robolancers.lancerscoutkotlin.activities.template.TemplateActivity
import com.robolancers.lancerscoutkotlin.activities.template.TemplateEditingActivity
import com.robolancers.lancerscoutkotlin.room.entities.Template
import com.robolancers.lancerscoutkotlin.room.viewmodels.TemplateViewModel
import com.robolancers.lancerscoutkotlin.utilities.callback.RecyclerViewOnClickListener
import kotlinx.android.synthetic.main.list_item_white_text.view.*
import java.util.*

class TemplateAdapter(private val context: Context): ListAdapter<Template, RecyclerView.ViewHolder>(DIFF_CALLBACK), LancerAdapter {
    inner class TemplateListener :
        RecyclerViewOnClickListener<Template> {
        override fun onItemClicked(itemClicked: Template) {
            val intent = Intent(context, TemplateEditingActivity::class.java)
            intent.putExtra("Template", itemClicked)
            context.startActivity(intent)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView = itemView.findViewById(R.id.list_content)
    }

    companion object {
        val DIFF_CALLBACK = object: DiffUtil.ItemCallback<Template>() {
            override fun areItemsTheSame(oldItem: Template, newItem: Template): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Template, newItem: Template): Boolean = oldItem.name == newItem.name && oldItem.data == newItem.data
        }
    }

    private lateinit var recentlyDeletedItem: Template
    private var templates = emptyList<Template>()
    private val listener = TemplateListener()
    private val viewModel = ViewModelProvider(context as TemplateActivity, ViewModelProvider.AndroidViewModelFactory(context.application)).get(
        TemplateViewModel::class.java)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder =
            ViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_white_text, parent, false)
            )

        viewHolder.itemView.handle_view.setOnTouchListener { _, event ->
            if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                if (context is TemplateActivity) {
                    context.startDrag(viewHolder)
                }
            }

            return@setOnTouchListener true
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.textView.text = templates[position].name
            holder.itemView.setOnClickListener {
                listener.onItemClicked(templates[position])
            }
        }
    }

    fun setTemplates(templates: List<Template>) {
        this.templates = templates
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = templates.size

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

    override fun moveItem(from: Int, to: Int){
        Collections.swap(templates, from, to)
    }

    override fun deleteItem(position: Int) {
        recentlyDeletedItem = templates[position]
        viewModel.delete(templates[position])
        notifyItemRemoved(position)
        showUndoSnackbar()
    }

    override fun undoDelete() {
        viewModel.insert(recentlyDeletedItem)
        notifyItemInserted(templates.size - 1)
    }
}