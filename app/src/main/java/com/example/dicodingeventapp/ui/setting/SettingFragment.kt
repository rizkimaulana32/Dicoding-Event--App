package com.example.dicodingeventapp.ui.setting

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.dicodingeventapp.databinding.FragmentSettingBinding
import com.example.dicodingeventapp.ui.factory.SettingViewModelFactory
import com.example.dicodingeventapp.worker.DailyReminderWorker
import java.util.concurrent.TimeUnit

class SettingFragment : Fragment() {
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!
    private lateinit var workManager: WorkManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        workManager = WorkManager.getInstance(requireContext())

        val pref = SettingPreferences.getInstance(requireContext().datastore)
        val settingViewModel = ViewModelProvider(
            this,
            SettingViewModelFactory(pref)
        )[SettingViewModel::class.java]

        val switchTheme = binding.switchDarkMode

        settingViewModel.getThemeSetting()
            .observe(viewLifecycleOwner) { isDarkModeActive: Boolean ->
                if (isDarkModeActive) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    switchTheme.isChecked = true
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    switchTheme.isChecked = false
                }
            }

        switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            settingViewModel.saveThemeSetting(isChecked)
        }

        val switchDailyReminder = binding.switchReminder

        settingViewModel.getDailyReminderSetting()
            .observe(viewLifecycleOwner) { isDailyReminderActive: Boolean ->
                switchDailyReminder.setOnCheckedChangeListener(null)
                switchDailyReminder.isChecked = isDailyReminderActive

                switchDailyReminder.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
                    settingViewModel.saveDailyReminderSetting(isChecked)

                    if (isChecked) {
                        if (Build.VERSION.SDK_INT >= 33) {
                            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                                scheduleDailyReminder()
                            } else {
                                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                            }
                        } else {
                            scheduleDailyReminder()
                            Log.d("SettingFragment", "Daily reminder scheduled")
                        }
                    } else {
                        cancelDailyReminder()
                        Log.d("SettingFragment", "Daily reminder canceled")
                    }
                }
            }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(requireContext(), "Notifications permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Notifications permission rejected", Toast.LENGTH_SHORT).show()
            }
        }

    private fun scheduleDailyReminder() {
        val periodicWorkRequest = PeriodicWorkRequest.Builder(DailyReminderWorker::class.java, 1, TimeUnit.DAYS)
            .addTag(DailyReminderWorker.TAG)
            .build()

        workManager.enqueue(periodicWorkRequest)
    }

    private fun cancelDailyReminder() {
        workManager.cancelAllWorkByTag(DailyReminderWorker.TAG)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}