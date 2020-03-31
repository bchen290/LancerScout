package com.robolancers.lancerscoutkotlin.room.repositories

import androidx.lifecycle.LiveData
import com.robolancers.lancerscoutkotlin.room.daos.PitTemplateDao
import com.robolancers.lancerscoutkotlin.room.entities.PitTemplate

class PitTemplateRepository(private val pitTemplateDao: PitTemplateDao) {
    val allPitTemplates: LiveData<List<PitTemplate>> = pitTemplateDao.getAllPitTemplates()

    suspend fun insert(pitTemplate: PitTemplate) {
        pitTemplateDao.insert(pitTemplate)
    }

    suspend fun deletePitTemplates(vararg pitTemplate: PitTemplate) {
        pitTemplateDao.delete(*pitTemplate)
    }
}