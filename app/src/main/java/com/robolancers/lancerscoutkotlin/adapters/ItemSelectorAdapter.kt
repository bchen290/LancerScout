package com.robolancers.lancerscoutkotlin.adapters

import android.content.Context
import android.text.Editable
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
import com.robolancers.lancerscoutkotlin.activities.template.TemplateEditingActivity
import com.robolancers.lancerscoutkotlin.models.template.ItemSelectorItem
import com.robolancers.lancerscoutkotlin.utilities.adapters.Deletable
import com.robolancers.lancerscoutkotlin.utilities.adapters.Reorderable
import com.robolancers.lancerscoutkotlin.utilities.callback.LancerTextWatcher
import kotlinx.android.synthetic.main.list_item_text_handle.view.*
import java.util.*

class ItemSelectorAdapter(private val context: Context, private val listItems: MutableList<ItemSelectorItem>, private val itemSelectorHolder: TemplateEditingAdapter.ItemSelectorHolder) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Deletable, Reorderable {
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val itemTitle: EditText = itemView.findViewById(R.id.item_selector_item_title)
        private val itemDelete: ImageButton = itemView.findViewById(R.id.item_selector_item_delete)

        fun bind(listItem: ItemSelectorItem) {
            itemTitle.setText(listItem.itemName)

            itemTitle.addTextChangedListener(object: LancerTextWatcher() {
                override fun afterTextChanged(s: Editable?) {
                    listItem.itemName = s.toString()
                }
            })

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
        if (holder is ViewHolder) {
            holder.bind(listItems[position])
        }
    }

    override fun moveItem(from: Int, to: Int){
        Collections.swap(listItems, from, to)
    }

    override fun deleteItem(position: Int) {}

    override fun undoDelete() {}

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