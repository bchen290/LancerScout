package com.robolancers.lancerscoutkotlin.room

import androidx.room.Entity

@Entity(tableName = "match_templates")
data class MatchTemplateEntity(var name: String, var data: String)