package com.example.dicodingeventapp.data.remote.retrofit

import com.example.dicodingeventapp.data.remote.response.DetailEventResponse
import com.example.dicodingeventapp.data.remote.response.EventResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("events")
    suspend fun getUpcomingEvents(@Query("active") active: Int = 1): EventResponse

    @GET("events")
    suspend fun getFinishedEvents(@Query("active") active: Int = 0): EventResponse

    @GET("events")
    suspend fun get5UpcomingEvents(
        @Query("active") active: Int = 1,
        @Query("limit") limit: Int = 5
    ): EventResponse

    @GET("events")
    suspend fun get5FinishedEvents(
        @Query("active") active: Int = 0,
        @Query("limit") limit: Int = 5
    ): EventResponse

    @GET("events/{id}")
    suspend fun getEventById(@Path("id") id: String): DetailEventResponse

    @GET("events")
    suspend fun searchEvents(
        @Query("active") active: Int,
        @Query("q") keyword: String
    ): EventResponse
}