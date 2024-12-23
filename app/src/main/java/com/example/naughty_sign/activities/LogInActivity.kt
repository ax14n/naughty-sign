package com.example.naughty_sign.activities

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.example.naughty_sign.databinding.ActivityLoginBinding
import com.example.naughty_sign.language.BaseActivity
import com.example.naughty_sign.utils.MessageUtils
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class LogInActivity : BaseActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    /**
     * Se inicializan variables y se preparan recursos no relacionados con la vista.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        auth = Firebase.auth

        /*
        * When the button is pressed sends the user to the main screen
        * */
        binding.logInButton.setOnClickListener {

            if (binding.email.text.isNotBlank() && binding.password.text.isNotBlank()) {
                sigIn(binding.email.text.toString(), binding.password.text.toString())
            }
        }

        /*
        * When the button is pressed sends the user to the sign up screen
        * */
        binding.signupButton.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
        setContentView(binding.root)
    }

    private fun sigIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    MessageUtils.mostrarToast(this, "Error de inicio de sesión")
                }
            }
    }
}