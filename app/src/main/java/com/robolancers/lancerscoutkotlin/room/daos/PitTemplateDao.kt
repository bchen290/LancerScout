package com.robolancers.lancerscoutkotlin.room.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.robolancers.lancerscoutkotlin.room.entities.PitTemplate

@Dao
interface PitTemplateDao :
    BaseDao<PitTemplate> {
    @Query("SELECT * FROM pit_templates")
    fun getAllPitTemplates(): LiveData<List<PitTemplate>>
}