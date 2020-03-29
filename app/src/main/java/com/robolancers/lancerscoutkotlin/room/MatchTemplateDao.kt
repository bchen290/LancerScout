package com.robolancers.lancerscoutkotlin.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MatchTemplateDao {
    @Insert
    fun insert(matchTemplateEntity: MatchTemplateEntity)

    @Delete
    fun deleteMatchTemplates(vararg matchTemplateEntity: MatchTemplateEntity)

    @Query("SELECT * FROM match_templates")
    fun getAllNotes(): LiveData<List<MatchTemplateEntity>>
}