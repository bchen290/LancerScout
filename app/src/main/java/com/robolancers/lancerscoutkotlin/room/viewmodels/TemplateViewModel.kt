package com.robolancers.lancerscoutkotlin.room.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.robolancers.lancerscoutkotlin.room.LancerDatabase
import com.robolancers.lancerscoutkotlin.room.entities.Template
import com.robolancers.lancerscoutkotlin.room.repositories.TemplateRepository
import kotlinx.coroutines.launch

class TemplateViewModel(application: Application) : AndroidViewModel(application) {
    private val templateRepository: TemplateRepository

    val allTemplates: LiveData<List<Template>>

    init {
        val templateDao = LancerDatabase.getDatabase(
            application
        ).templateDao()

        templateRepository =
            TemplateRepository(
                templateDao
            )
        allTemplates = templateRepository.allTemplates
    }

    fun insert(template: Template) = viewModelScope.launch {
        templateRepository.insert(template)
    }

    fun delete(vararg templates: Template) = viewModelScope.launch {
        templateRepository.deleteMatchTemplates(*templates)
    }
}