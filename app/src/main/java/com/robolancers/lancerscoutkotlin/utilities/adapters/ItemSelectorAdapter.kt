package com.robolancers.lancerscoutkotlin.utilities.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.robolancers.lancerscoutkotlin.R
import com.robolancers.lancerscoutkotlin.activities.TemplateEditingActivity
import com.robolancers.lancerscoutkotlin.models.template.ItemSelectorItem
import kotlinx.android.synthetic.main.list_item_white_text.view.*

class ItemSelectorAdapter<T: Any>(private val context: Context, private val listItems: MutableList<ItemSelectorItem>, private val itemSelectorHolder: TemplateEditingAdapter<T>.ItemSelectorHolder) : LancerAdapter<ItemSelectorItem>(listItems) {
    private lateinit var checkedButton: ImageButton

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val itemButton: ImageButton = itemView.findViewById(R.id.item_selector_item_button)
        private val itemTitle: EditText = itemView.findViewById(R.id.item_selector_item_title)

        fun bind(listItem: ItemSelectorItem) {
            itemTitle.setText(listItem.itemName)

            itemButton.setOnClickListener {
                checkedButton = itemButton
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
            val view = context.findViewById<CoordinatorLayout>(R.id.template_editing_coordinator_layout)
            val snackbar = Snackbar.make(view, "Deleted", Snackbar.LENGTH_LONG)
            snackbar.setAction("Undo") {
                undoDelete()
            }
            snackbar.show()
        }
    }
}