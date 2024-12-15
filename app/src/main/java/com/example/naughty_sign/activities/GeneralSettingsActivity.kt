package com.example.naughty_sign.activities

import android.app.AlertDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.naughty_sign.R
import com.example.naughty_sign.databinding.ActivityGeneralConfigurationsBinding
import com.google.android.material.slider.RangeSlider
import java.util.Locale


class GeneralSettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGeneralConfigurationsBinding
    private lateinit var sharedPreferences: SharedPreferences
    /*
    * Se establece conexión con el fichero donde se guardará la información de las pantallas.
    * */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inicializa el binding para enlazar la vista del layout
        binding = ActivityGeneralConfigurationsBinding.inflate(layoutInflater)
        createButtons()
        sharedPreferences =
            getSharedPreferences("general_config", MODE_PRIVATE)

        setContentView(binding.root)
    }

    private fun createButtons() {
        val botonesYTexto = mapOf(
            binding.changeMaxDistanceButton to getString(R.string.cambiar_distancia_m_xima),
            binding.changeTheme to getString(R.string.cambiar_tema),
            binding.changeLanguage to getString(R.string.cambiar_idioma)
        )

        val botonesYFunciones =
            mapOf(binding.changeMaxDistanceButton to {
                showMaxDistancePopUp(
                    sharedPreferences.getInt("min_distance", 10),
                    sharedPreferences.getInt("max_distance", 500)
                )
            },

                binding.changeTheme to { changeTheme() },
                binding.changeLanguage to { showLanguageOptions() })

        for (boton in botonesYTexto.keys) {
            boton.text.text = botonesYTexto[boton]
            boton.root.setOnClickListener { botonesYFunciones[boton]?.invoke() }
        }
    }

    /**
     * Muestra un diálogo para seleccionar la distancia máxima de búsqueda.
     *
     * @param min Distancia mínima en kilómetros.
     * @param max Distancia máxima en kilómetros.
     */
    private fun showMaxDistancePopUp(min: Int, max: Int) {

        /*
        * Se crea un editor para modificar los datos (se permite la escritura, que,
        * por defecto, es de lecutra)
        * */
        val myEdit = sharedPreferences.edit()
        /*
        * Crea un cuadro de dialogo que existe en el mismo contexto que esta ventana y le establece
        * el título de 'Selecciona el rango de distancia'.
        */
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Selecciona el rango de distancia")

        /*
        * Se crea un LinearLayout para que los elementos se agrupen de forma verticial. Establece
        * un margen interior de 50, 40, 50, 10. (LEFT, TOP, RIGHT, BOTTOM).
        * */
        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(50, 40, 50, 10)

        /*
        * Configuro un TextView con un valor inicial de 'Rango de edad' ajunto con el mínimo y el
        * máximo pasado por parámetro. El valor será actualizado más tarde según como el usuario
        * interacute con la barra.
        * */
        val kmRangeTextView = TextView(this).apply {
            text = "Rango de distancia: $min - $max"
        }

        /*
        * Se crea un RangeSlider donde se representará el rango de edad que se quiere alcanzar. En
        * su interior contiene los siguientes parámetros:
        * -----------------------------------------------------------------------------------------
        * valueFrom -> Representación del valor mínimo que usará la barra.
        * valueTo   -> Representación del valor máximo que usará la barra.
        * setpSize  -> Cuánto incrementará la barra por nivel alcanzado.
        * values    -> Una lista donde se indica el valor inicial y final que aparecerá por omisión.
        * */
        var selectedMinKm = 10
        var selectedMaxKm = 100

        val kmRangeSlider = RangeSlider(this).apply {
            valueFrom = 18f             // Valor mínimo del RangeSlider.
            valueTo = 100f              // Valor máximo del RangeSlider.
            stepSize = 1f               // Incremento de 1 año para cada paso.
            values = listOf(18f, 100f)  // Valores iniciales: 18 como mínimo y 100 como máximo.

            /*
            * Establezco un Listener que se encargará de que cuando se detecte un cambio actualice
            * el texto informativo para el usaurio de forma que refleje el rango por pantalla.
            * */
            addOnChangeListener { slider, _, _ ->
                selectedMinKm = slider.values[0].toInt()
                selectedMaxKm = slider.values[1].toInt()
                kmRangeTextView.text = "Rango de km: $selectedMinKm - $selectedMaxKm"
            }
        }

        /*
        * Añado al LayoutManager el TextView y el RangeSlider que declaramos y manipulamos antes.
        * */
        layout.addView(kmRangeTextView)
        layout.addView(kmRangeSlider)

        /*
        * Añado el LayoutMangaer a dentro del cuadro de dialogo que hicimos anteriormente.
        * */
        builder.setView(layout)


        /*
        * Configuro el botón de aceptar del cuadro de dialogo usando una expresión lambda. Cuando
        * se presiona el botón, surge un Toast que informa el valor mínimo seleccionado y el máximo.
        * Al finalizar el Toast se cierra.
        * */
        builder.setPositiveButton("Aceptar") { dialog, _ ->
            myEdit.putInt("max_distance", selectedMaxKm)
            myEdit.putInt("min_distance", selectedMinKm)
            dialog.dismiss()
            myEdit.apply()
        }

        /*
        * Una vez configurado el cuadro de dialogo, se forma finalmente y se muestra por pantalla
        * */
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun changeTheme() {}

    private fun changeLanguage(language: String) {
        /*
        * Se crea un editor para modificar los datos (se permite la escritura, que,
        * por defecto, es de lecutra)
        * */
        val myEdit = sharedPreferences.edit()
        myEdit.putString("language", language)

        val locale = Locale(language)
        Locale.setDefault(locale)

        val config = resources.configuration
        config.setLocale(locale)

        // Actualizar la configuración del contexto
        resources.updateConfiguration(config, resources.displayMetrics)

        // Reiniciar la actividad para aplicar el idioma
        recreate()

        myEdit.apply()

    }

    private fun showLanguageOptions() {
        val dialog: AlertDialog

        // Crear un array con los idiomas disponibles
        val languages = arrayOf(
            getString(R.string.language_english) to "en",
            getString(R.string.language_español) to "es",
            getString(R.string.language_italian) to "it"
        )

        // Crear un Spinner
        val spinner = Spinner(this)
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            languages.map { it.first } // Solo mostrar los nombres de los idiomas
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        // Configurar el AlertDialog con el Spinner
        dialog = AlertDialog.Builder(this)
            .setTitle(getString(R.string.select_language))
            .setView(spinner)
            .setPositiveButton(getString(R.string.ok)) { _, _ ->
                // Obtener la posición seleccionada
                val selectedIndex = spinner.selectedItemPosition
                val selectedLanguageCode = languages[selectedIndex].second
                // Cambiar el idioma
                changeLanguage(selectedLanguageCode)
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .setCancelable(true)
            .show()
    }


}