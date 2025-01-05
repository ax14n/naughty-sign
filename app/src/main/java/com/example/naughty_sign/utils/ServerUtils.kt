package com.example.naughty_sign.utils

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.PrintWriter
import java.net.Socket
import kotlin.math.min


/**
 * Objecto que almacena funciones de utilidad que interactuan con el servidor de la aplicación.
 */
object ServerUtils {

    /**
     * Puerto del servidor.
     */
    private const val SERVER_PORT = 12345

    /**
     * Dirección IP del servidor
     */
    private const val IP_ADDRESS = "192.168.1.157"

    /**
     * Establece conexión con el servidor propio para solicitar subir imagenes y respaldarlas
     * de lado del servidor.
     */
    fun enviarImagenesPorSocket(uris: List<Uri>, contentResolver: ContentResolver) {
        // ------- { Se hace un hilo secundario para las operaciones } ------- //
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // ------- { Se conecta a un socket y se establece un Writer. } ------- //
                val client = Socket(IP_ADDRESS, SERVER_PORT)
                val outputStream = DataOutputStream(client.getOutputStream())

                // ------- { Se imprime el UUID de usuario para identificar donde guardarlo. } ------- //
                val userUID = LoggedUserUtils.obtenerUid()
                outputStream.writeUTF("write")
                outputStream.writeUTF(userUID)

                // ------- { Se envía la información al servidor y se reconstruye allí. } ------- //
                uris.forEach { uri ->
                    val inputStream: InputStream? = contentResolver.openInputStream(uri)
                    if (inputStream != null) {

                        // ------- { Obtención de los datos necesarios de la imagen. } ------- //
                        val fileBytes = inputStream.readBytes()
                        val fileName = uri.lastPathSegment ?: "imagen_sin_nombre"

                        // ------- { Se envía el nombre del archivo, su tamaño y su contenido. } ------- //
                        outputStream.writeUTF(fileName)
                        outputStream.writeLong(fileBytes.size.toLong())
                        outputStream.write(fileBytes)
                    } else {
                        Log.d("ax14n", "No se pudo abrir la imagen: $uri")
                    }
                }

                // ------- { Se pone final y se cierra el socket de momento. } ------- //
                outputStream.writeUTF("EOF")
                outputStream.flush()
                client.close()

                Log.d("ax14n", "Todas las imágenes fueron enviadas correctamente.")
            } catch (e: Exception) {
                Log.e("ax14n", "Error enviando imágenes: ${e.message}", e)
            }
        }
    }

    /**
     * Solicita la información del usuario por medio de una conexión con el servidor.
     */
    fun solicitarInformacionUsuario() {
        CoroutineScope(Dispatchers.IO).launch {

            try {
                // Conectarse al servidor
                val socket = Socket(IP_ADDRESS, SERVER_PORT)

                // Leer los datos del socket
                val input = socket.getInputStream().bufferedReader()

                // Escribe el dato que desea e indica el usuario.
                val output = PrintWriter(socket.getOutputStream())
                output.println("get")
                output.println(LoggedUserUtils.obtenerUid())
                output.flush()


                // Sitio donde se guardarán las líneas leídas
                val datosRecibidos = StringBuilder()

                // Leer línea por línea
                var linea: String?
                while (input.readLine().also { linea = it } != null) {
                    datosRecibidos.appendLine(linea)
                }


                // Procesar las líneas con el formato "key -> value"
                val datosMapeados = datosRecibidos.lines()
                    .filter { it.contains(" -> ") } // Asegurarnos de procesar solo líneas válidas
                    .associate { line ->
                        val (key, value) = line.split(" -> ", limit = 2) // Dividir en clave y valor
                        key.trim() to value.trim()
                    }

                // Imprimir los resultados
                println("Datos recibidos:")
                datosMapeados.forEach { (key, value) ->
                    println("$key -> $value")
                }

                // Cerramos los flujos de datos y el socket
                output.close()
                input.close()
                socket.close()
            } catch (e: Exception) {
                e.printStackTrace()
                println("Error al conectarse o procesar datos del servidor.")
            }
        }
    }

    /**
     * Establece conexión con el servidor para cargar las imagenes respaldadas.
     */
    fun solicitarImagenesPorSocket(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val client = Socket(IP_ADDRESS, SERVER_PORT)
                val outputStream = DataOutputStream(client.getOutputStream())
                val inputStream = DataInputStream(client.getInputStream())

                val userUID = LoggedUserUtils.obtenerUid()
                outputStream.writeUTF("read")
                outputStream.writeUTF(userUID)

                val resolver = context.contentResolver

                while (true) {
                    val fileName = inputStream.readUTF()
                    if ("EOF" == fileName) {
                        Log.d("ax14n", "Fin de transferencia recibido.")
                        break
                    }
                    val fileSize = inputStream.readLong()

                    val values = ContentValues().apply {
                        put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
                        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                        put(
                            MediaStore.Images.Media.RELATIVE_PATH,
                            Environment.DIRECTORY_PICTURES + "/MiApp"
                        )
                    }

                    val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                    if (uri != null) {
                        resolver.openOutputStream(uri)?.use { resolverOutputStream ->
                            val buffer = ByteArray(8192)
                            var remaining = fileSize

                            while (remaining > 0) {
                                val bytesRead = inputStream.read(
                                    buffer,
                                    0,
                                    min(buffer.size.toDouble(), remaining.toDouble()).toInt()
                                )
                                if (bytesRead == -1) throw IOException("Fin inesperado del flujo de datos.")
                                resolverOutputStream.write(buffer, 0, bytesRead)
                                remaining -= bytesRead.toLong()
                            }
                        }
                    } else {
                        throw IOException("No se pudo crear el archivo en MediaStore.")
                    }
                }

                outputStream.writeUTF("EOF")
                outputStream.flush()
                client.close()

                Log.d("ax14n", "Todas las imágenes fueron recibidas satisfactoriamente.")
            } catch (e: Exception) {
                Log.e("ax14n", "Error recibiendo imágenes: ${e.message}", e)
            }
        }
    }

}