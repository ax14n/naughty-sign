package com.example.naughty_sign

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.naughty_sign.databinding.ActivityLoginBinding

class LogInActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    /**
     * Se inicializan variables y se preparan recursos no relacionados con la vista.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)

        /*
        * When the button is pressed sends the user to the main screen
        * */
        binding.logInButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
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
}