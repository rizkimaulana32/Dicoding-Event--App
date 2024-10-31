package com.example.dicodingeventapp.util

import android.os.Build
import androidx.lifecycle.MutableLiveData
import com.example.dicodingeventapp.repository.Result
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object Handler {
    fun <T> resultHandler(
        result: Result<T>,
        isLoading: MutableLiveData<Boolean>,
        listEvent: MutableLiveData<T>,
        errorMessage: MutableLiveData<Event<String?>?>,
        noResults: MutableLiveData<Boolean>? = null
    ) {
        when (result) {
            is Result.Loading -> {
                isLoading.value = true
            }
            is Result.Success -> {
                isLoading.value = false
                listEvent.value = result.data
                noResults?.value = result.data is List<*> && result.data.isEmpty()
                errorMessage.value = null
            }
            is Result.Error -> {
                isLoading.value = false
                errorMessage.value = Event("Gagal memuat data. Periksa koneksi internet Anda.")
            }
        }
    }

    fun dateFormat(date: String?): String {
        if (date.isNullOrEmpty()) {
            return "Unknown Time"
        }

        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val outputFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm", Locale.getDefault())

                val parsedDate = LocalDateTime.parse(date, inputFormatter)
                parsedDate.format(outputFormatter)

            } else {
                val inputFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val outputFormatter = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())

                val parsedDate = inputFormatter.parse(date)
                parsedDate?.let { outputFormatter.format(it) } ?: "Unknown Time"
            }
        } catch (e: Exception) {
            "Unknown Time"
        }
    }

}
