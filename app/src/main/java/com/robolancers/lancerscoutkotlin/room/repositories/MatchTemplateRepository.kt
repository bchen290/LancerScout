package com.robolancers.lancerscoutkotlin.room.repositories

import androidx.lifecycle.LiveData
import com.robolancers.lancerscoutkotlin.room.daos.MatchTemplateDao
import com.robolancers.lancerscoutkotlin.room.entities.MatchTemplate

class MatchTemplateRepository(private val matchTemplateDao: MatchTemplateDao) {
    val allMatchTemplates: LiveData<List<MatchTemplate>> = matchTemplateDao.getAllMatchTemplates()

    suspend fun insert(matchTemplate: MatchTemplate) {
        matchTemplateDao.insert(matchTemplate)
    }

    suspend fun deleteMatchTemplates(vararg matchTemplates: MatchTemplate) {
        matchTemplateDao.delete(*matchTemplates)
    }
}