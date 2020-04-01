package com.robolancers.lancerscoutkotlin.adapters

import android.app.ActivityOptions
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.robolancers.lancerscoutkotlin.R
import com.robolancers.lancerscoutkotlin.activities.template.TemplateActivity
import com.robolancers.lancerscoutkotlin.activities.template.TemplateEditingActivity
import com.robolancers.lancerscoutkotlin.room.entities.Template
import com.robolancers.lancerscoutkotlin.room.viewmodels.TemplateViewModel
import com.robolancers.lancerscoutkotlin.utilities.adapters.Deletable

class TemplateAdapter(private val templateActivity: TemplateActivity): RecyclerView.Adapter<RecyclerView.ViewHolder>(), Deletable {
    inner class TemplateListener {
        fun onItemClicked(viewHolder: RecyclerView.ViewHolder, itemClicked: Template) {
            val intent = Intent(templateActivity, TemplateEditingActivity::class.java)
            intent.putExtra("Template", itemClicked)
            val options = ActivityOptions.makeSceneTransitionAnimation(templateActivity, (viewHolder as ViewHolder).textView, "title")
            templateActivity.startActivity(intent, options.toBundle())
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView = itemView.findViewById(R.id.list_content)
    }

    private lateinit var recentlyDeletedItem: Template
    private var templates = emptyList<Template>()
    private val listener = TemplateListener()
    private val viewModel = ViewModelProvider(templateActivity, ViewModelProvider.AndroidViewModelFactory(templateActivity.application)).get(
        TemplateViewModel::class.java)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_text_no_handle, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.textView.text = templates[position].name
            holder.itemView.setOnClickListener {
                listener.onItemClicked(holder, templates[position])
            }
        }
    }

    fun setTemplates(templates: List<Template>) {
        this.templates = templates
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = templates.size

    override fun showUndoSnackbar() {
        val view = templateActivity.findViewById<CoordinatorLayout>(R.id.template_coordinator_layout)
        val snackbar = Snackbar.make(view, "Deleted", Snackbar.LENGTH_LONG)
        snackbar.setAction("Undo") {
            undoDelete()
        }
        snackbar.show()
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