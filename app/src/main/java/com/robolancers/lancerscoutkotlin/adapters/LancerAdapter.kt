package com.robolancers.lancerscoutkotlin.adapters

interface LancerAdapter {
    fun moveItem(from: Int, to: Int)

    fun deleteItem(position: Int)

    fun showUndoSnackbar()

    fun undoDelete()

    fun notifyItemMoved(from: Int, to: Int)
}