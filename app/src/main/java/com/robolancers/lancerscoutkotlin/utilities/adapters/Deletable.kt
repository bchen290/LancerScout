package com.robolancers.lancerscoutkotlin.utilities.adapters

interface Deletable {
    fun deleteItem(position: Int)

    fun showUndoSnackbar()

    fun undoDelete()

}