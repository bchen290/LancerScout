package com.robolancers.lancerscoutkotlin.room.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.robolancers.lancerscoutkotlin.room.TemplateDatabase
import com.robolancers.lancerscoutkotlin.room.entities.Team
import com.robolancers.lancerscoutkotlin.room.repositories.TeamRepository
import kotlinx.coroutines.launch

class TeamViewModel(application: Application) : AndroidViewModel(application) {
    private val teamRepository: TeamRepository

    val allTeams: LiveData<List<Team>>

    init {
        val teamDao = TemplateDatabase.getDatabase(application).teamDao()

        teamRepository = TeamRepository(teamDao)
        allTeams = teamRepository.allTeams
    }

    fun insert(team: Team) = viewModelScope.launch {
        teamRepository.insert(team)
    }

    fun delete(vararg teams: Team) = viewModelScope.launch {
        teamRepository.deleteTeams(*teams)
    }
}