package com.robolancers.lancerscoutkotlin.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PitTemplateDao {
    @Insert
    fun insert(pitTemplateEntity: PitTemplateEntity)

    @Delete
    fun deletePitTemplates(vararg pitTemplateEntity: PitTemplateEntity)

    @Query("SELECT * FROM pit_templates")
    fun getAllPitTemplates(): LiveData<List<PitTemplateEntity>>
}