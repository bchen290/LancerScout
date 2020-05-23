package com.robolancers.lancerscoutkotlin.room.repositories

import androidx.lifecycle.LiveData
import com.robolancers.lancerscoutkotlin.room.daos.ScoutDataDao
import com.robolancers.lancerscoutkotlin.room.entities.ScoutData

class ScoutDataRepository(private val scoutDataDao: ScoutDataDao) {
    val allScoutData: LiveData<List<ScoutData>> = scoutDataDao.getAllScoutData()

    fun getAllScoutData(teamNumber: Int): LiveData<List<ScoutData>> {
        return scoutDataDao.getAllScoutData(teamNumber)
    }

    suspend fun insert(scoutData: ScoutData) {
        scoutDataDao.insert(scoutData)
    }

    suspend fun deleteScoutData(vararg scoutData: ScoutData) {
        scoutDataDao.delete(*scoutData)
    }
}