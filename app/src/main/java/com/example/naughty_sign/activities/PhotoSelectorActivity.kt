package com.example.naughty_sign.activities

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.naughty_sign.databinding.ActivityPhotoSelectorBinding
import com.example.naughty_sign.utils.LoggedUserUtils
import com.example.naughty_sign.utils.MessageUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.DataOutputStream
import java.io.InputStream
import java.net.Socket

class PhotoSelectorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPhotoSelectorBinding

    /**
     * Se inicializan variables y se preparan recursos no relacionados con la vista.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoSelectorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addPhotoButton.setOnClickListener {
            seleccionarImagenes.launch("image/*") // Lanzamos el seleccionador de imágenes
        }

        binding.removePhotoButton.setOnClickListener {
            seleccionarImagenes.launch("image/*") // Lanzamos el seleccionador de imágenes
        }

    }

    /**
     * Lanzador para seleccionar múltiples imágenes.
     */
    private val seleccionarImagenes = registerForActivityResult(
        ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri>? -> // Recibimos una lista de URIs seleccionadas
        if (uris.isNullOrEmpty()) {
            MessageUtils.mostrarToast(this, "No se seleccionaron imágenes")
        } else {
            enviarImagenesPorSocket(uris)
        }
    }

    /**
     * Envía las imágenes seleccionadas por un socket en un hilo secundario.
     */
    private fun enviarImagenesPorSocket(uris: List<Uri>) {
        // ------- { Se hace un hilo secundario para las operaciones } ------- //
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // ------- { Se conecta a un socket y se establece un Writer. } ------- //
                val client = Socket("192.168.1.172", 23457)
                val outputStream = DataOutputStream(client.getOutputStream())

                // ------- { Se imprime el UUID de usuario para identificar donde guardarlo. } ------- //
                val userUID = LoggedUserUtils.obtenerUid()
                outputStream.writeUTF(userUID)

                // ------- { Se envía la información al servidor y se reconstruye allí. } ------- //
                uris.forEach { uri ->
                    val inputStream: InputStream? = contentResolver.openInputStream(uri)
                    if (inputStream != null) {

                        // ------- { Obtención de los datos necesarios de la imagen. } ------- //
                        val fileBytes = inputStream.readBytes()
                        val fileName = uri.lastPathSegment ?: "imagen_sin_nombre"

                        // ------- { Se envía el nombre del archivo, su tamaño y su contenido. } ------- //
                        outputStream.writeUTF(fileName)
                        outputStream.writeLong(fileBytes.size.toLong())
                        outputStream.write(fileBytes)
                    } else {
                        Log.d("ax14n", "No se pudo abrir la imagen: $uri")
                    }
                }

                // ------- { Se pone final y se cierra el socket de momento. } ------- //
                outputStream.writeUTF("EOF")
                outputStream.flush()
                client.close()

                Log.d("ax14n", "Todas las imágenes fueron enviadas correctamente.")
            } catch (e: Exception) {
                Log.e("ax14n", "Error enviando imágenes: ${e.message}", e)
            }
        }
    }


}