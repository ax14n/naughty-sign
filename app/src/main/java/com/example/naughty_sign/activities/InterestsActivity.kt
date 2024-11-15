package com.example.naughty_sign.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.naughty_sign.databinding.ActivityInterestsBinding

class InterestsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInterestsBinding

    /**
     * Se inicializan variables y se preparan recursos no relacionados con la vista.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInterestsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}