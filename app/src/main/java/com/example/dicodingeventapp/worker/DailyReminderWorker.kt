package com.example.dicodingeventapp.worker

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.dicodingeventapp.R
import com.example.dicodingeventapp.di.Injection
import com.example.dicodingeventapp.repository.EventRepository
import com.example.dicodingeventapp.ui.detail.DetailActivity
import com.example.dicodingeventapp.util.Handler
import kotlin.Exception
import kotlin.Int
import kotlin.String
import kotlin.apply
import kotlin.run
import com.example.dicodingeventapp.repository.Result as RepositoryResult

class DailyReminderWorker(
    appContext: Context,
    params: WorkerParameters
) :
    CoroutineWorker(appContext, params) {
    private val eventRepository: EventRepository = Injection.provideEventRepository()

    override suspend fun doWork(): Result {
        return try {
            when (val response = eventRepository.findClosestEvent()) {
                is RepositoryResult.Success -> {
                    val closestEvent = response.data.firstOrNull()
                    if (closestEvent != null) {
                        val name = closestEvent.name ?: "Unknown Event"
                        val beginTime = Handler.dateFormat(closestEvent.beginTime)
                        val id = closestEvent.id
                        if (id != null) {
                            showNotification(id, name, beginTime)
                        }
                    }
                }

                is RepositoryResult.Error -> {
                    return Result.failure()
                }

                is RepositoryResult.Loading -> {
                }
            }
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun showNotification(eventId: Int, eventName: String, eventTime: String) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notifDetailIntent = Intent(applicationContext, DetailActivity::class.java).apply {
            putExtra("eventId", eventId)
        }

        val pendingIntent = TaskStackBuilder.create(applicationContext).run {
            addNextIntentWithParentStack(notifDetailIntent)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getPendingIntent(NOTIFICATION_ID, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            } else {
                getPendingIntent(NOTIFICATION_ID, PendingIntent.FLAG_UPDATE_CURRENT)
            }
        }

        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setContentTitle("Event Terdekat: $eventName")
            .setContentText("Dimulai pada: $eventTime")
            .setSmallIcon(R.drawable.baseline_notifications_24)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    companion object {
        const val TAG = "DailyReminderWorker"
        private const val CHANNEL_ID = "daily_reminder_channel"
        private const val CHANNEL_NAME = "Daily Reminder"
        private const val NOTIFICATION_ID = 1
    }

}