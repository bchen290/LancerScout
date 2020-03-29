package com.robolancers.lancerscoutkotlin.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.robolancers.lancerscoutkotlin.R
import com.robolancers.lancerscoutkotlin.activities.TemplateEditingActivity
import com.robolancers.lancerscoutkotlin.models.template.ItemSelectorItem
import kotlinx.android.synthetic.main.list_item_white_text.view.*

class ItemSelectorAdapter<T: Any>(private val context: Context, private val listItems: MutableList<ItemSelectorItem>, private val itemSelectorHolder: TemplateEditingAdapter<T>.ItemSelectorHolder) : LancerAdapter<ItemSelectorItem>(listItems) {
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val itemTitle: EditText = itemView.findViewById(R.id.item_selector_item_title)
        private val itemDelete: ImageButton = itemView.findViewById(R.id.item_selector_item_delete)

        fun bind(listItem: ItemSelectorItem) {
            itemTitle.setText(listItem.itemName)

            itemTitle.setOnFocusChangeListener { _, hasFocus ->
                if(!hasFocus) {
                    listItem.itemName = itemTitle.text.toString()
                }
            }

            itemDelete.setOnClickListener {
                listItems.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_selector_items, parent, false))

        viewHolder.itemView.handle_view.setOnTouchListener { _, event ->
            if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                itemSelectorHolder.startDragging(viewHolder)
            }

            return@setOnTouchListener true
        }

        return viewHolder
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemSelectorAdapter<*>.ViewHolder) {
            holder.bind(listItems[position])
        }
    }

    override fun showUndoSnackbar() {
        if (context is TemplateEditingActivity) {
            val view = context.findViewById<LinearLayout>(R.id.template_editing_linear_layout)
            val snackbar = Snackbar.make(view, "Deleted", Snackbar.LENGTH_LONG)
            snackbar.setAction("Undo") {
                undoDelete()
            }
            snackbar.show()
        }
    }
}