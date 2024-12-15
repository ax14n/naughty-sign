package com.example.naughty_sign.recycleview

import androidx.recyclerview.widget.RecyclerView
import com.example.naughty_sign.databinding.ItemCardViewBinding
import com.example.naughty_sign.firebase.User

class ItemViewHolder(val binding: ItemCardViewBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: User) {
        binding.item = item
        binding.executePendingBindings()
    }
}