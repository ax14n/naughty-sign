package com.example.naughty_sign.recycleview

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.naughty_sign.LikeMatchProfileActivity
import com.example.naughty_sign.databinding.ItemCardViewBinding

class RecyclerViewAdapter(private val items: List<ItemData>, private val fromFragment: String) :
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
            // Crear un Intent para iniciar la actividad
            val context = holder.itemView.context
            val intent = Intent(context, LikeMatchProfileActivity::class.java)

            // Agregar datos al Intent
            intent.putExtra("userIdParam", currentItem.id)
            intent.putExtra(
                "fromFragmentPram",
                fromFragment
            ) // Reemplaza con el nombre actual del fragmento si es necesario

            // Iniciar la actividad
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = items.size
}