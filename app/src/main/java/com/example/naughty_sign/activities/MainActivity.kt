package com.example.naughty_sign.activities

import NotificationHelper
import android.Manifest
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.SoundPool
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.naughty_sign.R
import com.example.naughty_sign.databinding.ActivityMainBinding
import com.example.naughty_sign.databinding.GuideBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var guideBinding: GuideBinding

    private var needToStartGuide: Boolean = true
    private var stepGuide: Int = 0;

    /**
     * Se inicializan variables y se preparan recursos no relacionados con la vista.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Se inicia el binding.
        binding = ActivityMainBinding.inflate(layoutInflater)
        guideBinding = binding.includeLayout

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

        initializeGuide()
    }

    /**
     * Hacemos que la app nos pida permiso para las notificaciones
     */
    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1
                )
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
            }
        }
    }

    private fun initializeGuide() {
        guideBinding.exitGuide.setOnClickListener(::onExitGuide)

        if (needToStartGuide) {

            guideBinding.guideLayout.visibility = View.VISIBLE


            val scaleX = ObjectAnimator.ofFloat(guideBinding.pulseImage, "scaleX", 1f, 0.5f)
            val scaleY = ObjectAnimator.ofFloat(guideBinding.pulseImage, "scaleY", 1f, 0.5f)

            val fadeIn = ObjectAnimator.ofFloat(guideBinding.textStep, "alpha", 0f, 1f)

            scaleX.repeatCount = 3
            scaleY.repeatCount = 3
            val animatorSet = AnimatorSet()
            animatorSet.play(scaleX).with(scaleY).before(fadeIn)


            animatorSet.duration = 1000 // Duración de la animación
            animatorSet.start()
            animatorSet.doOnEnd {
                if (needToStartGuide) {
                    guideBinding.pulseImage.visibility = View.GONE
                    guideBinding.textStep.visibility = View.VISIBLE
                }
            }

        }
    }

    private fun onExitGuide(view: View?) {
        needToStartGuide = false;

        val soundPool = SoundPool.Builder().setMaxStreams(1).build()
        val soundId = soundPool.load(this, R.raw.bubble, 1)

        soundPool.setOnLoadCompleteListener { _, _, status ->
            if (status == 0) {  // Carga exitosa
                soundPool.play(soundId, 1f, 1f, 0, 0, 1f)
            }
        }

        val mediaPlayer = MediaPlayer.create(this, R.raw.bubble)
        mediaPlayer.start()
        mediaPlayer.setOnCompletionListener {
            mediaPlayer.release()
            guideBinding.guideLayout.visibility = View.GONE
        }
    }

}