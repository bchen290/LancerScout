package com.robolancers.lancerscoutkotlin.room.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.robolancers.lancerscoutkotlin.room.TemplateDatabase
import com.robolancers.lancerscoutkotlin.room.entities.MatchTemplate
import com.robolancers.lancerscoutkotlin.room.repositories.MatchTemplateRepository
import kotlinx.coroutines.launch

class MatchTemplateViewModel(application: Application) : AndroidViewModel(application) {
    private val matchTemplateRepository: MatchTemplateRepository

    val allMatchTemplates: LiveData<List<MatchTemplate>>

    init {
        val matchTemplateDao = TemplateDatabase.getDatabase(
            application
        ).matchTemplateDao()
        matchTemplateRepository =
            MatchTemplateRepository(
                matchTemplateDao
            )
        allMatchTemplates = matchTemplateRepository.allMatchTemplates
    }

    fun insert(matchTemplate: MatchTemplate) = viewModelScope.launch {
        matchTemplateRepository.insert(matchTemplate)
    }

    fun delete(vararg matchTemplates: MatchTemplate) = viewModelScope.launch {
        matchTemplateRepository.deleteMatchTemplates(*matchTemplates)
    }
}