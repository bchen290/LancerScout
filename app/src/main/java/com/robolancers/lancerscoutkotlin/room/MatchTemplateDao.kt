package com.robolancers.lancerscoutkotlin.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MatchTemplateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(matchTemplate: MatchTemplate)

    @Delete
    suspend fun deleteMatchTemplates(vararg matchTemplate: MatchTemplate)

    @Query("SELECT * FROM match_templates")
    fun getAllMatchTemplates(): LiveData<List<MatchTemplate>>
}