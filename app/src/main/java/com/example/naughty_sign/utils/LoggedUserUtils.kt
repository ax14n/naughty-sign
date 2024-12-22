package com.example.naughty_sign.utils

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import com.example.naughty_sign.firebase.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

/**
 * Clase que centraliza las operaciones relacionadas con el usuario de la aplicación.
 */
object LoggedUserUtils {

    /**
     * Acceso a la base de datos de la aplicación.
     */
    @SuppressLint("StaticFieldLeak")
    private val database = Firebase.firestore

    /**
     * Usuario actual de la aplicación.
     */
    private val perfil = Firebase.auth.currentUser


    /**
     * Obtiene el email del usuario y lo devuelve en forma de cadena.
     */
    fun obtenerEmail(): String {
        return perfil?.email.toString()
    }

    /**
     * Obtiene el uid y lo devuelve en forma de cadena.
     */
    fun obtenerUid(): String {
        return perfil?.uid.toString()
    }

    fun actualizar(campo: String, valor: Any?) {
        val collection = database.collection("Usuarios").document(obtenerUid())

        // Source can be CACHE, SERVER, or DEFAULT.
        val source = Source.SERVER

        // Get the document, forcing the SDK to use the offline cache
        collection.get(source).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Document found in the offline cache
                val document = task.result
                if (document != null && document.exists()) {
                    // Actualizar el campo "ciudad" en el documento
                    document.reference.update(campo, valor)
                        .addOnSuccessListener {
                            Log.d(TAG, "$campo updated to: $valor")
                        }
                        .addOnFailureListener { e ->
                            Log.e(TAG, "Error updating $campo", e)
                        }
                } else {
                    Log.w(TAG, "Document doesn't exist.")
                }

            } else {
                Log.d(TAG, "Cached get failed: ", task.exception)
            }
        }
    }

    /**
     * Crea un documento en la base de datos que será usado por el usuario a partir de ahora.
     */
    fun crearDocumento(bundle: Bundle) {
        // Obtengo la colección llamada 'Usuarios'.
        val collection = database.collection("Usuarios")

        // Operación de agregación del usuario a la base de datos.
        collection.get().addOnSuccessListener { document ->
            if (document != null && !document.isEmpty) {
                val user = User(
                    bundle.getString("id"),
                    bundle.getString("email"),
                    bundle.getString("nombre"),
                    bundle.getString("cita"),
                    "",
                    "",
                    "",
                    listOf(),
                    "",
                    "",
                    bundle.getInt("edad"),
                )
                collection.document(bundle.getString("id").toString()).set(user)
            }
        }
    }

    /**
     * Extrae los datos almacenados del perfil actual y los devuelve en forma de Bundle.
     */
    fun extraerDatosPerfil(callback: (Bundle) -> Unit) {
        val collection = database.collection("Usuarios").document(obtenerUid())
        val source = Source.SERVER

        collection.get(source).addOnCompleteListener { task ->
            val bundle = Bundle()
            if (task.isSuccessful) {
                val document = task.result
                if (document != null && document.exists()) {
                    bundle.apply {
                        putString("nombre", document.getString("nombre"))
                        putString("cita", document.getString("cita"))
                        putString("descripcion", document.getString("descripcion"))
                        putString("profesion", document.getString("profesion"))
                        putString("ciudad", document.getString("ciudad"))
                        putStringArrayList("intereses", document.get("intereses") as ArrayList<String>)
                    }
                } else {
                    Log.w(TAG, "Document doesn't exist.")
                }
            } else {
                Log.d(TAG, "Cached get failed: ", task.exception)
            }
            // Llama al callback pasando el bundle lleno o vacío
            callback(bundle)
        }
    }


}