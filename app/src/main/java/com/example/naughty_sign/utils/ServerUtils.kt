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
    private const val SERVER_ADDRESS = "192.168.1.157"

    /**
     * Establece conexión con el servidor propio para solicitar subir imagenes y respaldarlas
     * de lado del servidor.
     */
    fun sendImagesThroughSocket(uris: List<Uri>, contentResolver: ContentResolver) {
        // ------- { Se hace un hilo secundario para las operaciones } ------- //
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // ------- { Se conecta a un socket y se establece un Writer. } ------- //
                val client = Socket(SERVER_ADDRESS, SERVER_PORT)

                // ------- { Establecemiento de escritura a través de Socket. } ------- //
                val outputStream = DataOutputStream(client.getOutputStream())

                // ------- { Se informa la acción a realizar y el usuario que consulta. } ------- //
                outputStream.writeUTF("write")
                outputStream.writeUTF(LoggedUserUtils.obtenerUid())

                // ------- { Comienzo de proceso de envío de la imagen } ------- //
                uris.forEach { uri ->

                    /**
                     * Se abre un flujo de datos a la imagen especificada por el URI proporcionado
                     * por la lambda actual. Esto nos permite leer el contenido de la imagen.
                     */
                    val inputStream: InputStream? = contentResolver.openInputStream(uri)

                    if (inputStream != null) {

                        /**
                         * Se extraen los datos necesarios como el tamaño de la imagen y el nombre
                         * del fichero para pasarselo al servidor. En caso de que la imagen
                         * no tenga un nombre definido, entonces se le asignará 'imagen_sin_nombre'.
                         */
                        val fileBytes = inputStream.readBytes()
                        val fileName = uri.lastPathSegment ?: "imagen_sin_nombre"

                        /**
                         * Se escribe pasa al servidor la información necesaria para la formación
                         * de la imagen.
                         */
                        outputStream.writeUTF(fileName) // Nombre del fichero.
                        outputStream.writeLong(fileBytes.size.toLong()) // Tamaño del fichero.
                        outputStream.write(fileBytes) // Contenido del fichero.
                    } else {
                        Log.d("ax14n", "No se pudo abrir la imagen: $uri")
                    }
                }

                // ------- { Informe al servidor de que se ha acabado el proceso. } ------- //
                outputStream.writeUTF("EOF")
                outputStream.flush()

                // ------- { Cerrado de flujos de datos y puerto utilizado. } ------- //
                outputStream.close()
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
    fun requestUserInfo() {
        // ------- { Se hace un hilo secundario para las operaciones } ------- //
        CoroutineScope(Dispatchers.IO).launch {

            try {
                // ------- { Se establece conexión con el servidor. } ------- //
                val socket = Socket(SERVER_ADDRESS, SERVER_PORT)

                // ------- { Establecemiento de lectura y escritura a través de Socket. } ------- //
                val input = socket.getInputStream().bufferedReader()
                val output = PrintWriter(socket.getOutputStream())

                // ------- { Se informa la acción a realizar y el usuario que consulta. } ------- //
                output.println("get")
                output.println(LoggedUserUtils.obtenerUid())
                output.flush()

                // ------- { Pasos previos al recebimiento de información } ------- //
                val datosRecibidos = StringBuilder()
                var linea: String?

                // ------- { Se recibe la información y se almacena. } ------- //
                while (input.readLine().also { linea = it } != null) {
                    datosRecibidos.appendLine(linea)
                }

                // ------- { Conversión de texto a diccionario K.V } ------- //
                val datosMapeados = datosRecibidos.lines()
                    .filter { it.contains(" -> ") } // Asegurarnos de procesar solo líneas válidas
                    .associate { line ->
                        val (key, value) = line.split(" -> ", limit = 2) // Dividir en clave y valor
                        key.trim() to value.trim()
                    }

                // ------- { Impresión de resultados. } ------- //
                println("Datos recibidos:")
                datosMapeados.forEach { (key, value) ->
                    println("$key -> $value")
                }

                // ------- { Cerrado de flujos de datos y puerto utilizado. } ------- //
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
    fun requestImagesThroughSocket(context: Context) {
        // ------- { Se hace un hilo secundario para las operaciones } ------- //
        CoroutineScope(Dispatchers.IO).launch {
            try {

                // ------- { Se establece conexión con el servidor. } ------- //
                val client = Socket(SERVER_ADDRESS, SERVER_PORT)

                // ------- { Establecemiento de lectura y escritura a través de Socket. } ------- //
                val outputStream = DataOutputStream(client.getOutputStream())
                val inputStream = DataInputStream(client.getInputStream())

                // ------- { Se informa la acción a realizar y el usuario que consulta. } ------- //
                outputStream.writeUTF("read")
                outputStream.writeUTF(LoggedUserUtils.obtenerUid())
                outputStream.flush()

                // ------- { Se prepara el gestionador del sistema } ------- //
                val resolver = context.contentResolver

                // ------- { Comienzo de proceso de recibimiento de la imagen } ------- //
                while (true) {

                    /**
                     * Se obtiene el nombre del fichero para su formación o 'EOF' como indicativo
                     * de que no hay más imágenes que recibir. Recibir 'EOF' como resultado
                     * del nombre del fichero significa la parada de ejecución de esta función,
                     * dando por hecho que todas las imagenes han sido transferidas al dispositivo.
                     */
                    val fileName = inputStream.readUTF()
                    if ("EOF" == fileName) {
                        Log.d("ax14n", "Fin de transferencia recibido.")
                        break
                    }

                    /**
                     * Se solicita y almacena el tamaño de la imagen. Sirve como prueba de hasta
                     * donde hay que leer en los bytes que serán mandados por el servidor.
                     */
                    val fileSize = inputStream.readLong()

                    /**
                     * Se forman los metadatos de la imagen preparandola para su posterior inserción
                     * en el sistema.
                     */
                    val values = ContentValues().apply {
                        put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
                        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                        put(
                            MediaStore.Images.Media.RELATIVE_PATH,
                            Environment.DIRECTORY_PICTURES + "/MiApp"
                        )
                    }

                    /**
                     * Se informa al resolver encargado de gestionar el sistema donde se almacenará
                     * la imagen y se le proporciona sus metadatos.
                     */
                    val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

                    if (uri != null) {

                        /**
                         * Se comienza a escibir el contenido de la imagen.
                         */
                        resolver.openOutputStream(uri)?.use { resolverOutputStream ->
                            val buffer = ByteArray(8192)    // Tramos de escritura.
                            var remaining = fileSize             // Restante a leer.

                            while (remaining > 0) {
                                val bytesRead = inputStream.read(
                                    buffer, // Tramo de Bytes a leer.
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

                // ------- { Informe al servidor de que se ha acabado el proceso. } ------- //
                outputStream.writeUTF("EOF")
                outputStream.flush()

                // ------- { Cerrado de flujos de datos y liberando puerto utilizado. } ------- //
                outputStream.close()
                inputStream.close()
                client.close()

                Log.d("ax14n", "Todas las imágenes fueron recibidas satisfactoriamente.")
            } catch (e: Exception) {
                Log.e("ax14n", "Error recibiendo imágenes: ${e.message}", e)
            }
        }
    }

    /**
     * Realiza una consulta al servidor local para solicitar la lista de likes que ha dado un usuario.
     */
    fun checkLikes() {
        // ------- { Se hace un hilo secundario para las operaciones } ------- //
        CoroutineScope(Dispatchers.IO).launch {

            try {
                // ------- { Se establece conexión con el servidor. } ------- //
                val socket = Socket(SERVER_ADDRESS, SERVER_PORT)

                // ------- { Establecemiento de lectura y escritura a través de Socket. } ------- //
                val input = socket.getInputStream().bufferedReader()
                val output = PrintWriter(socket.getOutputStream())

                // ------- { Se informa la acción a realizar y el usuario que consulta. } ------- //
                output.println("likes")
                output.println(LoggedUserUtils.obtenerUid())
                output.flush()

                // ------- { Preparación para el recibimiento de la información } ------- //
                val datosRecibidos = StringBuilder()
                var linea: String?

                // ------- { Se extrae la información del servidor y se almacena. } ------- //
                while (input.readLine().also { linea = it } != null) {
                    datosRecibidos.appendLine(linea)
                }

                // ------- { Se convierte la información obtenida en una lista de datos } ------- //
                val likes = datosRecibidos.lines()

                // ------- { Se muestran los datos obtenidos. } ------- //
                println("Datos recibidos:")
                likes.forEach {
                    println(it)
                }

                // ------- { Cerrado de flujos de datos y liberando puerto utilizado. } ------- //
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
     * Realiza una consulta al servidor local para solicitar la lista de matches que ha logrado.
     */
    fun checkMatches() {

        CoroutineScope(Dispatchers.IO).launch {

            try {
                // ------- { Se establece conexión con el servidor. } ------- //
                val socket = Socket(SERVER_ADDRESS, SERVER_PORT)

                // ------- { Establecemiento de lectura y escritura a través de Socket. } ------- //
                val input = socket.getInputStream().bufferedReader()
                val output = PrintWriter(socket.getOutputStream())

                // ------- { Se informa la acción a realizar y el usuario que consulta. } ------- //
                output.println("matches")
                output.println(LoggedUserUtils.obtenerUid())
                output.flush()

                // ------- { Preparación para el recibimiento de la información } ------- //
                val datosRecibidos = StringBuilder()
                var linea: String?

                // ------- { Se extrae la información del servidor y se almacena. } ------- //
                while (input.readLine().also { linea = it } != null) {
                    datosRecibidos.appendLine(linea)
                }

                // ------- { Se convierte la información obtenida en una lista de datos } ------- //
                val matches = datosRecibidos.lines()

                // ------- { Se muestran los datos obtenidos. } ------- //
                println("Datos recibidos:")
                matches.forEach {
                    println(it)
                }

                // ------- { Cerrado de flujos de datos y liberando puerto utilizado. } ------- //
                output.close()
                input.close()
                socket.close()

            } catch (e: Exception) {
                e.printStackTrace()
                println("Error al conectarse o procesar datos del servidor.")
            }
        }
    }

}