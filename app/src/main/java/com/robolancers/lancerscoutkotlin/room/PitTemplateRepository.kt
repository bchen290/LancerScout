package com.robolancers.lancerscoutkotlin.room

import android.util.Log
import androidx.lifecycle.LiveData

class PitTemplateRepository(private val pitTemplateDao: PitTemplateDao) {
    val allPitTemplates: LiveData<List<PitTemplate>> = pitTemplateDao.getAllPitTemplates()

    suspend fun insert(pitTemplate: PitTemplate) {
        pitTemplateDao.insert(pitTemplate)
    }

    suspend fun deletePitTemplates(vararg pitTemplate: PitTemplate) {
        pitTemplateDao.delete(*pitTemplate)
    }
}