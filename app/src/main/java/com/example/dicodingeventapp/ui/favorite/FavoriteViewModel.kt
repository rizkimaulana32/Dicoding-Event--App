package com.example.dicodingeventapp.ui.favorite

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dicodingeventapp.data.local.FavoriteEvent
import com.example.dicodingeventapp.repository.FavoriteEventRepository

class FavoriteViewModel(application: Application) : ViewModel() {
    private val mFavoriteEventRepository: FavoriteEventRepository = FavoriteEventRepository(application)

    private val _noFavorite = MutableLiveData<Boolean>()
    val noFavorite: LiveData<Boolean> = _noFavorite

    fun checkIfNoFavorites(events: List<FavoriteEvent>) {
        _noFavorite.value = events.isEmpty()
    }

    fun getAllFavoriteEvents() = mFavoriteEventRepository.getAllFavEvent()

}