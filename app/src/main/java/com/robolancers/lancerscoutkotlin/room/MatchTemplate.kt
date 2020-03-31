package com.robolancers.lancerscoutkotlin.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "match_templates")
data class MatchTemplate(var name: String, var data: String){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}