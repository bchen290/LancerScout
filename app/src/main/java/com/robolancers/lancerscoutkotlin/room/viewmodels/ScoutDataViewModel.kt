package com.robolancers.lancerscoutkotlin.room.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.robolancers.lancerscoutkotlin.room.LancerDatabase
import com.robolancers.lancerscoutkotlin.room.entities.ScoutData
import com.robolancers.lancerscoutkotlin.room.repositories.ScoutDataRepository
import kotlinx.coroutines.launch

class ScoutDataViewModel(application: Application) : AndroidViewModel(application) {
    private val scoutDataRepository: ScoutDataRepository

    init {
        val scoutDataDao = LancerDatabase.getDatabase(
            application
        ).scoutDataDao()

        scoutDataRepository =
            ScoutDataRepository(
                scoutDataDao
            )
    }

    fun getAllScoutData(teamNumber: Int): LiveData<List<ScoutData>> {
        return scoutDataRepository.getAllScoutData(teamNumber)
    }

    fun insert(scoutData: ScoutData) = viewModelScope.launch {
        scoutDataRepository.insert(scoutData)
    }

    fun delete(vararg scoutData: ScoutData) = viewModelScope.launch {
        scoutDataRepository.deleteScoutData(*scoutData)
    }
}