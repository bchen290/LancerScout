package com.robolancers.lancerscoutkotlin.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pit_templates")
data class PitTemplate(var name: String, var data: String) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}