package com.example.naughty_sign.fragments

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.naughty_sign.databinding.FragmentProfileSwipeBinding
import com.example.naughty_sign.utils.LoggedUserUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentProfileSwipe.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentProfileSwipe : Fragment() {

    /**
     * Binding del fragmento. Almacena dentro de sí todas las clases necesarias para interacutar
     * con la interfáz y otros elementos lógicos del programa que se encuentren presentes dentro
     * del a interfaz.
     */
    private lateinit var binding: FragmentProfileSwipeBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProfileSwipeBinding.inflate(layoutInflater)

        // ------- { Asignación de boton de Like! } ------- //
        val animation_corason = binding.corason.background as AnimationDrawable
        binding.btnLike.setOnClickListener {
            LoggedUserUtils.almacenarLike("")
            cambiarUsuario()
            binding.corason.visibility= View.VISIBLE
            animation_corason.start()
            CoroutineScope(Dispatchers.Main).launch {
                Thread.sleep(1_000)  // wait for 1 second
                binding.corason.visibility= View.INVISIBLE
            }
        }



        // ------- { Asignación de boton de Next! } ------- //
        binding.btnDislike.setOnClickListener {
            // TODO: Nos vamos a inmolar porque vamos a tener que montar un servidor.
            cambiarUsuario()
        }

        // ------- { Puesta en escena de la vista } ------- //
        return binding.root
    }

    /**
     * Cambia el usuario que se muestra por pantalla y mostrando el siguiente candidato.
     */
    private fun cambiarUsuario() {

    }

}