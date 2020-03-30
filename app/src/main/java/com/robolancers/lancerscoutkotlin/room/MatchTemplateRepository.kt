package com.robolancers.lancerscoutkotlin.room

import androidx.lifecycle.LiveData

class MatchTemplateRepository(private val matchTemplateDao: MatchTemplateDao) {
    val allMatchTemplates: LiveData<List<MatchTemplate>> = matchTemplateDao.getAllMatchTemplates()

    suspend fun insert(matchTemplate: MatchTemplate) {
        matchTemplateDao.insert(matchTemplate)
    }

    suspend fun deleteMatchTemplates(vararg matchTemplates: MatchTemplate) {
        matchTemplateDao.deleteMatchTemplates(*matchTemplates)
    }
}