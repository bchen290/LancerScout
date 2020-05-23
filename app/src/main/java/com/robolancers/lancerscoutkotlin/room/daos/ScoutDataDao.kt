package com.robolancers.lancerscoutkotlin.room.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.robolancers.lancerscoutkotlin.room.entities.ScoutData

@Dao
interface ScoutDataDao : BaseDao<ScoutData> {
    @Query("SELECT scout_data.* FROM scout_data INNER JOIN teams ON scout_data.teamNumber == teams.teamNumber WHERE scout_data.teamNumber = :teamNumber")
    fun getAllScoutData(teamNumber: Int): LiveData<List<ScoutData>>

    @Query("SELECT * FROM scout_data")
    fun getAllScoutData(): LiveData<List<ScoutData>>
}