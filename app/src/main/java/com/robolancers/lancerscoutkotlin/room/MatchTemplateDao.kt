package com.robolancers.lancerscoutkotlin.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MatchTemplateDao : BaseDao<MatchTemplate> {
    @Query("SELECT * FROM match_templates")
    fun getAllMatchTemplates(): LiveData<List<MatchTemplate>>
}