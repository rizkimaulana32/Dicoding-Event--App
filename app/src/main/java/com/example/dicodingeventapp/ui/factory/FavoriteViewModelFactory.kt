package com.example.dicodingeventapp.ui.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dicodingeventapp.di.Injection
import com.example.dicodingeventapp.repository.FavoriteRepository
import com.example.dicodingeventapp.ui.detail.DetailViewModel
import com.example.dicodingeventapp.ui.detail.FavoriteAddDeleteViewModel
import com.example.dicodingeventapp.ui.favorite.FavoriteViewModel
import com.example.dicodingeventapp.ui.finished.FinishedViewModel
import com.example.dicodingeventapp.ui.home.HomeViewModel
import com.example.dicodingeventapp.ui.upcoming.UpcomingViewModel

class FavoriteViewModelFactory private constructor(
    private val favoriteRepository: FavoriteRepository
) : ViewModelProvider.Factory {

    companion object {
        @Volatile
        private var INSTANCE: FavoriteViewModelFactory? = null
        @JvmStatic
        fun getInstance(application: Application): FavoriteViewModelFactory {
            return INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: FavoriteViewModelFactory(Injection.provideFavoriteRepository(application)).also {
                        INSTANCE = it
                    }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            return FavoriteViewModel(favoriteRepository) as T
        } else if (modelClass.isAssignableFrom(FavoriteAddDeleteViewModel::class.java)) {
            return FavoriteAddDeleteViewModel(favoriteRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}
