package com.example.naughty_sign

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.naughty_sign.databinding.ActivityMainBinding
import com.example.naughty_sign.databinding.ActivitySettingsBinding
import com.example.naughty_sign.databinding.FragmentProfileBinding

class ConfigurationActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    /**
     * Se inicializan variables y se preparan recursos no relacionados con la vista.
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Enlaza el XML con la Activity
        setContentView(R.layout.activity_settings)

        // Se inicia el binding.
        binding = ActivitySettingsBinding.inflate(layoutInflater)



    }
}