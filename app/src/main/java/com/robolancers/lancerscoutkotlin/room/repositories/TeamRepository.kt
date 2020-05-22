package com.robolancers.lancerscoutkotlin.room.repositories

import androidx.lifecycle.LiveData
import com.robolancers.lancerscoutkotlin.room.daos.TeamDao
import com.robolancers.lancerscoutkotlin.room.entities.Team

class TeamRepository(private val teamDao: TeamDao) {
    val allTeams: LiveData<List<Team>> = teamDao.getAllTeams()

    suspend fun insert(team: Team) {
        teamDao.insert(team)
    }

    suspend fun deleteTeams(vararg teams: Team) {
        teamDao.delete(*teams)
    }

    fun findTeamByNumber(teamNumber: Int): Team {
        return teamDao.findTeamByNumber(teamNumber)
    }
}