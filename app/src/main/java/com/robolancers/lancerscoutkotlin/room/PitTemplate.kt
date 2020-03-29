package com.robolancers.lancerscoutkotlin.room

import androidx.room.Entity

@Entity(tableName = "pit_templates")
data class PitTemplate(var name: String, var data: String)