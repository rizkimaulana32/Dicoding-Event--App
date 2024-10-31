package com.example.dicodingeventapp.repository

import androidx.lifecycle.LiveData
import com.example.dicodingeventapp.data.local.FavoriteEvent
import com.example.dicodingeventapp.data.local.FavoriteEventDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class FavoriteRepository private constructor(
    private val mFavoriteEventDao: FavoriteEventDao,
    private val dispatcher: CoroutineDispatcher
) {
    companion object {
        @Volatile
        private var instance: FavoriteRepository? = null

        fun getInstance(
            dao: FavoriteEventDao,
            dispatcher: CoroutineDispatcher
        ): FavoriteRepository =
            instance ?: synchronized(this) {
                instance ?: FavoriteRepository(dao, dispatcher)
            }.also { instance = it }
    }


    fun getAllFavEvent(): LiveData<List<FavoriteEvent>> = mFavoriteEventDao.getAllFavoriteEvents()

    fun getFavoriteEventById(id: String): LiveData<FavoriteEvent> =
        mFavoriteEventDao.getFavoriteEventById(id)

    suspend fun insert(favoriteEvent: FavoriteEvent) {
        withContext(dispatcher) {
            mFavoriteEventDao.insert(favoriteEvent)
        }
    }

    suspend fun delete(favoriteEvent: FavoriteEvent) {
        withContext(dispatcher) {
            mFavoriteEventDao.delete(favoriteEvent)
        }
    }
}