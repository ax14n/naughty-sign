package com.example.naughty_sign

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cardview.ItemData
import com.example.cardview.RecyclerViewAdapter
import com.example.naughty_sign.databinding.FragmentMatchesBinding
import kotlinx.coroutines.launch
import org.json.JSONArray

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

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

    /**
     * Analiza una cadena JSON que representa una lista de elementos y la convierte en una lista de objetos ItemData.
     *
     * Esta función toma una cadena formateada en JSON que contiene un array de objetos, cada uno representando
     * a un individuo con atributos como nombre, edad y ciudad. Crea y llena una lista de objetos ItemData
     * basándose en la información extraída.
     *
     * @param jsonData Una cadena JSON que contiene un array de objetos con los campos: nombre, edad y ciudad.
     * @return Una lista de objetos ItemData, cada uno poblado con los valores correspondientes del JSON.
     */
    private fun parseJsonToItemData(jsonData: String): List<ItemData> {
        //Creo una lista mutable para almacenar los objetos ItemData
        val itemList = mutableListOf<ItemData>()

        //Convierto el String del JSON en un array de JSON
        val jsonArray = JSONArray(jsonData)

        //Recorro cada elemento del array al contrario para que me muestre ejemplos diferentes de Matches
        for (i in jsonArray.length() - 1 downTo 0) {

            //Obtengo el objeto actual y extraigo sus valores
            val jsonObject = jsonArray.getJSONObject(i)
            val name = jsonObject.getString("name")
            val age = jsonObject.getInt("age")
            val city = jsonObject.getString("city")

            val imageResId =
                R.drawable.ic_launcher_background // Reemplazar con un recurso de imagen real
            itemList.add(ItemData(name, age, city, imageResId))
        }
        return itemList
    }

    private fun loadMatches() {
        lifecycleScope.launch {
            try {
                val response = RetrofitInstance().api.getMatches()
                if (response.isSuccessful) {
                    response.body()?.let { matches ->
                        binding?.matchesView?.adapter = RecyclerViewAdapter(matches)
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
         * @return A new instance of fragment FragmentMatches.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentMatches().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}