package com.robolancers.lancerscoutkotlin.room.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.robolancers.lancerscoutkotlin.room.entities.Team

@Dao
interface TeamDao : BaseDao<Team> {
    @Query("SELECT * FROM teams")
    fun getAllTeams(): LiveData<List<Team>>
}