package com.example.naughty_sign

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.naughty_sign.databinding.FragmentMatchProfileBinding
import com.example.naughty_sign.json.RetrofitInstance
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.chip.Chip
import kotlinx.coroutines.launch

/**
 * Activity que se encarga de depositar los datos del usuario que se encuentra en Likes o Matches.
 * Dependiendo si viene de un fragmetno u otro, se cargará el mapa.
 */
class LikeMatchProfileActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: FragmentMatchProfileBinding
    private var userIdParam: Int = -1
    private var fromFragmentPram: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /* Se infla el XML para obtener las clases con las que interactuar. */
        binding = FragmentMatchProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*
        * Extraigo del intent con el que se ha enlazado a esta actividad, el ID del usuario
        * y el fragmento de donde proviene.
        * */
        userIdParam = intent.getIntExtra("userIdParam", -1)
        fromFragmentPram = intent.getStringExtra("fromFragmentPram")

        /*
        * Dependiendo de si el parámetro extraido del fragmento proveniente es `Likes` o `Matches`,
        * se creará el mapa o no.
        * */
        if (fromFragmentPram.equals("Matches", true)) {
            binding.map.onCreate(savedInstanceState)
            binding.map.getMapAsync(this)
        }

        /*
        * Finalmente carga el perfil para que se pueda ver.
        * */
        loadProfile()
    }

    /**
     * Carga el perfil del usuario tras leer el JSON del servidor.
     */
    private fun loadProfile() {
        lifecycleScope.launch {
            try {
                val response = RetrofitInstance().api.getUsers()
                if (response.isSuccessful) {
                    response.body()?.let { users ->
                        for (user in users) {
                            if (user.id == userIdParam) {
                                binding.profileName.text = user.nombre
                                binding.profileQuote.text = user.cita
                                binding.profileProfession.text = user.profesion
                                binding.profileCity.text = user.ciudad
                                binding.profileDescription.text = user.descripcion

                                val imageUrl = user.foto_perfil
                                Glide.with(this@LikeMatchProfileActivity).load(imageUrl)
                                    .transform(CircleCrop()).placeholder(R.drawable.thumb_up)
                                    .error(R.drawable.moon).into(binding.imageView)

                                for (interest in user.intereses) {
                                    val chipInteres = Chip(this@LikeMatchProfileActivity).apply {
                                        text = interest
                                        isCloseIconVisible = false
                                        setChipBackgroundColorResource(R.color.seashell)
                                        textSize = 9F
                                        setTextColor(R.color.dark_orange.toInt())
                                        setChipStrokeColorResource(R.color.silver)
                                        chipStrokeWidth = 2f
                                        setPadding(20, 10, 20, 10)
                                        isClickable = false
                                        isCheckable = false
                                    }
                                    binding.chipGroup.addView(chipInteres)
                                }

                                if (fromFragmentPram.equals("Matches", true)) {
                                    val coordinates = extractCoordinatesFromUrl(user.ubicacion)
                                    coordinates?.let { (lat, lon) ->
                                        val userLocation = LatLng(lat, lon)
                                        binding.map.getMapAsync { googleMap ->
                                            googleMap.addMarker(
                                                MarkerOptions().position(userLocation)
                                                    .title("Ubicación de ${user.nombre}")
                                            )
                                            googleMap.moveCamera(
                                                CameraUpdateFactory.newLatLngZoom(userLocation, 8f)
                                            )
                                        }
                                    }
                                } else {
                                    binding.map.visibility = View.INVISIBLE
                                }
                            }
                        }
                    }
                } else {
                    Log.e("API ERROR", "ERROR: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("NETWORK ERROR", "Exception: $e")
            }
        }
    }

    /**
     * Método que se encarga de configurar el mapa según las necesidades una vez esté hecho.
     */
    override fun onMapReady(mapa: GoogleMap) {
        mapa.uiSettings.apply {
            /* Desactiva el desplazamiento */
            isScrollGesturesEnabled = false
            /* Habilita el zoom (pellizcar para acercar o alejar) */
            isZoomGesturesEnabled = false
            /* Desactiva la inclinación */
            isTiltGesturesEnabled = false
            /* Desactiva la rotación */
            isRotateGesturesEnabled = false
        }
    }

    /**
     * Extrae las coordenadas de la posición que puso el usaurio usando expresiones regulares.
     */
    private fun extractCoordinatesFromUrl(url: String): Pair<Double, Double>? {
        val regex = """@(-?\d+\.\d+),(-?\d+\.\d+)""".toRegex()
        val matchResult = regex.find(url)
        return matchResult?.let {
            val lat = it.groupValues[1].toDouble()
            val lon = it.groupValues[2].toDouble()
            Pair(lat, lon)
        }
    }
}
