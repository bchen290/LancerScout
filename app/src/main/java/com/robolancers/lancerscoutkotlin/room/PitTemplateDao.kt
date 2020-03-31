package com.robolancers.lancerscoutkotlin.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
abstract class PitTemplateDao : BaseDao<PitTemplate>() {
    @Query("SELECT * FROM pit_templates")
    abstract fun getAllPitTemplates(): LiveData<List<PitTemplate>>
}