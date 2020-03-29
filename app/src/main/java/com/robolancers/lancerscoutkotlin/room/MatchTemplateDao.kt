package com.robolancers.lancerscoutkotlin.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MatchTemplateDao {
    @Insert
    fun insert(matchTemplate: MatchTemplate)

    @Delete
    fun deleteMatchTemplates(vararg matchTemplate: MatchTemplate)

    @Query("SELECT * FROM match_templates")
    fun getAllMatchTemplates(): LiveData<List<MatchTemplate>>
}