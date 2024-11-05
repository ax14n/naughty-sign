package com.example.naughty_sign

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.naughty_sign.databinding.FragmentProfileBinding

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

    // Instancia del menú que será mostrada en el PopupMenu.
    private lateinit var menu: Menu

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
     * Configura el comportamiento del botón de configuración, que despliega un menú al ser pulsado.
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

        // Configura el botón de configuración para mostrar el menú desplegable al hacer clic
        binding.configButton.setOnClickListener {
            mostrarMenuDesplegable(it)
        }

        // Retorna la vista raíz del binding (la vista principal del layout del fragmento)
        return binding.root
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
                R.id.go_settings_button -> {
                    // Crea un intent para abrir la actividad ConfigurationActivity
                    val intent = Intent(context, ConfigurationActivity::class.java)
                    startActivity(intent)
                    true
                }

                // Opción "Acerca de Nosotros"
                R.id.about_us_button -> {
                    // Muestra un mensaje Toast como respuesta
                    Toast.makeText(context, "Opción 2 seleccionada", Toast.LENGTH_SHORT).show()
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
