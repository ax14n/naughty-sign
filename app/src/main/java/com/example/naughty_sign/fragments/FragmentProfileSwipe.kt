package com.example.naughty_sign.fragments

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.animation.doOnEnd
import androidx.fragment.app.Fragment
import com.example.naughty_sign.R
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

//            // Sound
//            val soundPull = SoundPool.Builder().setMaxStreams(1).build()
//            val soundId = soundPull.load(requireContext(), R.raw.rain, 1)
//
//            soundPull.setOnLoadCompleteListener { _, _, status ->
//                if (status == 0)
//                    soundPull.play(soundId, 1f, 1f, 0, 0, 1f)
//            }

            try {
                val mediaplayer = MediaPlayer.create(context, R.raw.bubble)
                mediaplayer.start()
                mediaplayer.setOnCompletionListener {
                    mediaplayer.release()
                }
            } catch (e: Exception) {
                Log.d("MediaPlayer", e.stackTraceToString())
            }

            val scaleX = ObjectAnimator.ofFloat(binding.corazonLike, "scaleX", 1f, 0.5f)
            val scaleY = ObjectAnimator.ofFloat(binding.corazonLike, "scaleY", 1f, 0.5f)

            binding.corazonLike.visibility = View.VISIBLE

            scaleX.repeatCount = 3
            scaleY.repeatCount = 3
            val animatorSet = AnimatorSet().also {
                it.play(scaleX).with(scaleY)
                it.start()
            }
            animatorSet.doOnEnd {
                binding.corazonLike.visibility = View.GONE
            }

        }

        // ------- { Asignación de boton de Next! } ------- //
        binding.btnDislike.setOnClickListener {
            cambiarUsuario()

            try {
                val mediaplayer = MediaPlayer.create(context, R.raw.bubble)
                mediaplayer.start()
                mediaplayer.setOnCompletionListener {
                    mediaplayer.release()
                }
            } catch (e: Exception) {
                Log.d("MediaPlayer", e.stackTraceToString())
            }

            val scaleX = ObjectAnimator.ofFloat(binding.caraDislike, "scaleX", 1f, 0.5f)
            val scaleY = ObjectAnimator.ofFloat(binding.caraDislike, "scaleY", 1f, 0.5f)

            binding.caraDislike.visibility = View.VISIBLE

            scaleX.repeatCount = 3
            scaleY.repeatCount = 3
            val animatorSet = AnimatorSet().also {
                it.play(scaleX).with(scaleY)
                it.start()
            }
            animatorSet.doOnEnd {
                binding.caraDislike.visibility = View.GONE
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