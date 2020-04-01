package com.robolancers.lancerscoutkotlin.adapters

interface LancerAdapter {
    abstract fun moveItem(from: Int, to: Int)

    abstract fun deleteItem(position: Int)

    abstract fun showUndoSnackbar()

    abstract fun undoDelete()
}