package com.example.naughty_sign

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cardview.ItemData
import com.example.cardview.RecyclerViewAdapter
import com.example.naughty_sign.databinding.ActivityMainBinding
import com.example.naughty_sign.databinding.FragmentLikesBinding
import com.example.naughty_sign.databinding.FragmentMatchesBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding;
    private lateinit var likesBinding: FragmentLikesBinding;
    private lateinit var matchesBinding: FragmentMatchesBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        likesBinding = FragmentLikesBinding.inflate(layoutInflater)
        matchesBinding = FragmentMatchesBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        setContentView(binding.root)

        // Configura el NavController con el BottomNavigationView
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Conectar el BottomNavigationView con el NavController
        binding.bottomNavigation.setupWithNavController(navController)
    }
}