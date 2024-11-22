import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.naughty_sign.R
import com.example.naughty_sign.activities.MainActivity
import com.google.firebase.messaging.FirebaseMessaging

object NotificationConstants {
    const val CHANNEL_ID = "naughty_sign_update"
    const val CHANNEL_NAME = "Naughty Sign notificaciones"
    const val CHANNEL_DESCRIPTION = "Notificaciones para la app de citas Naughty Sign"
    const val NOTIFICATION_ID = 1004
}

class NotificationHelper(private val context: Context) {

    /**
     * Crea el canal de notificación para Android Oreo o superior
     */
    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NotificationConstants.CHANNEL_ID,
                NotificationConstants.CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = NotificationConstants.CHANNEL_DESCRIPTION
            }
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }

    /**
     * Muestra una notificación con un título y mensaje
     */
    fun showNotification(title: String, message: String) {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, NotificationConstants.CHANNEL_ID)
            .setSmallIcon(R.drawable.image_removebg_preview)
            .setContentTitle(title)
            .setContentText(message)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NotificationConstants.NOTIFICATION_ID, notification)
    }

    /**
     * Obtiene el token de registro del dispositivo para FCM
     */
    fun getToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM Token", "Error al obtener el token", task.exception)
                return@addOnCompleteListener
            }
            val token = task.result
            Log.d("FCM Token", "Token: $token")
        }
    }

    /**
     * Se suscribe a un tema en FCM
     */
    fun subscribeToTopic(topic: String) {
        FirebaseMessaging.getInstance().subscribeToTopic(topic)
            .addOnCompleteListener { task ->
                val msg = if (task.isSuccessful) {
                    "Suscripción exitosa al tema: $topic"
                } else {
                    "Error al suscribirse al tema: $topic"
                }
                Log.d("FCM Subscription", msg)
            }
    }
}
