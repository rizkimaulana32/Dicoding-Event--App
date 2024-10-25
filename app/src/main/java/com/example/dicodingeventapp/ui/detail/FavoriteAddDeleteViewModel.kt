package com.example.dicodingeventapp.ui.detail

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.dicodingeventapp.data.local.FavoriteEvent
import com.example.dicodingeventapp.repository.FavoriteEventRepository

class FavoriteAddDeleteViewModel(application: Application) : ViewModel() {
    private val mFavoriteEventRepository: FavoriteEventRepository = FavoriteEventRepository(application)

    fun insert(favoriteEvent: FavoriteEvent) {
        mFavoriteEventRepository.insert(favoriteEvent)
    }

    fun delete(favoriteEvent: FavoriteEvent) {
        mFavoriteEventRepository.delete(favoriteEvent)
    }

    fun getFavoriteEventById(id: String) = mFavoriteEventRepository.getFavoriteEventById(id)
}