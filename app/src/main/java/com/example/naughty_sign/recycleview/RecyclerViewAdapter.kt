package com.example.naughty_sign.recycleview

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.naughty_sign.LikeMatchProfileActivity
import com.example.naughty_sign.R
import com.example.naughty_sign.databinding.ItemCardViewBinding
import com.example.naughty_sign.json.User

class RecyclerViewAdapter(private val items: List<User>, private val fromFragment: String) :
    RecyclerView.Adapter<ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding =
            ItemCardViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = items[position]

        holder.bind(currentItem)

        Glide.with(holder.itemView.context)
            .load(currentItem.foto_perfil)
            .transform(RoundedCorners(100))
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(holder.binding.userPhoto)

        holder.itemView.setOnClickListener {
            // Crear un Intent para iniciar la actividad
            val context = holder.itemView.context
            val intent = Intent(context, LikeMatchProfileActivity::class.java)

            // Agregar datos al Intent
            intent.putExtra("userIdParam", currentItem.id)
            intent.putExtra(
                "fromFragmentPram",
                fromFragment
            )

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = items.size
}