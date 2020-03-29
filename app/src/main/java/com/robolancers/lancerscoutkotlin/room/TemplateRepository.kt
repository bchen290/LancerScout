package com.robolancers.lancerscoutkotlin.room

import androidx.lifecycle.LiveData

class TemplateRepository(private val pitTemplateDao: PitTemplateDao, private val matchTemplateDao: MatchTemplateDao) {
    val allPitTemplates: LiveData<List<PitTemplate>> = pitTemplateDao.getAllPitTemplates()
    val allMatchTemplates: LiveData<List<MatchTemplate>> = matchTemplateDao.getAllMatchTemplates()

    suspend fun insert(matchTemplate: MatchTemplate) {
        matchTemplateDao.insert(matchTemplate)
    }

    suspend fun insert(pitTemplate: PitTemplate) {
        pitTemplateDao.insert(pitTemplate)
    }
}