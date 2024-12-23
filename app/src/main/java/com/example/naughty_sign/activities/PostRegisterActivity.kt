package com.example.naughty_sign.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.naughty_sign.databinding.ActivityPostRegisterBinding
import com.example.naughty_sign.utils.LoggedUserUtils
import com.example.naughty_sign.utils.MessageUtils

/**
 * Clase encargada de enlazar y administrar los datos mínimos necesarios para el perfil del usuario
 * que acaba de crear su cuenta.
 */
class PostRegisterActivity : AppCompatActivity() {

    /**
     * Encargada de almacenar las clases presentes en la interfáz y llamarlas para
     * poder interactuar con ellas.
     */
    private lateinit var binding: ActivityPostRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //------------- { Inicialización de variables } -------------//
        binding = ActivityPostRegisterBinding.inflate(layoutInflater)

        //-------------- { Asignación de valor mínimo y máximo del Age Picker } --------------//
        binding.agePicker.minValue = 18     // VALOR MÍNIMO.
        binding.agePicker.maxValue = 99     // VALOR MÁXIMO.

        //-------------- { Asignación de valores de Spinners } --------------//
        val ciudades = arrayOf(
            "Madrid",
            "Barcelona",
            "Valencia",
            "Sevilla",
            "Zaragoza",
            "Málaga",
            "Murcia",
            "Palma",
            "Las Palmas de Gran Canaria",
            "Bilbao",
            "Alicante",
            "Córdoba",
            "Valladolid",
            "Vigo",
            "Gijón",
            "L'Hospitalet de Llobregat",
            "A Coruña",
            "Granada",
            "Elche",
            "Oviedo"
        )
        val profesiones = arrayOf(
            "Abogado",
            "Arquitecto",
            "Ingeniero",
            "Médico",
            "Enfermero",
            "Profesor",
            "Cocinero",
            "Escritor",
            "Artista",
            "Diseñador gráfico",
            "Desarrollador de software",
            "Contador",
            "Psicólogo",
            "Científico",
            "Periodista",
            "Fotógrafo",
            "Electricista",
            "Carpintero"
        )

        binding.citySpinner.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, ciudades)
        binding.profesionSpinner.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, profesiones)

        //-------------- { Asignación de funcionabilidad del botón } --------------//
        binding.finishPostRegister.setOnClickListener {

            val validations = listOf(
                binding.name.text to "¡Aún no nos has dicho tu nombre!",
                binding.smallQuote.text to "¿Te has olvidado de poner una pequeña frase?",
                binding.descripcion.text to "¡Cuentame más sobre tí! Descripción sin rellenar"
            )

            val errorMessage = validations.firstOrNull { it.first.isBlank() }?.second

            if (errorMessage != null) {
                MessageUtils.mostrarToast(this, errorMessage)
            } else {
                //----------- { Almacenamiento de datos mínimos necesarios } -----------//
                val bundle = Bundle().apply {
                    putString("id", LoggedUserUtils.obtenerUid())
                    putString("email", LoggedUserUtils.obtenerEmail())
                    putString("nombre", binding.name.text.toString())
                    putString("cita", binding.smallQuote.text.toString())
                    putInt("edad", binding.agePicker.value)
                    putString("descripcion", binding.descripcion.text.toString())
                    putString("ciudad", binding.citySpinner.selectedItem.toString())
                    putString("profesion", binding.profesionSpinner.selectedItem.toString())
                }

                //----------- { Creación del documento de la base de datos } -----------//
                LoggedUserUtils.creacionDocumentoUsuario(bundle)

                //----------- { Cambio de actividad } -----------//
                startActivity(Intent(this, MainActivity::class.java))

            }
        }

        // Se aplica la visual a la actividad actual.
        setContentView(binding.root)
    }
}
