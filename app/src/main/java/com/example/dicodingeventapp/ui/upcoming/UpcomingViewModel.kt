package com.example.dicodingeventapp.ui.upcoming

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingeventapp.data.remote.response.ListEventsItem
import com.example.dicodingeventapp.repository.EventRepository
import com.example.dicodingeventapp.util.Event
import com.example.dicodingeventapp.util.Handler
import kotlinx.coroutines.launch

class UpcomingViewModel(private val eventRepository: EventRepository) : ViewModel() {
    private val _listEvent = MutableLiveData<List<ListEventsItem>>()
    val listEvent: LiveData<List<ListEventsItem>> = _listEvent

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<Event<String?>?>()
    val errorMessage: LiveData<Event<String?>?> = _errorMessage

    private val _noResults = MutableLiveData<Boolean>()
    val noResults: LiveData<Boolean> = _noResults

    init {
        findUpcomingEvents()
    }

    private fun findUpcomingEvents() {
        _isLoading.value = true
        viewModelScope.launch {
            eventRepository.findUpcomingEvents().collect { result ->
                Handler.resultHandler(result, _isLoading, _listEvent, _errorMessage, _noResults)
            }
        }
    }

    fun searchEvent(keyword: String) {
        _isLoading.value = true
        viewModelScope.launch {
            eventRepository.searchEvent(keyword, active = 1).collect { result ->
                Handler.resultHandler(result, _isLoading, _listEvent, _errorMessage, _noResults)
            }
        }
    }

    fun loadUpcomingEvents() {
        findUpcomingEvents()
    }
}
