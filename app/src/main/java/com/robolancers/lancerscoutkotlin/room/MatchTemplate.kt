package com.robolancers.lancerscoutkotlin.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "match_templates")
data class MatchTemplate(@PrimaryKey var name: String, var data: String)