package com.example.naughty_sign.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.naughty_sign.databinding.ActivityPostRegisterBinding
import com.example.naughty_sign.firebase.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

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

    /**
     * Objeto que se encarga de las operaciones con la base de datos Firestore.
     */
    private val db = Firebase.firestore

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
                createUser()
                startActivity(Intent(this, LogInActivity::class.java))
            } else Toast.makeText(
                baseContext,
                "No se han rellenado los campos.",
                Toast.LENGTH_SHORT,
            ).show()
        }

        // Se aplica la visual a la actividad actual.
        setContentView(binding.root)
    }

    /**
     * Se crea un nuevo usuario dentro de la base de datos con los datos mínimos necesarios.
     */
    private fun createUser() {
        // Obtengo la colección llamada 'Usuarios'.
        val collection = db.collection("Usuarios")

        // Operación de agregación del usuario a la base de datos.
        collection.get().addOnSuccessListener { document ->
            val newId = document.size()
            val user = User(
                FirebaseAuth.getInstance().currentUser!!.email.toString(),
                newId,
                binding.name.text.toString(),
                binding.smallQuote.text.toString(),
                "",
                "",
                "",
                listOf(),
                "",
                "",
                binding.agePicker.value
            )
            collection.document(newId.toString()).set(user)
        }
    }
}