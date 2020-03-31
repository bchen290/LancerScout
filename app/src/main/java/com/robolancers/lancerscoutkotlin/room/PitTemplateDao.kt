package com.robolancers.lancerscoutkotlin.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PitTemplateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pitTemplate: PitTemplate)

    @Delete
    suspend fun deletePitTemplates(vararg pitTemplate: PitTemplate)

    @Query("SELECT * FROM pit_templates")
    fun getAllPitTemplates(): LiveData<List<PitTemplate>>
}