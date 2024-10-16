package com.example.dicodingeventapp.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dicodingeventapp.data.remote.response.Event
import com.example.dicodingeventapp.data.remote.response.DetailEventResponse
import com.example.dicodingeventapp.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.dicodingeventapp.util.Event as eventWrapper

class DetailViewModel : ViewModel() {
    private val _event = MutableLiveData<Event>()
    val event: LiveData<Event> = _event

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<eventWrapper<String?>?>()
    val errorMessage: LiveData<eventWrapper<String?>?> = _errorMessage

    companion object {
        private const val TAG = "DetailViewModel"
    }

    fun findEventById(eventId: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getEventById(eventId)

        client.enqueue(object : Callback<DetailEventResponse> {
            override fun onResponse(
                call: Call<DetailEventResponse>,
                response: Response<DetailEventResponse>
            ) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _event.value = responseBody.event
                        _errorMessage.value = null
                    } else {
                        _errorMessage.value = eventWrapper("Data kosong atau tidak dapat ditemukan.")
                        Log.e(TAG, "Response body is null")
                    }
                } else {
                    _errorMessage.value = eventWrapper("Gagal mendapatkan data: ${response.message()}")
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailEventResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = eventWrapper("Pastikan Anda memiliki koneksi internet.")
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
}