package com.example.naughty_sign.notifications


import NotificationHelper
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * Controla la recepción de mensajes push de Firebase
 */
class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val notificationHelper by lazy { NotificationHelper(this) }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        val title = remoteMessage.notification?.title ?: "Nueva Notificación"
        val message = remoteMessage.notification?.body ?: "Tienes un nuevo mensaje."

        notificationHelper.showNotification(title, message)
    }

    /**
     * Controla la generación de un nuevo token FCM
     */
    override fun onNewToken(token: String) {
        super.onNewToken(token)

        Log.d("FCM Token", "Token: $token")
    }
}