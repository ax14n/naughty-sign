package com.example.naughty_sign.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.naughty_sign.databinding.ActivitySignupBinding
import com.example.naughty_sign.utils.MessageUtils
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class SignUpActivity : AppCompatActivity() {

    /**
     * Objeto encargado de guardar las referencias a los elementos de la interfáz visual.
     */
    private lateinit var binding: ActivitySignupBinding

    /**
     * Objeto enlace a Firebase que nos permite interactuar con diferentes estancias de la sesión.
     */
    private lateinit var auth: FirebaseAuth

    /**
     * Se inicializan variables y se preparan recursos no relacionados con la vista.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //------------- { Inicialización de variables } -------------//
        binding = ActivitySignupBinding.inflate(layoutInflater)
        auth = Firebase.auth

        //------------- { Funcionabilidad de botón de inicio de sesión. } -------------//
        binding.logInButton.setOnClickListener {

            // Redirige al usuario a la ventana de Inicio de Sesión.
            val intent = Intent(this, LogInActivity::class.java)
            startActivity(intent)
        }

        //------------- { Funcionabilidad de botón de registro. } -------------//
        binding.registerButton.setOnClickListener {

            /**
             * Tras revisar una serie de condiciones, crea un nuevo usuario. Las condiciones que
             * han de cumplirse son:
             * ---------------------------------------------------------------------------------
             * -> Campo email no se ha dejado vacío.
             * -> Campo contraseña no se ha dejado vacía.
             * -> Se han aceptado los acuerdos y condiciones.
             *
             */
            if (binding.email.text.isNotBlank() || binding.password.text.isNotBlank()) {
                createAccount(binding.email.text.toString(), binding.password.text.toString())
            }

        }

        // Se aplica la visual a la actividad actual.
        setContentView(binding.root)
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            reload()
        }
    }

    /**
     * Crea un usuario en la platoforma con `usuario` y `contraseña`. Dependiendo de si la
     * operación ha salido bien, realiza una serie de operaciones que cambiarán de ventana, o
     * en caso contrario, informará al usuario de que ha habido un error.
     */
    private fun createAccount(email: String, password: String) {

        // Se crea el usuario con usuario y contraseña, y se pasa el resultado por lambda.
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->

            /**
             * Si la operación ha salido bien, crea un mensaje en logcat informando que se ha creado
             * la cuenta de manera satisfactoria, y preparará un Intent para moverse a la siguiente
             * ventana que completará los datos del usuario.
             */
            if (task.isSuccessful) {

                // Imprime un log por consola del desarrollador.
                Log.d(TAG, "createUserWithEmail:success")

                // Prepara la actividad y cambia a la nueva ventana.
                val intent = Intent(
                    this, PostRegisterActivity::class.java
                )
                startActivity(intent)

            } else {
                Log.w(TAG, "createUserWithEmail:failure", task.exception)
                MessageUtils.mostrarToast(this, "Authentication failed")
            }
        }
    }

    private fun reload() {
    }

    companion object {
        private const val TAG = "EmailPassword"
    }
}