package com.example.dicodingeventapp.ui.upcoming

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

class UpcomingViewModel : ViewModel() {
    private val _listEvent = MutableLiveData<List<ListEventsItem>>()
    val listEvent: LiveData<List<ListEventsItem>> = _listEvent

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<Event<String?>?>()
    val errorMessage: LiveData<Event<String?>?> = _errorMessage

    private val _noResults = MutableLiveData<Boolean>()
    val noResults: LiveData<Boolean> = _noResults

    companion object {
        private const val TAG = "UpcomingViewModel"
    }

    init {
        findUpcomingEvents()
    }

    private fun findUpcomingEvents() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUpcomingEvents()

        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(
                call: Call<EventResponse>,
                response: Response<EventResponse>
            ) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _listEvent.value = responseBody.listEvents
                        _noResults.value = responseBody.listEvents.isEmpty()
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
                _isLoading.value = false
                _errorMessage.value = Event("Pastikan Anda memiliki koneksi internet.")
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun searchEvent(keyword: String) {
        _isLoading.value = true
        _noResults.value = false
        val client = ApiConfig.getApiService().searchUpcomingEvents(keyword = keyword)

        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(
                call: Call<EventResponse>,
                response: Response<EventResponse>
            ) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _listEvent.value = responseBody.listEvents
                        _noResults.value = responseBody.listEvents.isEmpty()
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
                _isLoading.value = false
                _errorMessage.value = Event("Pastikan Anda memiliki koneksi internet.")
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun loadUpcomingEvents() {
        findUpcomingEvents()
    }
}