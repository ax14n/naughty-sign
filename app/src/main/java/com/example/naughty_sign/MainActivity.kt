package com.example.naughty_sign

import android.app.Activity
import android.os.Bundle
import android.renderscript.ScriptGroup.Binding
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cardview.ItemData
import com.example.naughty_sign.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recycleView.layoutManager = LinearLayoutManager(this)
        val items = listOf(
            ItemData("1", R.drawable.ic_launcher_background),
            ItemData("2", R.drawable.ic_launcher_background),
            ItemData("3", R.drawable.ic_launcher_background),
            ItemData("4", R.drawable.ic_launcher_background),
            ItemData("5", R.drawable.ic_launcher_background),
            ItemData("6", R.drawable.ic_launcher_background),
            ItemData("7", R.drawable.ic_launcher_background),
            ItemData("8", R.drawable.ic_launcher_background),
        )

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}