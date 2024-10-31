package com.example.dicodingeventapp.ui.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dicodingeventapp.di.Injection
import com.example.dicodingeventapp.repository.EventRepository
import com.example.dicodingeventapp.ui.detail.DetailViewModel
import com.example.dicodingeventapp.ui.finished.FinishedViewModel
import com.example.dicodingeventapp.ui.home.HomeViewModel
import com.example.dicodingeventapp.ui.upcoming.UpcomingViewModel

class EventViewModelFactory private constructor(private val eventRepository: EventRepository) :
ViewModelProvider.NewInstanceFactory() {
    companion object {
        @Volatile
        private var instance: EventViewModelFactory? = null
        fun getInstance(): EventViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: EventViewModelFactory(Injection.provideEventRepository())
            }.also { instance = it }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UpcomingViewModel::class.java)) {
            return UpcomingViewModel(eventRepository) as T
        } else if (modelClass.isAssignableFrom(FinishedViewModel::class.java)) {
            return FinishedViewModel(eventRepository) as T
        } else if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(eventRepository) as T
        } else if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(eventRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }


}