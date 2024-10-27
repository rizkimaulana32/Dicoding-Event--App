package com.example.dicodingeventapp.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.dicodingeventapp.data.local.FavoriteEvent
import com.example.dicodingeventapp.data.local.FavoriteEventDao
import com.example.dicodingeventapp.data.local.FavoriteEventRoomDatabase
import com.example.dicodingeventapp.repository.EventRepository.Companion
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

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

//class FavoriteRepository(application: Application) {
//    private val mFavoriteEventDao: FavoriteEventDao
//    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
//
//    init {
//        val db = FavoriteEventRoomDatabase.getDatabase(application)
//        mFavoriteEventDao = db.favoriteEventDao()
//    }
//
//    fun getAllFavEvent(): LiveData<List<FavoriteEvent>> = mFavoriteEventDao.getAllFavoriteEvents()
//
//    fun getFavoriteEventById(id: String): LiveData<FavoriteEvent> = mFavoriteEventDao.getFavoriteEventById(id)
//
//    fun insert(favoriteEvent: FavoriteEvent) {
//        executorService.execute { mFavoriteEventDao.insert(favoriteEvent) }
//    }
//
//    fun delete(favoriteEvent: FavoriteEvent) {
//        executorService.execute { mFavoriteEventDao.delete(favoriteEvent) }
//    }
//}