package com.example.naughty_sign

/**
 * Estructura de datos que representa los datos que tendrá un usuario
 * en especifico dentro de su perfil.
 */
data class User(
    val id: Int,                    // ID del usuario.
    val nombre: String,             // Nombre del usuario.
    val cita: String,               // Cita del usuario.
    val profesion: String,          // Profesión del usuario.
    val ciudad: String,             // Ciudad del usuario.
    val descripcion: String,        // Descripción del usuario.
    val intereses: List<String>     // Intereses del usuario.
)
