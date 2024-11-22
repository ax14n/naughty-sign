package com.example.naughty_sign.activities

import NotificationHelper
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.naughty_sign.R
import com.example.naughty_sign.databinding.ActivityMainBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    /**
     * Se inicializan variables y se preparan recursos no relacionados con la vista.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Se inicia el binding.
        binding = ActivityMainBinding.inflate(layoutInflater)

        // Configura el NavController con el BottomNavigationView
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment)
                as NavHostFragment
        val navController = navHostFragment.navController

        // Conectar el BottomNavigationView con el NavController
        binding.bottomNavigation.setupWithNavController(navController)
        setContentView(binding.root)

        //LLamamos a los métodos para inicializar las notificaciones
        checkNotificationPermission()
        initializeNotificacion()

    }

    /**
     * Hacemos que la app nos pida permiso para las notificaciones
     */
    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1)
            }
        }
    }

    /**
     * Inicializamos las notificaciones
     */
    private fun initializeNotificacion() {
        NotificationHelper(this).apply {
            createNotificationChannel()
            lifecycleScope.launch {
                getToken()
                // Suscribirse a un tema
                subscribeToTopic("naughty_sign_alerts")

                delay(5000) // Pausar durante 5 segundos
                //Mostrar notificación local
                showNotification("¡Has recibido un like, fenómeno!", "¿A qué estás esperando?")
            }
        }
    }
}