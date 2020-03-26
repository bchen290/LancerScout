package com.robolancers.lancerscoutkotlin.models.template

data class Counter(var title: String = "", var count: Int = 0, var unit: String = "") : TemplateModel("Counter")