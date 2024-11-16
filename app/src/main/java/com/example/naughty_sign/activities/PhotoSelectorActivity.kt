package com.example.naughty_sign.activities

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.naughty_sign.databinding.ActivityPhotoSelectorBinding

class PhotoSelectorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPhotoSelectorBinding

    // Lanzador para seleccionar múltiples imágenes
    private val seleccionarImagenes = registerForActivityResult(
        ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri>? -> // Recibimos una lista de URIs seleccionadas
        if (uris.isNullOrEmpty()) {
            Toast.makeText(this, "No se seleccionaron imágenes", Toast.LENGTH_SHORT).show()
        } else {
            // Iteramos sobre las imágenes seleccionadas y mostramos sus URIs
            uris.forEach { uri ->
                Toast.makeText(this, "Imagen seleccionada: $uri", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Se inicializan variables y se preparan recursos no relacionados con la vista.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoSelectorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addPhotoButton.setOnClickListener() {
            seleccionarImagenes.launch("image/*") // Lanzamos el seleccionador de imágenes
        }

        binding.removePhotoButton.setOnClickListener() {
            seleccionarImagenes.launch("image/*") // Lanzamos el seleccionador de imágenes
        }

    }
}