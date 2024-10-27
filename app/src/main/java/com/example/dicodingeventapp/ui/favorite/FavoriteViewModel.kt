package com.example.dicodingeventapp.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dicodingeventapp.data.local.FavoriteEvent
import com.example.dicodingeventapp.repository.FavoriteRepository

class FavoriteViewModel(private val favoriteRepository: FavoriteRepository) : ViewModel() {

    private val _noFavorite = MutableLiveData<Boolean>()
    val noFavorite: LiveData<Boolean> = _noFavorite

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _favoriteEvents = MediatorLiveData<List<FavoriteEvent>>()
    val favoriteEvents: LiveData<List<FavoriteEvent>> = _favoriteEvents

    init {
        getAllFavoriteEvents()
    }

    private fun checkIfNoFavorites(events: List<FavoriteEvent>) {
        _noFavorite.value = events.isEmpty()
    }

    private fun getAllFavoriteEvents() {
        _isLoading.value = true
        val source = favoriteRepository.getAllFavEvent()
        _favoriteEvents.addSource(source) { events ->
            _isLoading.value = false
            _favoriteEvents.value = events
            checkIfNoFavorites(events)
        }
    }
}