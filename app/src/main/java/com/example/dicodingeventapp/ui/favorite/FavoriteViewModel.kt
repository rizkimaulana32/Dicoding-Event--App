package com.example.dicodingeventapp.ui.favorite

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dicodingeventapp.data.local.FavoriteEvent
import com.example.dicodingeventapp.repository.FavoriteEventRepository

class FavoriteViewModel(application: Application) : ViewModel() {
    private val mFavoriteEventRepository: FavoriteEventRepository =
        FavoriteEventRepository(application)

    private val _noFavorite = MutableLiveData<Boolean>()
    val noFavorite: LiveData<Boolean> = _noFavorite

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _favoriteEvents = MutableLiveData<List<FavoriteEvent>>()
    val favoriteEvents: LiveData<List<FavoriteEvent>> = _favoriteEvents

    init {
        getAllFavoriteEvents()
    }

    private fun checkIfNoFavorites(events: List<FavoriteEvent>) {
        _noFavorite.value = events.isEmpty()
    }

    private fun getAllFavoriteEvents() {
        _isLoading.value = true
        val favoriteEvents = mFavoriteEventRepository.getAllFavEvent()
        favoriteEvents.observeForever {
            _isLoading.value = false
            _favoriteEvents.value = it
            checkIfNoFavorites(it)
        }
    }
}