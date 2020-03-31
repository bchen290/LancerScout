package com.robolancers.lancerscoutkotlin.room.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.robolancers.lancerscoutkotlin.room.entities.MatchTemplate

@Dao
interface MatchTemplateDao :
    BaseDao<MatchTemplate> {
    @Query("SELECT * FROM match_templates")
    fun getAllMatchTemplates(): LiveData<List<MatchTemplate>>
}