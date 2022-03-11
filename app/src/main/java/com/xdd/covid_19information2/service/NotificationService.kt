package com.xdd.covid_19information2.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.xdd.covid_19information2.MainActivity
import com.xdd.covid_19information2.R

class NotificationService(context: Context, workerParameters: WorkerParameters): Worker(context, workerParameters) {

    companion object{
        private const val CHANNEL_ID = "vaccine_service"
        private const val NOTIFICATION_ID = 1
    }

    override fun doWork(): Result {

        Log.e("XDD", "Do Work")
        showNotification()

        return Result.success()
    }


    private fun showNotification() {
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_MULTIPLE_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("This is a test")
            .setContentText("You are a little bitch")
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        val channel = NotificationChannel(CHANNEL_ID, "vaccine_channel", NotificationManager.IMPORTANCE_HIGH).apply {
            description = "XDD"
        }

        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.createNotificationChannel(channel)

        with(NotificationManagerCompat.from(applicationContext)){
            notify(NOTIFICATION_ID, notification)
        }
    }
}