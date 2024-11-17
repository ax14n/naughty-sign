package com.example.naughty_sign.recycleview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.naughty_sign.R
import com.example.naughty_sign.databinding.ItemCardViewBinding

class RecyclerViewAdapter(private val items: List<ItemData>) :
    RecyclerView.Adapter<ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding =
            ItemCardViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = items[position]

        holder.bind(currentItem)

        holder.itemView.setOnClickListener {
            // Obtener el NavController
            val navController = Navigation.findNavController(holder.itemView)
            val currentFragmentName = navController.currentDestination?.label.toString()

            // Crear un bundle con el ID del usuario
            val bundle = Bundle().apply {
                putInt("userIdParam", currentItem.id)
                putString("fromFragmentPram", currentFragmentName)
            }

            // Navegar al FragmentMatchProfile pasando el ID
            navController.navigate(R.id.match_profile_fragment, bundle)
        }
    }

    override fun getItemCount(): Int = items.size
}