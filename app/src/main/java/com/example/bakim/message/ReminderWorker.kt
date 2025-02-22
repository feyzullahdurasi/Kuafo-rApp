package com.example.bakim.message

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.example.bakim.R

class ReminderWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val reservationId = inputData.getString(KEY_RESERVATION_ID)
            val title = inputData.getString(KEY_TITLE) ?: "Randevu Hatırlatıcısı"
            val message = inputData.getString(KEY_MESSAGE) ?: "Randevunuz yaklaşıyor!"

            showNotification(title, message)
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    private fun showNotification(title: String, message: String) {
        val notificationId = 1
        val channelId = "reminder_channel"

        // Bildirim kanalını oluştur
        createNotificationChannel(channelId)

        // Notification oluştur
        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.mipmap.ic_launcher_round)  // Bildirim simgesi
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH) // Önemli bir bildirim olarak ayarla
            .build()

        // Bildirimi göster
        with(NotificationManagerCompat.from(applicationContext)) {
            // Bildirim ID'si ile bildirimi göster  notify(notificationId, notification)
        }
    }

    private fun createNotificationChannel(channelId: String) {
        // Sadece Android Oreo ve üzeri sürümlerde bildirim kanalı oluştur
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Reminder Notifications"
            val descriptionText = "Randevu hatırlatma bildirimleri"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            // NotificationManager ile kanalı kaydet
            val notificationManager: NotificationManager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val KEY_RESERVATION_ID = "reservation_id"
        const val KEY_TITLE = "title"
        const val KEY_MESSAGE = "message"
    }
}
