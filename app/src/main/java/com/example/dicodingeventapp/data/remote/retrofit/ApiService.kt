package com.example.dicodingeventapp.data.remote.retrofit

import com.example.dicodingeventapp.data.remote.response.DetailEventResponse
import com.example.dicodingeventapp.data.remote.response.EventResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("events")
    fun getUpcomingEvents(@Query("active") active: Int = 1): Call<EventResponse>

    @GET("events")
    fun getFinishedEvents(@Query("active") active: Int = 0): Call<EventResponse>

    @GET("events")
    fun get5UpcomingEvents(
        @Query("active") active: Int = 1,
        @Query("limit") limit: Int = 5
    ): Call<EventResponse>

    @GET("events")
    fun get5FinishedEvents(
        @Query("active") active: Int = 0,
        @Query("limit") limit: Int = 5
    ): Call<EventResponse>

    @GET("events/{id}")
    fun getEventById(@Path("id") id: String): Call<DetailEventResponse>

    @GET("events")
    fun searchUpcomingEvents(
        @Query("active") active: Int = 1,
        @Query("q") keyword: String
    ): Call<EventResponse>

    @GET("events")
    fun searchFinishedEvents(
        @Query("active") active: Int = 0,
        @Query("q") keyword: String
    ): Call<EventResponse>
}