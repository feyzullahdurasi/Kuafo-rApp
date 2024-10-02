package com.example.kuafrapp.message

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.kuafrapp.R

class ReminderWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    override fun doWork(): Result {
        // Hatırlatma mesajını göstermek için bir notification gönderiyoruz
        showNotification("Randevu Hatırlatıcısı", "Randevunuz 2 saat içinde!")
        return Result.success()
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
}
