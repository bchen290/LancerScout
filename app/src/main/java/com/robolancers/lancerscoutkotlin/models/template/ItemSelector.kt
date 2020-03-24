package com.robolancers.lancerscoutkotlin.models.template

data class ItemSelector(var title: String = "", var list: MutableList<ItemSelectorItem> = mutableListOf()) : TemplateModel()