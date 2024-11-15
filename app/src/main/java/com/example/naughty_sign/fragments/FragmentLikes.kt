package com.example.naughty_sign.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.naughty_sign.databinding.FragmentLikesBinding
import com.example.naughty_sign.json.RetrofitInstance
import com.example.naughty_sign.recycleview.RecyclerViewAdapter
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentLikes.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentLikes : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var binding: FragmentLikesBinding? = null

    /**
     * Se inicializan variables y se preparan recursos no relacionados con la vista.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    /**
     * Se infla la vista del fragmento, creando su interfaz visual.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLikesBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    /**
     * Se realiza la configuración adicional de la vista, como establecer adaptadores y manejar eventos.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.likesView?.layoutManager = LinearLayoutManager(this.context)

        loadLikes()
    }

    private fun loadLikes() {
        lifecycleScope.launch {
            try {
                val response = RetrofitInstance().api.getLikes()
                if (response.isSuccessful) {
                    response.body()?.let { likes ->
                        binding?.likesView?.adapter = RecyclerViewAdapter(likes)
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
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentLikes.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentLikes().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}