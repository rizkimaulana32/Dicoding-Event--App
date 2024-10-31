package com.example.dicodingeventapp.di

import android.app.Application
import com.example.dicodingeventapp.data.local.FavoriteEventRoomDatabase
import com.example.dicodingeventapp.data.remote.retrofit.ApiConfig
import com.example.dicodingeventapp.repository.EventRepository
import com.example.dicodingeventapp.repository.FavoriteRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

object Injection {
    fun provideEventRepository(): EventRepository {
        val apiService = ApiConfig.getApiService()
        val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
        return EventRepository.getInstance(apiService, ioDispatcher)
    }

    fun provideFavoriteRepository(application: Application): FavoriteRepository {
        val database = FavoriteEventRoomDatabase.getDatabase(application)
        val dao = database.favoriteEventDao()
        val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
        return FavoriteRepository.getInstance(dao, ioDispatcher)
    }
}