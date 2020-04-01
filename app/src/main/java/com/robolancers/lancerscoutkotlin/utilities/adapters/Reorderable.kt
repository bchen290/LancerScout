package com.robolancers.lancerscoutkotlin.utilities.adapters

interface Reorderable {
    fun moveItem(from: Int, to: Int)

    fun notifyItemMoved(from: Int, to: Int)
}