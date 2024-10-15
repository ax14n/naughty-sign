package com.example.naughty_sign

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cardview.ItemData
import com.example.cardview.RecyclerViewAdapter
import com.example.naughty_sign.databinding.LikesBinding
import com.example.naughty_sign.databinding.MainBinding
import com.example.naughty_sign.databinding.MatchesBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: MainBinding;
    lateinit var likesBinding: LikesBinding;
    lateinit var matchesBinding: MatchesBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding = MainBinding.inflate(layoutInflater)
        likesBinding = LikesBinding.inflate(layoutInflater)
        matchesBinding = MatchesBinding.inflate(layoutInflater)

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
            ItemData("9", R.drawable.ic_launcher_background),
            ItemData("10", R.drawable.ic_launcher_background),
            ItemData("11", R.drawable.ic_launcher_background),
            ItemData("12", R.drawable.ic_launcher_background),
            ItemData("13", R.drawable.ic_launcher_background),
            ItemData("14", R.drawable.ic_launcher_background),
        )
        binding.recycleView.adapter = RecyclerViewAdapter(items)

        likesBinding.likesView.layoutManager = LinearLayoutManager(this)
        val itemsLikes = listOf(
            ItemData("1", R.drawable.ic_launcher_background),
            ItemData("2", R.drawable.ic_launcher_background),
            ItemData("3", R.drawable.ic_launcher_background),
            ItemData("4", R.drawable.ic_launcher_background),
            ItemData("5", R.drawable.ic_launcher_background),
            ItemData("6", R.drawable.ic_launcher_background),
            ItemData("7", R.drawable.ic_launcher_background),
            ItemData("7", R.drawable.ic_launcher_background),
            ItemData("7", R.drawable.ic_launcher_background),
            ItemData("7", R.drawable.ic_launcher_background),
            ItemData("7", R.drawable.ic_launcher_background),
            ItemData("7", R.drawable.ic_launcher_background),
            ItemData("8", R.drawable.ic_launcher_background),
        )
        likesBinding.likesView.adapter = RecyclerViewAdapter(itemsLikes)

        matchesBinding.matchesView.layoutManager = LinearLayoutManager(this)
        val itemsMatches = listOf(
            ItemData("1", R.drawable.ic_launcher_background),
            ItemData("2", R.drawable.ic_launcher_background),
            ItemData("3", R.drawable.ic_launcher_background),
            ItemData("4", R.drawable.ic_launcher_background),
            ItemData("5", R.drawable.ic_launcher_background),
            ItemData("6", R.drawable.ic_launcher_background),
            ItemData("7", R.drawable.ic_launcher_background),
            ItemData("8", R.drawable.ic_launcher_background),
        )
        matchesBinding.matchesView.adapter = RecyclerViewAdapter(itemsMatches)

        setContentView(matchesBinding.root)
    }
}