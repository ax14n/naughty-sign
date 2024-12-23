package com.example.naughty_sign.activities

import android.content.Intent
import android.os.Bundle
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

        //-------------- { Asignación de funcionabilidad del botón } --------------//
        binding.finishPostRegister.setOnClickListener {

            /**
             * Crea un nuevo documento en la base de datos de Firestore con los datos mínimos
             * requeridos insertados en la pantalla de esta actividad.
             * -------------------------------------------------------------------------------
             * Requistos para agregar el usuario a la colección:
             *
             * -> No dejar sin rellenar el campo del nombre.
             * -> No dejar sin rellenar el campo de la pequeña cita.
             */
            if (binding.name.text.isNotBlank() && binding.smallQuote.text.isNotBlank()) {

                //----------- { Almacenamiento de datos mínimos necesarios } -----------//
                val bundle = Bundle().apply {
                    putString("id", LoggedUserUtils.obtenerUid())
                    putString("email", LoggedUserUtils.obtenerEmail())
                    putString("nombre", binding.name.text.toString())
                    putString("cita", binding.smallQuote.text.toString())
                    putInt("edad", binding.agePicker.value)
                }

                //----------- { Creación del documento de la base de datos } -----------//
                LoggedUserUtils.crearDocumentoUsuario(bundle)

                //----------- { Cambio de actividad } -----------//
                startActivity(Intent(this, LogInActivity::class.java))

            } else MessageUtils.mostrarToast(this, "No se han rellenado los campos necesarios")
        }

        // Se aplica la visual a la actividad actual.
        setContentView(binding.root)
    }

}