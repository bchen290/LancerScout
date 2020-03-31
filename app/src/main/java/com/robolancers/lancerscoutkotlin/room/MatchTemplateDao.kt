package com.robolancers.lancerscoutkotlin.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
abstract class MatchTemplateDao : BaseDao<MatchTemplate>() {
    @Query("SELECT * FROM match_templates")
    abstract fun getAllMatchTemplates(): LiveData<List<MatchTemplate>>
}