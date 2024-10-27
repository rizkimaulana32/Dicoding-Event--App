package com.example.dicodingeventapp.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingeventapp.data.local.FavoriteEvent
import com.example.dicodingeventapp.repository.FavoriteRepository
import kotlinx.coroutines.launch

class FavoriteAddDeleteViewModel(private val mFavoriteRepository: FavoriteRepository) :
    ViewModel() {

    fun insert(favoriteEvent: FavoriteEvent) {
        viewModelScope.launch {
            mFavoriteRepository.insert(favoriteEvent)
        }
    }

    fun delete(favoriteEvent: FavoriteEvent) {
        viewModelScope.launch {
            mFavoriteRepository.delete(favoriteEvent)
        }
    }

    fun getFavoriteEventById(id: String) = mFavoriteRepository.getFavoriteEventById(id)
}