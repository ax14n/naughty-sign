package com.example.naughty_sign.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.naughty_sign.databinding.FragmentMatchesBinding
import com.example.naughty_sign.firebase.User
import com.example.naughty_sign.recycleview.RecyclerViewAdapter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private val profile = Firebase.auth.currentUser

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentMatches.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentMatches : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var binding: FragmentMatchesBinding? = null
    private var db = Firebase.firestore

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
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMatchesBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    /**
     * Se realiza la configuración adicional de la vista, como establecer adaptadores y manejar eventos.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.matchesView?.layoutManager = LinearLayoutManager(this.context)

        loadMatches()
    }

    private fun loadMatches() {

        db.collection("Usuarios").get().addOnSuccessListener { result ->
            val listaUsuarios: MutableList<User> = mutableListOf()
            for (document in result) {
                if (!profile!!.email.equals(document.get("email").toString())) {
                    var user = User(
                        "",
                        Integer.parseInt(document.get("id").toString()),
                        document.get("nombre").toString(),
                        document.get("cita").toString(),
                        document.get("profesion").toString(),
                        document.get("ciudad").toString(),
                        document.get("descripcion").toString(),
                        document.get("intereses") as List<String>,
                        document.get("foto_perfil").toString(),
                        document.get("ubicacion").toString(),
                        Integer.parseInt(document.get("edad").toString())
                    )
                    listaUsuarios.add(user)
                }
            }
            binding?.matchesView?.adapter = RecyclerViewAdapter(listaUsuarios, "Matches")
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentMatches.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) = FragmentMatches().apply {
            arguments = Bundle().apply {
                putString(ARG_PARAM1, param1)
                putString(ARG_PARAM2, param2)
            }
        }
    }
}