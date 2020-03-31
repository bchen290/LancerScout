package com.robolancers.lancerscoutkotlin.room.daos

import androidx.room.*

@Dao
interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(obj: T): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg obj: T): List<Long>

    @Update
    suspend fun update(obj: T)

    @Update
    suspend fun update(obj: List<T>)

    @Delete
    suspend fun delete(vararg obj: T)
}