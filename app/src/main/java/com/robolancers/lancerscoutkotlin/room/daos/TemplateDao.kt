package com.robolancers.lancerscoutkotlin.room.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.robolancers.lancerscoutkotlin.room.entities.Template

@Dao
interface TemplateDao : BaseDao<Template> {
    @Query("SELECT * FROM templates ORDER BY name ")
    fun getAllTemplates(): LiveData<List<Template>>
}