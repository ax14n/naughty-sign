package com.example.cardview

import androidx.recyclerview.widget.RecyclerView
import com.example.naughty_sign.databinding.ItemCardViewBinding

class ItemViewHolder(private val binding: ItemCardViewBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item :ItemData){
        binding.item = item
        binding.executePendingBindings()
    }
}