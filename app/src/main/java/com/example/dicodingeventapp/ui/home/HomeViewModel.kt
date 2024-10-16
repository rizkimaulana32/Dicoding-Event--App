package com.example.dicodingeventapp.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dicodingeventapp.data.remote.response.EventResponse
import com.example.dicodingeventapp.data.remote.response.ListEventsItem
import com.example.dicodingeventapp.data.remote.retrofit.ApiConfig
import com.example.dicodingeventapp.util.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val _upcomingEvents = MutableLiveData<List<ListEventsItem>>()
    val upcomingEvents: LiveData<List<ListEventsItem>> = _upcomingEvents

    private val _finishedEvents = MutableLiveData<List<ListEventsItem>>()
    val finishedEvents: LiveData<List<ListEventsItem>> = _finishedEvents

    private val _isLosding = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLosding

    private val _errorMessage = MutableLiveData<Event<String?>?>()
    val errorMessage: LiveData<Event<String?>?> = _errorMessage

    companion object {
        private const val TAG = "HomeViewModel"
    }

    init {
        findUpcomingEvents5()
        findFinishedEvents5()
    }

    private fun findUpcomingEvents5() {
        _isLosding.value = true
        val client = ApiConfig.getApiService().get5UpcomingEvents()

        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(
                call: Call<EventResponse>,
                response: Response<EventResponse>
            ) {
                _isLosding.value = false

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _upcomingEvents.value = responseBody.listEvents
                        _errorMessage.value = null
                    } else {
                        _errorMessage.value = Event("Data kosong atau tidak dapat ditemukan.")
                        Log.e(TAG, "Response body is null")
                    }
                } else {
                    _errorMessage.value = Event("Gagal mendapatkan data: ${response.message()}")
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLosding.value = false
                _errorMessage.value = Event("Pastikan Anda memiliki koneksi internet.")
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun findFinishedEvents5() {
        _isLosding.value = true
        val client = ApiConfig.getApiService().get5FinishedEvents()

        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(
                call: Call<EventResponse>,
                response: Response<EventResponse>
            ) {
                _isLosding.value = false

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _finishedEvents.value = responseBody.listEvents
                        _errorMessage.value = null
                    } else {
                        _errorMessage.value = Event("Data kosong atau tidak dapat ditemukan.")
                        Log.e(
                            TAG, "Response body is null"
                        )
                    }
                } else {
                    _errorMessage.value = Event("Gagal mendapatkan data: ${response.message()}")
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLosding.value = false
                _errorMessage.value = Event("Pastikan Anda memiliki koneksi internet.")
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }


    fun load() {
        findUpcomingEvents5()
        findFinishedEvents5()
    }
}