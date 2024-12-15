package com.example.naughty_sign.recycleview

data class ItemData(
    val nombre: String,     // Nombre del usuario del match.
    val edad: Int,          // Edad del usuario del match.
    val ciudad: String,     // Y ciudad donde vive el usuario del match
    val id: Int             // Id del usuario del match.
) {
    fun getFormattedAge(): String {
        return buildString {
            append("$edad ")
            append(" years")
        }
    }
}