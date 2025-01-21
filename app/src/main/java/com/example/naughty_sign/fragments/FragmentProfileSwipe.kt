package com.example.naughty_sign.fragments

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.animation.doOnEnd
import androidx.fragment.app.Fragment
import com.example.naughty_sign.databinding.FragmentProfileSwipeBinding
import com.example.naughty_sign.utils.LoggedUserUtils

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
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProfileSwipeBinding.inflate(layoutInflater)

        // ------- { Asignación de boton de Like! } ------- //

        binding.btnLike.setOnClickListener {
            LoggedUserUtils.almacenarLike("")
            cambiarUsuario()
            val scaleX = ObjectAnimator.ofFloat(binding.corason, "scaleX", 1f, 0.5f)
            val scaleY = ObjectAnimator.ofFloat(binding.corason, "scaleY", 1f, 0.5f)

            binding.corason.visibility = View.VISIBLE

            scaleX.repeatCount = 3
            scaleY.repeatCount = 3
            val animatorSet = AnimatorSet().also {
                it.play(scaleX).with(scaleY)
                it.start()
            }
            animatorSet.doOnEnd {
                binding.corason.visibility = View.GONE
            }

        }

//        val animation_corason = binding.corason. as AnimationDrawable
//            binding.corason.visibility = View.VISIBLE
//            animation_corason.start() // Se inicia
////            CoroutineScope(Dispaxtchers.Main).launch {
//            Log.d("HOLA", "Se ha lanzado el hilo")
//            binding.corason.visibility = View.INVISIBLE
//            Thread.sleep(1_000)  // wait for 1 second
////            }
        // ------- { Asignación de boton de Next! } ------- //
        binding.btnDislike.setOnClickListener {
            cambiarUsuario()
            val scaleX = ObjectAnimator.ofFloat(binding.corason, "scaleX", 1f, 0.5f)
            val scaleY = ObjectAnimator.ofFloat(binding.corason, "scaleY", 1f, 0.5f)

            binding.corason.visibility = View.VISIBLE

            scaleX.repeatCount = 3
            scaleY.repeatCount = 3
            val animatorSet = AnimatorSet().also {
                it.play(scaleX).with(scaleY)
                it.start()
            }
            animatorSet.doOnEnd {
                binding.corason.visibility = View.GONE
            }
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