package com.example.dicodingeventapp.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingeventapp.data.remote.response.Event
import com.example.dicodingeventapp.repository.EventRepository
import com.example.dicodingeventapp.util.Handler
import kotlinx.coroutines.launch
import com.example.dicodingeventapp.util.Event as eventWrapper

class DetailViewModel(private val eventRepository: EventRepository) : ViewModel() {
    private val _event = MutableLiveData<Event>()
    val event: LiveData<Event> = _event

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<eventWrapper<String?>?>()
    val errorMessage: LiveData<eventWrapper<String?>?> = _errorMessage

    fun findEventById(eventId: String) {
        _isLoading.value = true
        viewModelScope.launch {
            eventRepository.findEventById(eventId).collect { result ->
                Handler.resultHandler(result, _isLoading, _event, _errorMessage)
            }
        }
    }
}