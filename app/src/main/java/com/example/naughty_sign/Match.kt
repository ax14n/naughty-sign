package com.example.naughty_sign

/**
 * Estructura de datos que representa al usuario con el que se ha logrado un match.
 */
data class Match(
    val id: Int,            // Id del usuario del match.
    val nombre: String,     // Nombre del usuario del match.
    val edad: Int,          // Edad del usuario del match.
    val ciudad: String      // Y ciudad donde vive el usuario del match.
)

