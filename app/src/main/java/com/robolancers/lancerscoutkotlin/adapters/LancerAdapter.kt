package com.robolancers.lancerscoutkotlin.adapters

import androidx.recyclerview.widget.RecyclerView

abstract class LancerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    abstract fun moveItem(from: Int, to: Int)

    abstract fun deleteItem(position: Int)

    abstract fun showUndoSnackbar()

    abstract fun undoDelete()
}