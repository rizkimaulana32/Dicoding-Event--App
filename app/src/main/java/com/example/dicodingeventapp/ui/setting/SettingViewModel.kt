package com.example.dicodingeventapp.ui.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class SettingViewModel(private val preferences: SettingPreferences): ViewModel() {
    fun getThemeSetting(): LiveData<Boolean>{
        return preferences.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean){
        viewModelScope.launch {
            preferences.saveThemeSetting(isDarkModeActive)
        }
    }

    fun getDailyReminderSetting(): LiveData<Boolean>{
        return preferences.getDailyReminderSetting().asLiveData()
    }

    fun saveDailyReminderSetting(isDailyReminderActive: Boolean){
        viewModelScope.launch {
            preferences.saveDailyReminderSetting(isDailyReminderActive)
        }
    }
}