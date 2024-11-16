package com.example.naughty_sign.activities

import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.naughty_sign.R
import com.example.naughty_sign.databinding.ActivitySettingsBinding
import com.google.android.material.slider.RangeSlider

/**
 * Esta clase representa la actividad de configuración de la aplicación, permitiendo al usuario
 * modificar su información personal, preferencias, y otras configuraciones de su perfil.
 */
class ConfigurationActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private val camara =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { resultado ->
            if (resultado) {
                Toast.makeText(this, "Si se ha tomado la foto", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "No se ha almacenado la foto", Toast.LENGTH_SHORT).show()
            }
        }


    /**
     * Inicializa la actividad, configura los botones y sus funciones, y establece el listener para
     * cerrar sesión y demás botones de la interfáz.
     *
     * @param savedInstanceState Estado guardado de la actividad, si existe.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*
        * Se establece una relación entre la actividad y el XML correspondiente, mostrnado por
        * pantalla finalmentel a interfáz de configuración.
        * */
        setContentView(R.layout.activity_settings)

        /*
        * Una vez establecida la relación entre el Activity y el Layout, se infla los contenidos
        * del XML para convertirlos en código y poder interactuar con los elementos de la interáz.
        * */
        binding = ActivitySettingsBinding.inflate(layoutInflater)

        /*
        * Dado que los botones de configuración están compuestos por elementos agrupados,
        * no se puede asignar texto directamente en el archivo XML. Por ello, se crea
        * un mapa en el código donde cada botón se asocia a su texto. Esta estructura permite
        * configurar los textos de forma programática, facilitando su mantenimiento y claridad.
        */
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
         * Para asignar funcionalidad a cada botón de manera organizada, se agrupan en un mapa
         * junto con sus respectivas funciones. Esto facilita añadir o cambiar la funcionalidad
         * de cada botón, manteniendo un código más legible y estructurado.
        */
        val buttonAndFunction =
            mapOf(binding.changeUsernameButton to { showTextPopUp("nombre de usuario", 20) },
                binding.changeDescriptionButton to { showTextPopUp("descripción", 100) },
                binding.changeQuoteButton to { showTextPopUp("cita", 20) },
                binding.changeRomaticPreferencesButton to {
                    showRomanticPreferencesPopUp(
                        listOf(
                            "Hombres", "Mujeres", "Otros"
                        )
                    )
                },
                binding.changeAgeRangeButton to { showNumberRangePopUp(18, 100) },
                binding.changeHobbiesButton to { goToHobbiesSelector() },
                binding.changePhotosButton to { goToPhotosSelector() },
                binding.changeMaxDistanceButton to { showMaxDistancePopUp(1, 1000) },
                binding.changeThemeButton to { /* TODO: Ya nos encargaremos más tarde de esto */ })

        /*
        * A cada botón del diccionario se le asigna el texto y la función correspondientes.
        * El texto se configura directamente desde el mapa `buttonAndText` para mayor claridad.
        * Luego, mediante una expresión lambda en `setOnClickListener`, cada botón ejecutará
        * la función asignada en `buttonAndFunction` al ser presionado.
        * */
        for (button in buttonAndFunction.keys) {
            button.text.text = buttonAndText[button]
            button.root.setOnClickListener {
                buttonAndFunction[button]?.invoke()
            }
        }

        binding.changeAvatarButton.setOnClickListener {
            val uri = obtenerUriImagen() // Obtén la URI de destino donde se almacenará la foto
            camara.launch(uri)
        }

        /*
        * Le asigno al botón 'logOutButton' mediante una expresión lambda en 'setOnClickListener',
        * que cambie la ventana actual por la ventana de inicio de sesión mediante la llamada a
        * la función 'goToLogIn'.
        * */
        binding.logOutButton.setOnClickListener { goToLogIn() }

        /*
        * Asigno la vista actual de la aplicación a la pantalla de configuración una vez todas
        * las variables y asignaciones hayan concluido de forma satisfactoria.
        * */
        setContentView(binding.root)
    }

    // Obtener la URI para guardar la foto en la galería
    private fun obtenerUriImagen(): Uri {
        val contentResolver = contentResolver

        // Crear un conjunto de valores para la imagen
        val values = ContentValues().apply {
            put(
                MediaStore.Images.Media.DISPLAY_NAME,
                "foto_${System.currentTimeMillis()}.jpg"
            )  // Nombre del archivo
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")  // Tipo de la imagen
            put(
                MediaStore.Images.Media.RELATIVE_PATH,
                "Pictures/Camera"
            )  // Directorio en la galería
        }

        // Insertar en MediaStore y obtener la URI
        return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)!!
    }

    /**
     * Navega a la actividad de inicio de sesión.
     * Crea un Intent que establece la transición desde la pantalla actual hacia la pantalla de
     * inicio de sesión (`LogInActivity`). Al invocar `startActivity(intent)`, se lanza la actividad
     * de inicio de sesión y se reemplaza la pantalla actual por esta, permitiendo al usuario
     * regresar a la interfaz de acceso.
     */
    private fun goToLogIn() {
        val intent = Intent(this, LogInActivity::class.java)
        startActivity(intent)
    }

    /**
     * Navega a la actividad de selector de hobbies.
     * Crea un Intent que establece la transición desde la pantalla actual hacia la pantalla de
     * selección de intereses (`InterestsActivity`). Al invocar `startActivity(intent)`, se lanza la actividad
     * de selección de hobbies, reemplazando la pantalla actual.
     */
    private fun goToHobbiesSelector() {
        val intent = Intent(this, InterestsActivity::class.java)
        startActivity(intent)
    }

    /**
     * Navega a la actividad de adición/eliminación de imágenes del perfil.
     * Crea un Intent que establece la transición desde la pantalla actual hacia la pantalla de
     * selección de fotos (`PhotoSelectorActivity`). Al invocar `startActivity(intent)`, se lanza la actividad
     * de selección de imágenes, reemplazando la pantalla actual.
     */
    private fun goToPhotosSelector() {
        val intent = Intent(this, PhotoSelectorActivity::class.java)
        startActivity(intent)
    }

    /**
     * Muestra un diálogo para seleccionar un rango de edad usando SeekBars.
     *
     * @param min Edad mínima posible.
     * @param max Edad máxima posible.
     */
    private fun showNumberRangePopUp(min: Int, max: Int) {

        /*
        * Crea un cuadro de dialogo que existe en el mismo contexto que esta ventana y le establece
        * el título de 'Seleccionaa el rango de edad'.
        */
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Selecciona el rango de edad")

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
        val ageRangeTextView = TextView(this).apply {
            text = "Rango de edad: $min - $max"
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
        val ageRangeSlider = RangeSlider(this).apply {
            valueFrom = 18f             // Valor mínimo del RangeSlider.
            valueTo = 100f              // Valor máximo del RangeSlider.
            stepSize = 1f               // Incremento de 1 año para cada paso.
            values = listOf(18f, 100f)  // Valores iniciales: 18 como mínimo y 100 como máximo.

            /*
            * Establezco un Listener que se encargará de que cuando se detecte un cambio actualice
            * el texto informativo para el usaurio de forma que refleje el rango por pantalla.
            * */
            addOnChangeListener { slider, _, _ ->
                val selectedMinAge = slider.values[0].toInt()
                val selectedMaxAge = slider.values[1].toInt()
                ageRangeTextView.text = "Rango de edad: $selectedMinAge - $selectedMaxAge"
            }
        }

        /*
        * Añado al LayoutManager el TextView y el RangeSlider que declaramos y manipulamos antes.
        * */
        layout.addView(ageRangeTextView)
        layout.addView(ageRangeSlider)

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
            val selectedMinAge = ageRangeSlider.values[0].toInt()
            val selectedMaxAge = ageRangeSlider.values[1].toInt()
            if (selectedMinAge < selectedMaxAge) {
                showToast("Rango de edad seleccionado: $selectedMinAge - $selectedMaxAge")
            } else {
                showToast("El rango seleccionado no es válido.")
            }
            dialog.dismiss()
        }

        /*
        * Configuro el botón de cancler del cuadro de digalogo usando una expresión lambda. Se pasa
        * dialog como argumento y se ignora el faltante mediante '_'. Cierra el cuadro de dialogo.
        * */
        builder.setNegativeButton("Cancelar") { dialog, _ -> dialog.dismiss() }

        /*
        * Una vez configurado el cuadro de dialogo, se forma finalmente y se muestra por pantalla
        * */
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    /**
     * Muestra un diálogo de texto para insertar información de usuario.
     *
     * @param motivo Describe el motivo de la entrada de texto.
     * @param largo Longitud máxima permitida para el texto ingresado.
     */
    private fun showTextPopUp(motivo: String, largo: Int) {

        /*
        * Crea un cuadro de dialogo que existe en el mismo contexto que esta ventana y le establece
        * el título de 'Insertar $motivo'.
        **/
        val builder = AlertDialog.Builder(this).apply {
            setTitle("Insertar $motivo")
        }

        /*
        * Se crea un campo de texto donde el usaurio introducirá su respuesta y se agrega al
        * cuadro de diálogo de maneara que lo envuelva.
        * */
        val text = EditText(this)
        builder.setView(text)

        /*
        * Al presionar el botón de acpetar se mostrará un Toast con un mensaje indicando lo que ha
        * introducido a mano el usuario. Tras mostrar el mensaje, se cierra el cuadro de dialogo.
        * */
        builder.setPositiveButton("Aceptar") { dialog, _ ->
            val introducedText: Int = text.text.length
            if (introducedText <= largo) {
                showToast("Has introducido: $introducedText")
                dialog.dismiss()
            }
        }

        /*
        * El botón de cancelar configurado de forma que al presionarlo desaparezca el cuadro de
        * dialogo.
        * */
        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
        }

        /*
        * Una vez configurado el cuadro de dialogo, se forma finalmente y se muestra por pantalla
        * */
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }


    /**
     * Muestra un diálogo para seleccionar preferencias románticas mediante un Spinner.
     *
     * @param items Lista de elementos que contendrá el Spinner.
     */
    private fun showRomanticPreferencesPopUp(items: List<String>) {

        /*
        * Crea un cuadro de dialogo que existe en el mismo contexto que esta ventana y le establece
        * el título de 'Insertar su tipo de interés'.
        **/
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Inserte su tipo de interés")


        /*
        * Se crea un spinner configurado en el contexto actual de la actividad y se implementa un
        * adaptador que contendrá la lista de elementos pasado por parámetro. Los elementos están
        * configurados como elementos simples. Se puede acceder a la estructura pinchando en el
        * segundo parámetro del constructor.
        * */
        val spinner = Spinner(this)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)

        /*
        * Se configura el DropDown del adaptador. El DropDown por definición es el Pop-Up que suerge
        * y muestra los elementos del adaptador cuando se presiona sobre el spinner. Bastante
        * interesante.
        * */
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        /*
        * Terminado de configurar el adaptador, lo introduzco el el spinner.
        * */
        spinner.adapter = adapter

        /*
        * Agrego el spinner al cuadro de dialogo de forma que este lo envuelva.
        * */
        builder.setView(spinner)

        /*
        * Se configura el botón de 'Aceptar' de forma que al presionarlo se imprima un mensaje
        * por pantalla mostrando el valor seleccionado del Spinner.
        * */
        builder.setPositiveButton("Aceptar") { dialog, _ ->
            showToast("Se ha seleccionado ${spinner.selectedItem}")
        }

        /*
        * Se configura el botón de 'Cancelar' de forma que al presionarlo se cierre el cuadro de
        * dialogo.
        * */
        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
        }

        /*
        * Una vez configurado el cuadro de dialogo, se forma finalmente y se muestra por pantalla
        * */
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    /**
     * Muestra un diálogo para seleccionar la distancia máxima de búsqueda.
     *
     * @param min Distancia mínima en kilómetros.
     * @param max Distancia máxima en kilómetros.
     */
    private fun showMaxDistancePopUp(min: Int, max: Int) {

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
                val selectedMinKm = slider.values[0].toInt()
                val selectedMaxKm = slider.values[1].toInt()
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
        * Una vez configurado el cuadro de dialogo, se forma finalmente y se muestra por pantalla
        * */
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    /**
     * Muestra un mensaje emergente en pantalla.
     *
     * @param message Texto del mensaje que se mostrará.
     */
    private fun showToast(message: String) {
        // Forma un cuadro de texto con el mensaje proporcionado por parámetro.
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}