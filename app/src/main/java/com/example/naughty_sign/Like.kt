package com.example.naughty_sign

/**
 * Estructura de datos que representa a una persona a la que se le ha dado like.
 */
data class Like(
    val id: Int,            // ID de la persona.
    val nombre: String,     // Nombre de la persona.
    val edad: Int,          // Edad de la persona.
    val ciudad: String      // Ciudad de la persona.
)
