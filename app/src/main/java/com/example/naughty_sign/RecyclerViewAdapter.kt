    package com.example.cardview

    import android.view.LayoutInflater
    import android.view.ViewGroup
    import android.widget.Toast
    import androidx.recyclerview.widget.RecyclerView
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
                Toast.makeText(
                    holder.itemView.context,
                    "Clic en: ${currentItem.id}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        override fun getItemCount(): Int = items.size
    }