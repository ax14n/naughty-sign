package com.example.naughty_sign.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.iterator
import com.example.naughty_sign.R
import com.example.naughty_sign.databinding.ActivityInterestsBinding
import com.example.naughty_sign.utils.LoggedUserUtils
import com.example.naughty_sign.utils.MessageUtils
import com.google.android.material.chip.Chip

class InterestsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInterestsBinding

    /**
     * Se inicializan variables y se preparan recursos no relacionados con la vista.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInterestsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val interests = listOf(
            "Derecho ⚖️",
            "Viajes ✈️",
            "Cine 🎬",
            "Deportes 🏅",
            "Cultura 📚",
            "Arquitectura 🏛️",
            "Fotografía 📸",
            "Arte 🎨",
            "Música 🎶",
            "Psicología 🧠",
            "Meditación 🧘",
            "Diseño 🖌️",
            "Moda 👗",
            "Teatro 🎭",
            "Baile 💃",
            "Naturaleza 🌿",
            "Educación 🎓",
            "Fútbol ⚽",
            "Gastronomía 🍤",
            "Periodismo 📰",
            "Noticias 📡"
        )

        for (interest in interests) {
            val chip: Chip = Chip(this).apply {
                text = interest
                tag = interest
                setChipBackgroundColorResource(R.color.mint_cream)
                setChipStrokeColorResource(R.color.gray)
                setChipStrokeWidth(2f)
            }

            chip.setOnClickListener {
                if (chip.isSelected) {
                    chip.setSelected(false)
                    chip.setChipBackgroundColorResource(R.color.mint_cream)
                } else {
                    chip.setSelected(true)
                    chip.setChipBackgroundColorResource(R.color.light_sea_green)
                }
            }
            binding.chipGroup.addView(chip)
        }

        binding.updateInterests.setOnClickListener {
            actualizarIntereses()
        }

    }

    /**
     * Actualiza los intereses de la persona.
     */
    private fun actualizarIntereses() {
        val intereses = mutableListOf<String>()
        for (chip in binding.chipGroup.iterator()) {
            if (chip.isSelected) intereses.add(chip.tag.toString())
        }
        LoggedUserUtils.actualizar("intereses", intereses)
        MessageUtils.mostrarToast(this, "Se han actualizado los intereses")
        finish()
    }
}