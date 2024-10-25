package com.example.dicodingeventapp.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.dicodingeventapp.data.local.FavoriteEvent
import com.example.dicodingeventapp.data.local.FavoriteEventDao
import com.example.dicodingeventapp.data.local.FavoriteEventRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteEventRepository(application: Application) {
    private val mFavoriteEventDao: FavoriteEventDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteEventRoomDatabase.getDatabase(application)
        mFavoriteEventDao = db.favoriteEventDao()
    }

    fun getAllFavEvent(): LiveData<List<FavoriteEvent>> = mFavoriteEventDao.getAllFavoriteEvents()

    fun getFavoriteEventById(id: String): LiveData<FavoriteEvent> = mFavoriteEventDao.getFavoriteEventById(id)

    fun insert(favoriteEvent: FavoriteEvent) {
        executorService.execute { mFavoriteEventDao.insert(favoriteEvent) }
    }

    fun delete(favoriteEvent: FavoriteEvent) {
        executorService.execute { mFavoriteEventDao.delete(favoriteEvent) }
    }
}