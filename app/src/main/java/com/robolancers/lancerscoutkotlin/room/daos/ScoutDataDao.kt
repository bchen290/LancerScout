package com.robolancers.lancerscoutkotlin.room.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.robolancers.lancerscoutkotlin.room.entities.ScoutData

@Dao
interface ScoutDataDao : BaseDao<ScoutData> {
    @Query("SELECT scout_data.* FROM scout_data INNER JOIN teams ON scout_data.teamNumber == teams.teamNumber where scout_data.teamNumber = :teamNumber")
    fun getAllScoutData(teamNumber: Int): LiveData<List<ScoutData>>
}