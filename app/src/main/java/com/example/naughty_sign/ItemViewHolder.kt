package com.example.cardview

import androidx.recyclerview.widget.RecyclerView
import com.example.cardview.databinding.ActivityMainBinding
import com.example.cardview.databinding.ItemCardviewBinding

class ItemViewHolder(private val binding: ItemCardviewBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item :ItemData){
        binding.item = item
        binding.executePendingBindings()
    }
}