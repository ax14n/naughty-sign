package com.example.naughty_sign.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.naughty_sign.R
import com.example.naughty_sign.activities.GeneralSettingsActivity
import com.example.naughty_sign.activities.ProfileConfigurationActivity
import com.example.naughty_sign.databinding.FragmentProfileBinding
import com.example.naughty_sign.fragments.FragmentProfile.Companion.newInstance
import com.example.naughty_sign.utils.LoggedUserUtils
import com.google.android.material.chip.Chip

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * Un [Fragment] que representa el perfil del usuario.
 * Contiene un botón de configuración que despliega un menú para navegar a configuraciones y mostrar opciones adicionales.
 * Utiliza el método [newInstance] para crear una instancia de este fragmento.
 */
class FragmentProfile : Fragment() {

    // Parámetros de inicialización para el fragmento.
    private var param1: String? = null
    private var param2: String? = null

    // Enlace con la vista del fragmento para facilitar el acceso a los elementos UI.
    private lateinit var binding: FragmentProfileBinding

    /**
     * Método llamado para crear la instancia del fragmento.
     * Carga los argumentos de configuración y establece el enlace de vista (binding).
     *
     * @param savedInstanceState Estado guardado del fragmento.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Carga los parámetros recibidos como argumentos
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        // Inicializa el binding para enlazar la vista del layout
        binding = FragmentProfileBinding.inflate(layoutInflater)
    }

    /**
     * Método para inflar el layout de este fragmento.
     *
     * @param inflater El objeto LayoutInflater que puede usarse para inflar cualquier vista en el fragmento.
     * @param container Si no es nulo, es la vista primaria que contiene el fragmento.
     * @param savedInstanceState Estado guardado de la vista, si existe.
     * @return La vista inflada que representa el layout del fragmento.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Retorna la vista raíz del binding (la vista principal del layout del fragmento)
        return binding.root
    }

    /**
     * Método que establece los elementos de la interfáz una vez formada la misma.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configura el botón de configuración para mostrar el menú desplegable al hacer clic
        binding.configButton.setOnClickListener {
            mostrarMenuDesplegable(it)
        }
        // Carga los datos del usuario especificado
        cargarPerfil()
    }

    /**
     * Carga el pefil del usuario tras leer el JSON del servidor.
     */
    private fun cargarPerfil() {
        LoggedUserUtils.extraerDatosPerfil { bundle ->

            binding.profileName.text = bundle.getString("nombre")
            binding.profileQuote.text = bundle.getString("cita").toString()
            binding.profileProfession.text = bundle.getString("profesion").toString()
            binding.profileCity.text = bundle.getString("ciudad").toString()
            binding.profileDescription.text = bundle.getString("descripcion").toString()
            val intereses = bundle.getStringArrayList("intereses") as List<String?>
            for (interes in intereses) binding.chipGroup.addView(Chip(requireContext()).apply {
                text = interes.toString()
            })
            // bundle.getString("foto_perfil").toString()
            binding.profileCity.text = bundle.getString("ciudad").toString()
            // bundle.getString(document.get("edad").toString())
        }
    }

    /**
     * Muestra un menú desplegable (PopupMenu) al usuario.
     * Este menú contiene opciones de configuración y sobre nosotros.
     *
     * @param view La vista que dispara el PopupMenu (en este caso, configButton).
     */
    private fun mostrarMenuDesplegable(view: View) {

        // Crea un PopupMenu asociado con la vista especificada
        val popupMenu = PopupMenu(context, view)

        // Infla el menú de opciones a partir del archivo XML definido en res/menu/settings_menu.xml
        val inflater: MenuInflater = popupMenu.menuInflater
        inflater.inflate(R.menu.settings_menu, popupMenu.menu)

        // Establece un listener para manejar los clics en las opciones del menú desplegable
        popupMenu.setOnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {

                // Opción de ir a Configuración
                R.id.go_profile_settings_button -> {
                    // Crea un intent para abrir la actividad ConfigurationActivity
                    val intent = Intent(context, ProfileConfigurationActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.go_settings_button -> {
                    // Crea un intent para abrir la actividad GeneralSettingsActivity
                    val intent = Intent(context, GeneralSettingsActivity::class.java)
                    startActivity(intent)
                    true
                }

                // Opción "Acerca de Nosotros"
                R.id.about_us_button -> {
                    // Muestra un mensaje Toast como respuesta

                    val builder = AlertDialog.Builder(context).apply {
                        this.setTitle("Acerca de")
                    }

                    val layout = LinearLayout(context)
                    layout.orientation = LinearLayout.VERTICAL
                    layout.setPadding(50, 40, 50, 40)

                    val textView = TextView(context).apply {
                        text =
                            "Proyecto realizado por Cristian Pop y Zelmar Hernán para la asignatura de desarrollo de aplicaciones móviles."
                    }

                    layout.addView(textView)

                    builder.setView(layout)
                    val dialog = builder.create()
                    dialog.show()

                    true
                }

                // Caso por defecto si no se reconoce la opción seleccionada
                else -> false
            }
        }

        // Muestra el PopupMenu
        popupMenu.show()
    }

    /**
     * Método de fábrica para crear una nueva instancia de FragmentProfile con parámetros.
     *
     * @param param1 Parámetro de inicialización 1.
     * @param param2 Parámetro de inicialización 2.
     * @return Una nueva instancia de FragmentProfile con los argumentos configurados.
     */
    companion object {

        /**
         * Crea una nueva instancia de FragmentProfile, configurando los argumentos iniciales necesarios.
         *
         * @param param1 Primer parámetro de configuración.
         * @param param2 Segundo parámetro de configuración.
         * @return Instancia de FragmentProfile con los argumentos asignados.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentProfile().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
