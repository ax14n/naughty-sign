package com.example.naughty_sign

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.naughty_sign.databinding.FragmentMatchProfileBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.material.chip.Chip
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val PARAM1 = "userId"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentMatchProfile.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentMatchProfile : Fragment(), OnMapReadyCallback {
    // TODO: Rename and change types of parameters
    private var param1: Int? = null

    private lateinit var binding: FragmentMatchProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*
        * Obtengo el parámetro que se lo asignó a este fragmento mediante la clave
        * */
        arguments?.let {
            param1 = it.getInt(PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMatchProfileBinding.inflate(inflater, container, false)
        binding.map.onCreate(savedInstanceState)
        binding.map.getMapAsync(this)
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
                            if (user.id == param1) {
                                binding.profileName.text = user.nombre
                                binding.profileQuote.text = user.cita
                                binding.profileProfession.text = user.profesion
                                binding.profileCity.text = user.ciudad
                                binding.profileDescription.text = user.descripcion

                                /*
                                * Obtengo los intereses del usuario y los recorro para formar chips
                                * */
                                for (interest in user.intereses) {
                                    val chipInteres = Chip(requireContext())
                                    chipInteres.text = interest
                                    chipInteres.isCloseIconVisible = false

                                    // Ajusta los colores para mejorar la estética
                                    chipInteres.setChipBackgroundColorResource(R.color.seashell) // Fondo más suaveç
                                    chipInteres.setTextSize(9F)
                                    chipInteres.setTextColor(R.color.dark_orange.toInt()) // Texto contrastante
                                    chipInteres.setChipStrokeColorResource(R.color.silver) // Borde sutil
                                    chipInteres.setChipStrokeWidth(2f) // Grosor del borde
                                    chipInteres.setPadding(20, 10, 20, 10) // Ajustar el padding

                                    // Desactivar la selección
                                    chipInteres.isClickable = false
                                    chipInteres.isCheckable = false

                                    binding.chipGroup.addView(chipInteres)

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
        fun newInstance(param1: Int) =
            FragmentMatchProfile().apply {
                arguments = Bundle().apply {
                    putInt(PARAM1.toString(), param1)
                }
            }
    }


    override fun onMapReady(p0: GoogleMap) {

    }
}