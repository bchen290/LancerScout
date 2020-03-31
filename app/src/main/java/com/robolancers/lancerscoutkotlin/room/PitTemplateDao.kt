package com.robolancers.lancerscoutkotlin.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PitTemplateDao : BaseDao<PitTemplate> {
    @Query("SELECT * FROM pit_templates")
    fun getAllPitTemplates(): LiveData<List<PitTemplate>>
}