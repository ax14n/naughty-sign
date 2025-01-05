package com.example.naughty_sign.activities

import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.naughty_sign.R
import com.example.naughty_sign.databinding.ActivitySettingsBinding
import com.example.naughty_sign.utils.LoggedUserUtils
import com.example.naughty_sign.utils.MessageUtils
import com.google.android.material.slider.RangeSlider

/**
 * Esta clase representa la actividad de configuración de la aplicación, permitiendo al usuario
 * modificar su información personal, preferencias, y otras configuraciones de su perfil.
 */
class ProfileConfigurationActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private var currentPhotoUri: Uri? = null

    private val camara =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { resultado ->
            if (resultado) {
                currentPhotoUri?.let {
                    binding.imageView.setImageURI(it)
                    binding.imageView.invalidate() // Forzar actualización del ImageView
                    MessageUtils.mostrarToast(this, getString(R.string.se_ha_almacenado_la_foto))
                } ?: run {
                    Log.e("ProfileConfig", "La URI de la foto es nula, no se puede actualizar la imagen.")
                    MessageUtils.mostrarToast(this, getString(R.string.error_al_guardar_foto))
                }
            } else {
                Log.e("ProfileConfig", "Error: La cámara devolvió un resultado negativo.")
                MessageUtils.mostrarToast(this, getString(R.string.no_se_ha_almacenado_la_foto))
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
            binding.changeUsernameButton to getString(R.string.cambiar_nombre_de_usuario),
            binding.changeDescriptionButton to getString(R.string.cambiar_descripci_n),
            binding.changeQuoteButton to getString(R.string.cambiar_cita),
            binding.changeCityButton to getString(R.string.cambiar_ciudad),
            binding.changeProfessionButton to getString(R.string.cambiar_profesi_n),
            binding.changeRomaticPreferencesButton to getString(R.string.cambiar_preferencias_rom_nticas),
            binding.changeAgeRangeButton to getString(R.string.cambiar_rango_de_edad),
            binding.changeHobbiesButton to getString(R.string.cambiar_hobbies),
            binding.changePhotosButton to getString(R.string.cambiar_fotos)
        )

        /*
         * Para asignar funcionalidad a cada botón de manera organizada, se agrupan en un mapa
         * junto con sus respectivas funciones. Esto facilita añadir o cambiar la funcionalidad
         * de cada botón, manteniendo un código más legible y estructurado.
        */
        val buttonAndFunction = mapOf(binding.changeUsernameButton to { cambiarNombreUsuario() },
            binding.changeDescriptionButton to { cambiarDescripcion() },
            binding.changeQuoteButton to { cambiarCita() },
            binding.changeCityButton to { cambiarCiudad() },
            binding.changeProfessionButton to { cambiarProfesion() },
            binding.changeRomaticPreferencesButton to { showRomanticPreferencesPopUp() },
            binding.changeAgeRangeButton to { showNumberRangePopUp() },
            binding.changeHobbiesButton to { goToHobbiesSelector() },
            binding.changePhotosButton to { goToPhotosSelector() })

        /*
        * A cada botón del diccionario se le asigna el texto y la función correspondientes.
        * El texto se configura directamente desde el mapa `buttonAndText` para mayor claridad.
        * Luego, mediante una expresión lambda en `setOnClickListener`, cada botón ejecutará
        * la función asignada en `buttonAndFunction` al ser presionado.
         */
        for (button in buttonAndFunction.keys) {
            button.text.text = buttonAndText[button]
            button.root.setOnClickListener {
                buttonAndFunction[button]?.invoke()
            }
        }

        binding.changeAvatarButton.setOnClickListener {
            val photoUri = obtenerUriImagen()
            if (photoUri != null) {
                currentPhotoUri = photoUri
                camara.launch(photoUri)
            } else {
                MessageUtils.mostrarToast(
                    this,
                    getString(R.string.error_al_generar_uri)
                )
            }
        }

        /*
        * Le asigno al botón 'logOutButton' mediante una expresión lambda en 'setOnClickListener',
        * que cambie la ventana actual por la ventana de inicio de sesión mediante la llamada a
        * la función 'goToLogIn'.
        * */
        binding.logOutButton.setOnClickListener {
            LoggedUserUtils.cerrarSesionUsuario()
            finish()
            goToLogIn()
        }

        /*
        * Le asigno al botón 'logOutButton' mediante una expresión lambda en 'setOnClickListener',
        * que cambie la ventana actual por la ventana de inicio de sesión mediante la llamada a
        * la función 'goToLogIn'.
        * */
        binding.goBackButton.setOnClickListener { regresarActividadPrincipal() }

        /*
        * Asigno la vista actual de la aplicación a la pantalla de configuración una vez todas
        * las variables y asignaciones hayan concluido de forma satisfactoria.
        * */
        setContentView(binding.root)
    }

    /**
     * Cierra configuraciones y vuelve a la actividad princial.
     */
    private fun regresarActividadPrincipal() {
        val intent = Intent(
            this, MainActivity::class.java
        )
        startActivity(intent)
        finish()
    }

    /**
     * Cambia la ciudad de residencia del usuario.
     */
    private fun cambiarCiudad() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.seleccione_la_ciudad_en_donde_habita))

        val spinner = Spinner(this)
        val ciudades = arrayOf(
            "Madrid", "Barcelona", "Valencia", "Sevilla",
            "Zaragoza", "Málaga", "Murcia", "Palma",
            "Las Palmas de Gran Canaria", "Bilbao",
            "Alicante", "Córdoba", "Valladolid",
            "Vigo", "Gijón", "L'Hospitalet de Llobregat",
            "A Coruña", "Granada", "Elche", "Oviedo"
        )
        spinner.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, ciudades)
        builder.setView(spinner)

        builder.setPositiveButton(getString(R.string.aplicar)) { dialog, _ ->
            LoggedUserUtils.actualizar("ciudad", spinner.selectedItem.toString())
            dialog.dismiss()
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    /**
     * Cambia la profesión del usuario.
     */
    private fun cambiarProfesion() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.seleccione_su_profesi_n))

        val spinner = Spinner(this)
        val profesiones = arrayOf(
            "Abogado", "Arquitecto", "Ingeniero", "Médico",
            "Enfermero", "Profesor", "Cocinero", "Escritor",
            "Artista", "Diseñador gráfico", "Desarrollador de software",
            "Contador", "Psicólogo", "Científico", "Periodista",
            "Fotógrafo", "Electricista", "Carpintero"
        )
        spinner.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, profesiones)
        builder.setView(spinner)

        builder.setPositiveButton(getString(R.string.aplicar)) { dialog, _ ->
            LoggedUserUtils.actualizar("profesion", spinner.selectedItem.toString())
            dialog.dismiss()
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    /**
     * Solicita un nuevo nombre de usuario y actualiza el campo en la base de datos.
     */
    private fun cambiarNombreUsuario() {

        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.introduzca_un_nuevo_nombre_de_usuario))

        val editText = EditText(this)
        builder.setView(editText)

        builder.setPositiveButton(getString(R.string.aplicar)) { dialog, _ ->
            LoggedUserUtils.actualizar("nombre", editText.text.toString())
            dialog.dismiss()
        }


        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    /**
     * Solicita un nuevo nombre de usuario y actualiza el campo en la base de datos.
     */
    private fun cambiarDescripcion() {

        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.introduzca_una_nueva_descripci_n))

        val editText = EditText(this)
        builder.setView(editText)

        builder.setPositiveButton(getString(R.string.aplicar)) { dialog, _ ->
            LoggedUserUtils.actualizar("descripcion", editText.text.toString())
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }


    /**
     * Solicita un nuevo nombre de usuario y actualiza el campo en la base de datos.
     */
    private fun cambiarCita() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.introduzca_una_nueva_cita))

        val editText = EditText(this)
        builder.setView(editText)

        builder.setPositiveButton(getString(R.string.aplicar)) { dialog, _ ->
            LoggedUserUtils.actualizar("cita", editText.text.toString())
            dialog.dismiss()
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
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
                MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Camera"
            )  // Directorio en la galería
        }

        // Insertar en MediaStore y obtener la URI
        return contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            values
        )!!
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
    private fun showNumberRangePopUp() {

        //------------- { Valor máximo y mínimo del rango de edad } -------------//
        val min = 18;
        val max = 100

        /*
        * Crea un cuadro de dialogo que existe en el mismo contexto que esta ventana y le establece
        * el título de 'Seleccionaa el rango de edad'.
        */
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.selecciona_el_rango_de_edad))

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
            text = buildString {
                append(context.getString(R.string.rango_de_edad))
                append(" ")
                append(min)
                append(" - ")
                append(max)
            }
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
            values =
                listOf(18f, 100f)  // Valores iniciales: 18 como mínimo y 100 como máximo.

            /*
            * Establezco un Listener que se encargará de que cuando se detecte un cambio actualice
            * el texto informativo para el usaurio de forma que refleje el rango por pantalla.
            * */
            addOnChangeListener { slider, _, _ ->
                val selectedMinAge = slider.values[0].toInt()
                val selectedMaxAge = slider.values[1].toInt()
                ageRangeTextView.text = buildString {
                    append(context.getString(R.string.rango_de_edad))
                    append(" ")
                    append(selectedMinAge)
                    append("-")
                    append(selectedMaxAge)
                }
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
        builder.setPositiveButton(getString(R.string.ok)) { dialog, _ ->
            val selectedMinAge = ageRangeSlider.values[0].toInt()
            val selectedMaxAge = ageRangeSlider.values[1].toInt()
            if (selectedMinAge < selectedMaxAge) {
                MessageUtils.mostrarToast(this, buildString {
                    append(getString(R.string.rango_de_edad_seleccionado))
                    append(" ")
                    append(selectedMinAge)
                    append(" - ")
                    append(selectedMaxAge)
                })
            } else {
                MessageUtils.mostrarToast(
                    this,
                    getString(R.string.el_rango_seleccionado_no_es_v_lido)
                )
            }
            dialog.dismiss()
        }

        /*
        * Configuro el botón de cancler del cuadro de digalogo usando una expresión lambda. Se pasa
        * dialog como argumento y se ignora el faltante mediante '_'. Cierra el cuadro de dialogo.
        * */
        builder.setNegativeButton(getString(R.string.cancel)) { dialog, _ -> dialog.dismiss() }

        /*
        * Una vez configurado el cuadro de dialogo, se forma finalmente y se muestra por pantalla
        * */
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    /**
     * Muestra un diálogo para seleccionar preferencias románticas mediante un Spinner.
     */
    private fun showRomanticPreferencesPopUp() {

        val items = listOf(
            getString(R.string.hombres),
            getString(R.string.mujeres),
            getString(R.string.otros)
        )

        /*
        * Crea un cuadro de dialogo que existe en el mismo contexto que esta ventana y le establece
        * el título de 'Insertar su tipo de interés'.
        **/
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.inserte_su_tipo_de_inter_s))


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
        builder.setPositiveButton(getString(R.string.ok)) { dialog, _ ->
            MessageUtils.mostrarToast(this, "Se ha seleccionado ${spinner.selectedItem}")
        }

        /*
        * Se configura el botón de 'Cancelar' de forma que al presionarlo se cierre el cuadro de
        * dialogo.
        * */
        builder.setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
            dialog.dismiss()
        }

        /*
        * Una vez configurado el cuadro de dialogo, se forma finalmente y se muestra por pantalla
        * */
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

}