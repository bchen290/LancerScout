package com.robolancers.lancerscoutkotlin.room.repositories

import androidx.lifecycle.LiveData
import com.robolancers.lancerscoutkotlin.room.daos.TemplateDao
import com.robolancers.lancerscoutkotlin.room.entities.Template

class TemplateRepository(private val templateDao: TemplateDao) {
    val allTemplates: LiveData<List<Template>> = templateDao.getAllTemplates()

    suspend fun insert(template: Template) {
        templateDao.insert(template)
    }

    suspend fun deleteMatchTemplates(vararg templates: Template) {
        templateDao.delete(*templates)
    }
}