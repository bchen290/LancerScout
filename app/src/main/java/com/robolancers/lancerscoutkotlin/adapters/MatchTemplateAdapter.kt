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
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.robolancers.lancerscoutkotlin.R
import com.robolancers.lancerscoutkotlin.activities.TemplateActivity
import com.robolancers.lancerscoutkotlin.activities.TemplateEditingActivity
import com.robolancers.lancerscoutkotlin.room.MatchTemplate
import com.robolancers.lancerscoutkotlin.room.MatchTemplateViewModel
import com.robolancers.lancerscoutkotlin.utilities.RecyclerViewOnClickListener
import kotlinx.android.synthetic.main.list_item_white_text.view.*
import java.util.*

class MatchTemplateAdapter(private val context: Context): LancerAdapter() {
    inner class MatchTemplateListener : RecyclerViewOnClickListener<MatchTemplate> {
        override fun onItemClicked(itemClicked: MatchTemplate) {
            val intent = Intent(context, TemplateEditingActivity::class.java)
            intent.putExtra("ItemClickedName", itemClicked.name)
            intent.putExtra("ItemClickedData", itemClicked.data)
            context.startActivity(intent)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView = itemView.findViewById(R.id.list_content)
    }

    private lateinit var recentlyDeletedItem: MatchTemplate
    private var templates = emptyList<MatchTemplate>()
    private val listener = MatchTemplateListener()
    private val viewModel = ViewModelProvider(context as TemplateActivity, ViewModelProvider.AndroidViewModelFactory(context.application)).get(MatchTemplateViewModel::class.java)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder =
            ViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_white_text, parent, false)
            )

        viewHolder.itemView.handle_view.setOnTouchListener { _, event ->
            if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                if (context is TemplateActivity) {
                    context.startMatchDrag(viewHolder)
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

    fun setTemplates(templates: List<MatchTemplate>) {
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