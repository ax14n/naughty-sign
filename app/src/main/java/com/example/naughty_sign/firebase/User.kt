package com.example.naughty_sign.firebase

data class User(
    val id: String?,                    // ID del usuario.
    val email: String?,             // Email del usuario
    val nombre: String?,             // Nombre del usuario.
    val cita: String?,               // Cita del usuario.
    val profesion: String?,          // Profesión del usuario.
    val ciudad: String?,             // Ciudad del usuario.
    val descripcion: String?,        // Descripción del usuario.
    val intereses: List<String?>,    // Intereses del usuario.
    val foto_perfil: String?,         // Intereses del usuario.
    val ubicacion: String?,  // Enlace de Google Maps con la ubicación
    val edad: Int
) {
    fun getFormattedAge(): String {
        return buildString {
            append(edad)
            append(" years")
        }
    }
}