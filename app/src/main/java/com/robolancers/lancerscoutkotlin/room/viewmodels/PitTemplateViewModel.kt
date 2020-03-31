package com.robolancers.lancerscoutkotlin.room.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.robolancers.lancerscoutkotlin.room.TemplateDatabase
import com.robolancers.lancerscoutkotlin.room.entities.PitTemplate
import com.robolancers.lancerscoutkotlin.room.repositories.PitTemplateRepository
import kotlinx.coroutines.launch

class PitTemplateViewModel(application: Application) : AndroidViewModel(application) {
    private val pitTemplateRepository: PitTemplateRepository

    val allPitTemplates: LiveData<List<PitTemplate>>

    init {
        val pitTemplateDao = TemplateDatabase.getDatabase(
            application
        ).pitTemplateDao()
        pitTemplateRepository =
            PitTemplateRepository(
                pitTemplateDao
            )
        allPitTemplates = pitTemplateRepository.allPitTemplates
    }

    fun insert(pitTemplate: PitTemplate) = viewModelScope.launch {
        pitTemplateRepository.insert(pitTemplate)
    }

    fun delete(vararg pitTemplate: PitTemplate) = viewModelScope.launch {
        pitTemplateRepository.deletePitTemplates(*pitTemplate)
    }
}