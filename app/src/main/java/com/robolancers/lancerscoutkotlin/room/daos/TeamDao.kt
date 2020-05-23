package com.robolancers.lancerscoutkotlin.room.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.robolancers.lancerscoutkotlin.room.entities.Team

@Dao
interface TeamDao : BaseDao<Team> {
    @Query("SELECT * FROM teams ORDER BY teamNumber")
    fun getAllTeams(): LiveData<List<Team>>

    @Query("SELECT * FROM teams WHERE teamNumber=:teamNumber")
    fun findTeamByNumber(teamNumber: Int): Team
}