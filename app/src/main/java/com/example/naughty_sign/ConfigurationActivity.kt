package com.example.naughty_sign

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.Spinner
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

            binding.changeMaxDistanceButton to { showNumberRangePopUp(1, 1000) },
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
        builder.setTitle("Selecciona tu Edad")

        // Crear un NumberPicker programáticamente
        val numberPicker = NumberPicker(this)
        numberPicker.minValue = min  // Valor mínimo
        numberPicker.maxValue = max  // Valor máximo
        numberPicker.wrapSelectorWheel = false    // Hace que el selector sea circular

        // Agregar el NumberPicker al diálogo
        builder.setView(numberPicker)

        builder.setPositiveButton("Aceptar") { dialog, _ ->
            val selectedAge = numberPicker.value
            showToast("Has seleccionado la edad: $selectedAge")
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

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}