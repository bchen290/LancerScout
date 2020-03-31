package com.robolancers.lancerscoutkotlin.room

import androidx.room.*
import java.io.Serializable

@Dao
abstract class BaseDao<T>: Serializable {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insert(obj: T): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insert(obj: List<T>): List<Long>

    @Update
    abstract suspend fun update(obj: T)

    @Update
    abstract suspend fun update(obj: List<T>)

    @Delete
    abstract suspend fun delete(vararg obj: T)

    @Transaction
    open suspend fun insertOrUpdate(obj: T) {
        if (insert(obj) == -1L) update(obj)
    }

    @Transaction
    open suspend fun insertOrUpdate(objList: List<T>) {
        val insertResult = insert(objList)
        val updateList = mutableListOf<T>()

        for (i in insertResult.indices) {
            if (insertResult[i] == -1L) updateList.add(objList[i])
        }

        if (updateList.isNotEmpty()) update(updateList)
    }
}