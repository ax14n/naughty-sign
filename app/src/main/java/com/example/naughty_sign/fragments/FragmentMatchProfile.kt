package com.example.naughty_sign.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.naughty_sign.R
import com.example.naughty_sign.databinding.FragmentMatchProfileBinding
import com.example.naughty_sign.json.RetrofitInstance
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.chip.Chip
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val PARAM1 = "userIdParam"
private const val PARAM2 = "fromFragmentPram"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentMatchProfile.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentMatchProfile : Fragment(), OnMapReadyCallback {
    // TODO: Rename and change types of parameters
    private var userIdParam: Int? = null
    private var fromFragmentPram: String? = null

    private lateinit var binding: FragmentMatchProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*
        * Obtengo el parámetro que se lo asignó a este fragmento mediante la clave
        * */
        arguments?.let {
            userIdParam = it.getInt(PARAM1)
            fromFragmentPram = it.getString(PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMatchProfileBinding.inflate(inflater, container, false)

        if (fromFragmentPram.equals("Matches", true)) {
            binding.map.onCreate(savedInstanceState)
            binding.map.getMapAsync(this)
        }
        loadProfile()
        return binding.root
    }

    /**
     * Carga el pefil del usuario tras leer el JSON del servidor.
     */
    private fun loadProfile() {
        /*
        * Inicia una corutina
        * */
        lifecycleScope.launch {
            try {

                /*
                * Almaceno la lista de usuarios y, si la respuesta ha sido efectiva, relleno el perfil
                * con los datos del usuario.
                * */
                val response = RetrofitInstance().api.getUsers()
                if (response.isSuccessful) {
                    response.body()?.let { users ->
                        /*
                        * Recorro los usuarios y relleno su perfil según el ID.
                        * */
                        for (user in users) {
                            if (user.id == userIdParam) {
                                binding.profileName.text = user.nombre
                                binding.profileQuote.text = user.cita
                                binding.profileProfession.text = user.profesion
                                binding.profileCity.text = user.ciudad
                                binding.profileDescription.text = user.descripcion

                                // Cargar la imagen en el ImageView
                                val imageUrl =
                                    user.foto_perfil  // Suponiendo que tienes este campo en tu modelo
                                Glide.with(requireContext())
                                    .load(imageUrl) // Aquí va la URL de la imagen
                                    .transform(CircleCrop()) // Redondear la imagen completamente
                                    .placeholder(R.drawable.thumb_up)  // Imagen de carga
                                    .error(R.drawable.moon)  // Imagen de error
                                    .into(binding.imageView) // Aquí va tu ImageView


                                /*
                                * Obtengo los intereses del usuario y los recorro para formar chips
                                * */
                                for (interest in user.intereses) {
                                    val chipInteres = Chip(requireContext())
                                    chipInteres.text = interest
                                    chipInteres.isCloseIconVisible = false

                                    // Ajusta los colores para mejorar la estética
                                    chipInteres.setChipBackgroundColorResource(R.color.seashell) // Fondo más suaveç
                                    chipInteres.textSize = 9F
                                    chipInteres.setTextColor(R.color.dark_orange.toInt()) // Texto contrastante
                                    chipInteres.setChipStrokeColorResource(R.color.silver) // Borde sutil
                                    chipInteres.chipStrokeWidth = 2f // Grosor del borde
                                    chipInteres.setPadding(20, 10, 20, 10) // Ajustar el padding

                                    // Desactivar la selección
                                    chipInteres.isClickable = false
                                    chipInteres.isCheckable = false

                                    binding.chipGroup.addView(chipInteres)

                                }

                                if (fromFragmentPram.equals("Matches", true)) {
                                    // Extraer las coordenadas de la URL
                                    val coordinates = extractCoordinatesFromUrl(user.ubicacion)
                                    coordinates?.let {
                                        val (lat, lon) = it
                                        val userLocation = LatLng(lat, lon)

                                        // Añadir el marcador en el mapa
                                        binding.map.getMapAsync { googleMap ->
                                            googleMap.addMarker(
                                                MarkerOptions().position(userLocation)
                                                    .title("Ubicación de ${user.nombre}")
                                            )
                                            googleMap.moveCamera(
                                                CameraUpdateFactory.newLatLngZoom(
                                                    userLocation,
                                                    8f
                                                )
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
                    Log.e("API ERROR", "ERROR:  ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("NETWORK ERROR", "Exception: $e")
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @return A new instance of fragment FragmentMatchProfile.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: Int, param2: String) =
            FragmentMatchProfile().apply {
                arguments = Bundle().apply {
                    putInt(PARAM1.toString(), param1)
                    putString(PARAM2.toString(), param2)
                }
            }
    }


    override fun onMapReady(p0: GoogleMap) {

    }

    fun extractCoordinatesFromUrl(url: String): Pair<Double, Double>? {
        // Usamos una expresión regular para extraer la latitud y longitud
        val regex = """@(-?\d+\.\d+),(-?\d+\.\d+)""".toRegex()

        val matchResult = regex.find(url)

        return if (matchResult != null) {
            val lat = matchResult.groupValues[1].toDouble()
            val lon = matchResult.groupValues[2].toDouble()
            Pair(lat, lon)
        } else {
            null
        }
    }

}