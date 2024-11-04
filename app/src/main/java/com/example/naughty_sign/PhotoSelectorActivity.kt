package com.example.naughty_sign

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.naughty_sign.databinding.ActivityPhotoSelectorBinding

class PhotoSelectorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPhotoSelectorBinding

    /**
     * Se inicializan variables y se preparan recursos no relacionados con la vista.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoSelectorBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}