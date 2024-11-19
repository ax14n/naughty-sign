package com.example.naughty_sign.language

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        changeLanguage()
    }

    private fun changeLanguage() {

        val language: String? =
            getSharedPreferences("general_config", MODE_PRIVATE).getString("language", "es")

        val locale = Locale(language!!)
        Locale.setDefault(locale)

        val config = resources.configuration
        config.setLocale(locale)

        // Actualizar la configuración del contexto
        resources.updateConfiguration(config, resources.displayMetrics)

        // Reiniciar la actividad para aplicar el idioma
    }

}