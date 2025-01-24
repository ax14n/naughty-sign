package com.example.naughty_sign.activities

import NotificationHelper
import android.Manifest
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
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

    /**
     * Inicia la guía.
     */
    @SuppressLint("Recycle")
    private fun initializeGuide() {
        // --- { ABANDONO DE LA GUÍA. } --- //
        guideBinding.exitGuide.setOnClickListener(::onExitGuide)

        // --- { COMIENZO DE LA GUÍA. } --- //
        if (needToStartGuide) {

            // --- { HACE LA GUÍA VISIBLE } --- //
            guideBinding.guideLayout.visibility = View.VISIBLE
            guideBinding.textStep.visibility = View.VISIBLE

            // --- { ANIMACIONES CORREGIDAS } --- //
            val fadeTextStep = ObjectAnimator.ofFloat(guideBinding.textStep, "alpha", 0f, 1f)
            fadeTextStep.repeatCount = 3                        // Veces que se repetirá
            fadeTextStep.repeatMode = ObjectAnimator.REVERSE    // Efecto "parpadeo"

            val fadePulseImage = ObjectAnimator.ofFloat(guideBinding.pulseImage, "alpha", 0f, 0.8f)
            fadePulseImage.repeatCount = 3                      // Veces que se repetirá
            fadePulseImage.repeatMode = ObjectAnimator.REVERSE  // Efecto "parpadeo"

            // --- { INICIADOR DE LAS ANIMACIONES } --- //
            val animatorSet = AnimatorSet()

            // --- { INICIA LAS ANIMACIONES SIN CONFLICTOS } --- //
            animatorSet.playTogether(fadeTextStep, fadePulseImage)
            animatorSet.duration = 1000                         // Duración de las animaciones
            animatorSet.start()                                 // Se inician las animaciones

            // --- { SEGUNDA SECUENCIA DE INSTRUCCIONES } --- //
            animatorSet.doOnEnd {

                // --- { OCULTACIÓN DE ELEMENTOS DEL MENÚ ANTERIOR } --- //
                guideBinding.pulseImage.visibility = View.GONE
                guideBinding.textStep.visibility = View.GONE

                if (needToStartGuide) {

                    guideBinding.textStep2.visibility = View.VISIBLE
                    guideBinding.buttonsGuide.visibility = View.VISIBLE

                    // --- { ANIMACIONES CORREGIDAS } --- //
                    val fadeTextStep2 =
                        ObjectAnimator.ofFloat(guideBinding.textStep2, "alpha", 0f, 1f)
                    fadeTextStep2.repeatCount = 3                        // Veces que se repetirá
                    fadeTextStep2.repeatMode = ObjectAnimator.REVERSE    // Efecto "parpadeo"

                    val fadePulseImage2 =
                        ObjectAnimator.ofFloat(guideBinding.buttonsGuide, "alpha", 0f, 0.8f)
                    fadePulseImage2.repeatCount = 3                      // Veces que se repetirá
                    fadePulseImage2.repeatMode = ObjectAnimator.REVERSE  // Efecto "parpadeo"

                    // --- { INICIADOR DE LAS ANIMACIONES } --- //
                    val animatorSet2 = AnimatorSet()

                    // --- { INICIA LAS ANIMACIONES SIN CONFLICTOS } --- //
                    animatorSet2.playTogether(fadeTextStep2, fadePulseImage2)
                    animatorSet2.duration =
                        1000                         // Duración de las animaciones
                    animatorSet2.start()             // Se inician las animaciones

                    animatorSet2.doOnEnd {
                        guideBinding.buttonsGuide.visibility = View.GONE
                        guideBinding.textStep2.visibility = View.GONE
                        guideBinding.guideLayout.visibility = View.GONE
                    }
                }
            }


        }
    }

    /**
     * Abandona la guía.
     */
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