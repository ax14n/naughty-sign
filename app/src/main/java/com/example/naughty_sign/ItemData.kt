package com.example.cardview

data class ItemData(
    val name: String,
    val age : Int,
    val city : String,
    val imageResId : Int

) {
    fun getFormattedAge(): String {
        return "$age años"
    }
}
