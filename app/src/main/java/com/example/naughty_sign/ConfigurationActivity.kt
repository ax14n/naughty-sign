package com.example.naughty_sign

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.NumberPicker
import android.widget.SeekBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.naughty_sign.databinding.ActivitySettingsBinding


class ConfigurationActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    /**
     * Se inicializan variables y se preparan recursos no relacionados con la vista.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Enlaza el XML con la Activity
        setContentView(R.layout.activity_settings)

        // Se inicia el binding.
        binding = ActivitySettingsBinding.inflate(layoutInflater)

        // Mapa que relaciona cada botón con el texto que mostrará al usuario.
        val buttonAndText = mapOf(
            binding.changeUsernameButton to "Cambiar Nombre de Usuario",
            binding.changeDescriptionButton to "Cambiar Descripción",
            binding.changeQuoteButton to "Cambiar Cita",
            binding.changeRomaticPreferencesButton to "Cambiar Preferencias Románticas",
            binding.changeAgeRangeButton to "Cambiar Rango de Edad",
            binding.changeHobbiesButton to "Cambiar Hobbies",
            binding.changePhotosButton to "Cambiar Fotos",
            binding.changeMaxDistanceButton to "Cambiar Distancia Máxima",
            binding.changeThemeButton to "Cambiar Tema"
        )

        /*
        * FIXME: La navegación de pantallas funciona mal y rompe la agplicación.
        *  - Se ha de arreglar goToHobbiesSelector() y goToPhotosSelector()
        * */

        // Mapa que relaciona cada botón con la función a la que llamará al presionar el botón.
        val buttonAndFunction = mapOf(
            binding.changeUsernameButton to { showTextPopUp("nombre de usuario", 20) },
            binding.changeDescriptionButton to { showTextPopUp("descripción", 100) },
            binding.changeQuoteButton to { showTextPopUp("cita", 20) },
            binding.changeRomaticPreferencesButton to { showRomanticPreferencesPopUp() },
            binding.changeAgeRangeButton to { showNumberRangePopUp(18, 100) },


            binding.changeHobbiesButton to { goToHobbiesSelector() },       // ARREGLAR
            binding.changePhotosButton to { goToPhotosSelector() },         // ARREGLAR

            binding.changeMaxDistanceButton to { showMaxDistancePopUp(1, 1000) },
            binding.changeThemeButton to { /* TODO: Ya nos encargaremos más tarde de esto */ }
        )

        for (button in buttonAndFunction.keys) {
            button.text.text = buttonAndText[button]    // Les aplico el texto a cada botón.
            button.root.setOnClickListener {
                buttonAndFunction[button]?.invoke()     // Agrego la función al presionar el botón.
            }
        }

        // Establece un listener para el botón de cerrar sesión
        binding.logOutButton.setOnClickListener { goToLogIn() }
        setContentView(binding.root)
    }

    private fun goToLogIn() {
        // Crea un nuevo intent para iniciar la actividad de inicio de sesión
        val intent = Intent(this, LogInActivity::class.java)
        // Inicia la actividad de inicio de sesión
        startActivity(intent)
    }

    private fun goToHobbiesSelector() {
        // Crea un nuevo intent para iniciar la actividad de intereses
        val intent = Intent(this, InterestsActivity::class.java)
        // Inicia la actividad de los intereses
        startActivity(intent)
    }

    private fun goToPhotosSelector() {
        // Crea un nuevo intent para iniciar la actividad de inicio de sesión
        val intent = Intent(this, PhotoSelectorActivity::class.java)
        // Inicia la actividad de inicio de sesión
        startActivity(intent)
    }

    private fun showNumberRangePopUp(min: Int, max: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Selecciona el rango de edad")

        // Crear un layout para agregar los SeekBars
        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(50, 40, 50, 10)

        // Crear SeekBars para seleccionar la edad mínima y máxima
        val minAgeSeekBar = SeekBar(this).apply {
            this.max = max - min  // Establecemos el valor máximo del SeekBar
            progress = 0
        }

        val maxAgeSeekBar = SeekBar(this).apply {
            this.max = max - min  // Establecemos el valor máximo del SeekBar
            progress = max - min
        }

        // Agregar etiquetas para mostrar valores seleccionados
        val minAgeTextView = TextView(this)
        val maxAgeTextView = TextView(this)
        minAgeTextView.text = "Edad mínima: $min"
        maxAgeTextView.text = "Edad máxima: $max"

        // Listener para actualizar el valor de edad mínima y el TextView
        minAgeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                minAgeTextView.text = "Edad mínima: ${min + progress}"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Listener para actualizar el valor de edad máxima y el TextView
        maxAgeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                maxAgeTextView.text = "Edad máxima: ${min + progress}"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Agregar elementos al layout
        layout.addView(minAgeTextView)
        layout.addView(minAgeSeekBar)
        layout.addView(maxAgeTextView)
        layout.addView(maxAgeSeekBar)
        builder.setView(layout)

        builder.setPositiveButton("Aceptar") { dialog, _ ->
            val selectedMinAge = min + minAgeSeekBar.progress
            val selectedMaxAge = min + maxAgeSeekBar.progress
            if (selectedMinAge < selectedMaxAge) {
                showToast("Rango de edad seleccionado: $selectedMinAge - $selectedMaxAge")
            } else {
                showToast("El rango seleccionado no es válido.")
            }
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }


    private fun showTextPopUp(motivo: String, largo: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Insertar $motivo")

        // Crear un NumberPicker programáticamente
        val text = EditText(this)

        // Agregar el NumberPicker al diálogo
        builder.setView(text)

        builder.setPositiveButton("Aceptar") { dialog, _ ->
            val selectedAge: Int = text.text.length
            if (selectedAge <= largo) {
                showToast("Has introducido: $selectedAge")
                dialog.dismiss()
            }
        }
        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()

    }

    private fun showRomanticPreferencesPopUp() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Inserte su tipo de interés")

        // Crear un NumberPicker programáticamente
        val spinner = Spinner(this)
        val items = listOf("Hombres", "Mujeres", "Otros")
        // Crear un ArrayAdapter usando el layout simple para cada elemento
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
        // Especificar el diseño del dropdown
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Asignar el adaptador al Spinner
        spinner.adapter = adapter
        // Agregar el NumberPicker al diálogo
        builder.setView(spinner)

        builder.setPositiveButton("Aceptar") { dialog, _ ->
            showToast("Se ha seleccionado ${spinner.selectedItem.toString()}")
        }
        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()

    }

    private fun showMaxDistancePopUp(min: Int, max: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Selecciona la distancia máxima")

        // Crear un layout para agregar el SeekBar
        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(50, 40, 50, 10)

        // Crear un SeekBar para seleccionar la distancia
        val distanceSeekBar = SeekBar(this).apply {
            this.max = max  // Establecemos el valor máximo del SeekBar
            progress = min  // Valor inicial (distancia mínima)
        }

        // Crear un TextView para mostrar la distancia seleccionada
        val distanceTextView = TextView(this).apply {
            text = "Distancia máxima: ${min} km" // Texto inicial
        }

        // Listener para actualizar el valor de distancia y el TextView
        distanceSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                distanceTextView.text = "Distancia máxima: ${progress} km"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Agregar elementos al layout
        layout.addView(distanceTextView)
        layout.addView(distanceSeekBar)
        builder.setView(layout)

        builder.setPositiveButton("Aceptar") { dialog, _ ->
            val selectedDistance = distanceSeekBar.progress
            showToast("Distancia máxima seleccionada: $selectedDistance km")
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}