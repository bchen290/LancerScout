package com.robolancers.lancerscoutkotlin.utilities

import androidx.recyclerview.widget.RecyclerView
import java.util.*

abstract class LancerAdapter<T: Any>(var list: MutableList<T>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var recentlyDeletedItem: T
    private var recentlyDeletedItemPosition: Int = 0

    public fun moveItem(from: Int, to: Int){
        Collections.swap(list, from, to)
    }

    fun deleteItem(position: Int) {
        recentlyDeletedItem = list[position]
        recentlyDeletedItemPosition = position
        list.removeAt(position)
        notifyItemRemoved(position)
        showUndoSnackbar();
    }

    abstract fun showUndoSnackbar()

    fun undoDelete() {
        list.add(recentlyDeletedItemPosition, recentlyDeletedItem);
        notifyItemInserted(recentlyDeletedItemPosition)
    }
}