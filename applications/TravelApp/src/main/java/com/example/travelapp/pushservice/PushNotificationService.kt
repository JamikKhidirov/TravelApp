package com.example.travelapp.pushservice

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.travelapp.R
import com.google.firebase.Firebase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlin.time.Duration.Companion.nanoseconds


@AndroidEntryPoint
class PushNotificationService : FirebaseMessagingService() {


    override fun onNewToken(token: String) {
        super.onNewToken(token)
        //Отпарвляем токен на сервер или в телеграмм
        //Можно сделать запрос полностью данных аккаунта и отправить на сервер
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        //Вытаскиваем данные с уведомления
        val title: String = message.notification?.title ?: "Новое сообщение"
        val body: String = message.notification?.body ?: ""
        val data: Map<String, String> = message.data

        //Показываем уведомление
        showNotification(
            title,
            body,
            data
        )

    }


    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private fun showNotification(
        title: String,
        body: String,
        data: Map<String, String>
    ){
        val chanelId: String = "push_chanel"

        //Создаем тут канал для уведомления для Android 8+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val cnanel = NotificationChannel(
                chanelId,
                "Push Notification",
                NotificationManager.IMPORTANCE_HIGH
            )
            val notifiManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notifiManager.createNotificationChannel(cnanel)
        }

        val notification = NotificationCompat.Builder(
            this,
            chanelId
        ).setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(this)
            .notify(System.currentTimeMillis().toInt(), notification)

    }


}