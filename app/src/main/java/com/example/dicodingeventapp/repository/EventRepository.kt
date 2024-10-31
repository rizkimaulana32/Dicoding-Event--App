package com.example.dicodingeventapp.repository

import com.example.dicodingeventapp.data.remote.response.Event
import com.example.dicodingeventapp.data.remote.response.ListEventsItem
import com.example.dicodingeventapp.data.remote.retrofit.ApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.Flow

class EventRepository private constructor(
    private val apiService: ApiService,
    private val dispatcher: CoroutineDispatcher
) {
    companion object {
        @Volatile
        private var instance: EventRepository? = null
        fun getInstance(
            apiService: ApiService,
            dispatcher: CoroutineDispatcher
        ): EventRepository =
            instance ?: synchronized(this) {
                instance ?: EventRepository(apiService, dispatcher)
            }.also { instance = it }
    }

    fun findUpcomingEvents(): Flow<Result<List<ListEventsItem>>> = flow {
        emit(Result.Loading)
        try {
            val response = apiService.getUpcomingEvents()
            val events = response.listEvents
            val listUpcoming = events.map { event ->
                event.toListEvents()
            }
            emit(Result.Success(listUpcoming))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }.flowOn(dispatcher)

    fun findFinishedEvents(): Flow<Result<List<ListEventsItem>>> = flow {
        emit(Result.Loading)
        try {
            val response = apiService.getFinishedEvents()
            val events = response.listEvents
            val listFinished = events.map { event ->
                event.toListEvents()
            }
            emit(Result.Success(listFinished))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }.flowOn(dispatcher)

    fun find5UpcomingEvents(): Flow<Result<List<ListEventsItem>>> = flow {
        emit(Result.Loading)
        try {
            val response = apiService.get5UpcomingEvents()
            val events = response.listEvents
            val listUpcoming = events.map { event ->
                event.toListEvents()
            }
            emit(Result.Success(listUpcoming))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }.flowOn(dispatcher)

    fun find5FinishedEvents(): Flow<Result<List<ListEventsItem>>> = flow {
        emit(Result.Loading)
        try {
            val response = apiService.get5FinishedEvents()
            val events = response.listEvents
            val listFinished = events.map { event ->
                event.toListEvents()
            }
            emit(Result.Success(listFinished))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }.flowOn(dispatcher)

    fun findEventById(eventId: String): Flow<Result<Event>> = flow {
        emit(Result.Loading)
        try {
            val response = apiService.getEventById(eventId)
            val event = response.event
            emit(Result.Success(event))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }.flowOn(dispatcher)

    fun searchEvent(keyword: String, active: Int): Flow<Result<List<ListEventsItem>>> =
        flow {
            emit(Result.Loading)
            try {
                val response = apiService.searchEvents(active = active, keyword = keyword)
                val events = response.listEvents
                val listEvent = events.map { event ->
                    event.toListEvents()
                }
                emit(Result.Success(listEvent))
            } catch (e: Exception) {
                emit(Result.Error(e.message.toString()))
            }
        }.flowOn(dispatcher)

    suspend fun findClosestEvent(): Result<List<ListEventsItem>> {
        return try {
            val response = apiService.getClosestEvent()
            val closestEvent = response.listEvents
            val listEvent = closestEvent.map { event ->
                event.toListEvents()
            }
            Result.Success(listEvent)
        } catch (e: Exception) {
            Result.Error(e.message.toString())
        }
    }

    private fun ListEventsItem.toListEvents(): ListEventsItem {
        return ListEventsItem(
            summary,
            mediaCover,
            registrants,
            imageLogo,
            link,
            description,
            ownerName,
            cityName,
            quota,
            name,
            id,
            beginTime,
            endTime,
            category
        )
    }
}