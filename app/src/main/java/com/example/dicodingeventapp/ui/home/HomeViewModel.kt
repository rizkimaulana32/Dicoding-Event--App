package com.example.dicodingeventapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingeventapp.data.remote.response.ListEventsItem
import com.example.dicodingeventapp.repository.EventRepository
import com.example.dicodingeventapp.util.Event
import com.example.dicodingeventapp.util.Handler
import kotlinx.coroutines.launch

class HomeViewModel(private val eventRepository: EventRepository) : ViewModel() {

    private val _upcomingEvents = MutableLiveData<List<ListEventsItem>>()
    val upcomingEvents: LiveData<List<ListEventsItem>> = _upcomingEvents

    private val _finishedEvents = MutableLiveData<List<ListEventsItem>>()
    val finishedEvents: LiveData<List<ListEventsItem>> = _finishedEvents

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<Event<String?>?>()
    val errorMessage: LiveData<Event<String?>?> = _errorMessage


    init {
        findUpcomingEvents5()
        findFinishedEvents5()
    }

    private fun findUpcomingEvents5() {
        _isLoading.value = true
        viewModelScope.launch {
            eventRepository.find5UpcomingEvents().collect { result ->
                Handler.resultHandler(result, _isLoading, _upcomingEvents, _errorMessage)
            }
        }
    }

    private fun findFinishedEvents5() {
        _isLoading.value = true
        viewModelScope.launch {
            eventRepository.find5FinishedEvents().collect { result ->
                Handler.resultHandler(result, _isLoading, _finishedEvents, _errorMessage)
            }
        }
    }

    fun load() {
        findUpcomingEvents5()
        findFinishedEvents5()
    }
}