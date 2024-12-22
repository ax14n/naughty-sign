package com.example.naughty_sign.utils

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast

/**
 * Muestra mensajes por pantalla.
 */
object MessageUtils {

    /**
     * Muestra un Toast de poca duración.
     */
    fun mostrarToast(context: Context, mensaje: String) {
        Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show()
    }

    /**
     * Muestra un Toast de larga duración.
     */
    fun mostrarToastLargo(context: Context, mensaje: String) {
        Toast.makeText(context, mensaje, Toast.LENGTH_LONG).show()
    }

    /**
     * Muestra un Toast de duración personalizada.
     */
    fun mostrarToastPersonalizado(context: Context, mensaje: String, duracion: Int) {
        Toast.makeText(context, mensaje, duracion).show()
    }

    /**
     * Muestra un dialgo con un mensaje y un botón de aceptar..
     */
    fun mostrarDialogoAlerta(context: Context, mensaje: String) {
        AlertDialog.Builder(context)
            .setMessage(mensaje)
            .setPositiveButton("Aceptar", null)
            .show()
    }
}