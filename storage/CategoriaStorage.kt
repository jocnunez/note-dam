package storage


import android.content.Context
import android.os.Environment
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson


import models.*
import java.io.*
import java.util.UUID


@Suppress("UNCHECKED_CAST")
class CategoriaStorage {

    /**
     * @author JiaCheng Zhang
     * @param context contexto de main, necesario para poder hacer los toast
     * @param categorias, lista de categorias que se van a exportar
     * Funcion que exporta las a un fichero csv cada categoria que hay
     */
    fun exportarCSV(context: Context, categoria: Categoria) {
        val fileName = "${categoria.nombre}.csv"
        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName)

        try {
            BufferedWriter(FileWriter(filePath)).use { writer ->
                // Escribir encabezados
                writer.append("checkBox|fechaCreacion|texto|urlImage|audio|subnotaCheckBox|subnotaFechaCreacion\n")
                // Escribir datos
                categoria.notas.forEach { nota ->
                    when (nota) {
                        is NotaText -> writer.append("${nota.checkBox}|${nota.fechaCreacion},${nota.texto}||||\n")
                        is NotaImagen -> writer.append("${nota.checkBox}|${nota.fechaCreacion}||${nota.urlImage}|||\n")
                        is NotaAudio -> writer.append("${nota.checkBox}|${nota.fechaCreacion}|||${nota.audio}||\n")
                        is NotaConSubnota -> {
                            val subnota = nota.subnota
                            writer.append("${nota.checkBox}|${nota.fechaCreacion}||||${subnota.checkBox}|${subnota.fechaCreacion}\n")
                        }

                        else -> {

                        }
                    }
                }
            }                // Mostrar Toast de éxito
            showToast(context, "Exportación exitosa")
        } catch (e: IOException) {
            e.printStackTrace()

            // Mostrar Toast de error
            showToast(context, "Error al exportar")
        }
    }

    /**
     * @author JiaCheng Zhang
     * @param context contexto de main, necesario para poder hacer los toast
     * @param categorias, lista de categorias que se van a exportar
     * Función que exporta las categorias y las notas a un fichero json
     */
    fun exportarJson(context: Context, categorias: List<Categoria>) {
        val fileName = "categorias.json"
        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName)

        try {
            FileWriter(filePath).use { writer ->
                val gson = Gson()

                // Crear una lista para almacenar la información exportada
                val categoriasExportadas = mutableListOf<List<Any>>()

                // Iterar sobre cada categoría y exportar información
                for (categoria in categorias) {
                    val categoriaExportada = listOf(
                        categoria.id,
                        categoria.isChecked,
                        categoria.prioridad,
                        categoria.nombre,
                        categoria.notas.map { nota -> exportarNota(nota) }

                    )
                    categoriasExportadas.add(categoriaExportada)
                }

                // Convertir la lista a formato JSON y escribir en el archivo
                val categoriasJson = gson.toJson(categoriasExportadas)
                writer.write(categoriasJson)

                // Mostrar Toast de éxito
                showToast(context, "Exportación de todas las categorías a JSON exitosa")

                // Dentro de la función exportarJson
                Log.d("ExportarJSON", "JSON exportado: $categoriasJson")
            }
        } catch (e: IOException) {
            e.printStackTrace()

            // Mostrar Toast de error
            showToast(context, "Error al exportar todas las categorías a JSON")
        }
    }

    /**
     * @param nota nota que se quiere exportar
     * Funcion que exporta las notas dependiendo del tipo de la nota
     * @return devuelve una lista de any con los campos de las notas exportadas
     */
    private fun exportarNota(nota: Nota): List<Any> {
        val notaExportada = mutableListOf<Any>(
            nota.checkBox,
            nota.fechaCreacion
        )
        when (nota) {
            is NotaText -> {
                notaExportada.add("NotaText")
                notaExportada.add(nota.texto)
            }

            is NotaImagen -> {
                notaExportada.add("NotaImagen")
                notaExportada.add(nota.urlImage)
            }

            is NotaAudio -> {
                notaExportada.add("NotaAudio")
                notaExportada.add(nota.audio)
            }

            is NotaConSubnota -> {
                notaExportada.add("NotaConSubnota")
                notaExportada.add(exportarSubnota(nota.subnota))
            }
        }

        return notaExportada
    }

    /**
     * @param subnota subnota que se quiere exportar
     * Exporta la subnota que se le pasa en la funcion padre
     * @return devuelve una lista con los campos de la subnota
     */
    private fun exportarSubnota(subnota: Subnota): List<Any> {
        return listOf(
            subnota.checkBox,
            subnota.fechaCreacion
        )
    }

    /**
     * @author JiaCheng Zhang
     * Funcion que importa las notas de una categoria concreta desde un fichero csv
     * @return devuelve la lista de notas importadaas
     */
    fun importarCSV(context: Context, nombreArchivo: String): List<Nota> {
        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), nombreArchivo)

        val notas = mutableListOf<Nota>()

        try {
            BufferedReader(FileReader(filePath)).use { reader ->
                // Leer la primera línea (encabezados) y omitirla
                reader.readLine()

                // Leer las líneas restantes
                var linea: String? = reader.readLine()
                while (linea != null) {
                    val columnas = linea.split("|")


                    val checkBox = columnas[0].toBoolean()
                    val fechaCreacion = columnas[1]

                    // Determinar el tipo de nota según las columnas disponibles
                    val nota: Nota = when {
                        (columnas[3].isEmpty() && columnas[4].isEmpty() && columnas[5].isEmpty() && columnas[6].isEmpty()) -> NotaText(
                            checkBox,
                            fechaCreacion,
                            columnas[2]
                        )

                        (columnas[2].isEmpty() && columnas[4].isEmpty() && columnas[5].isEmpty() && columnas[6].isEmpty()) -> NotaImagen(
                            checkBox,
                            fechaCreacion,
                            columnas[3]
                        )

                        (columnas[2].isEmpty() && columnas[3].isEmpty() && columnas[5].isEmpty() && columnas[6].isEmpty()) && columnas[4].isNotEmpty() -> NotaAudio(
                            checkBox,
                            fechaCreacion,
                            columnas[4]
                        )

                        (columnas[2].isEmpty() && columnas[3].isEmpty() && columnas[4].isEmpty()) && columnas[5].isNotEmpty() -> {
                            val subnotaCheckBox = columnas[5].toBoolean()
                            val subnotaFechaCreacion = columnas[6]
                            NotaConSubnota(checkBox, fechaCreacion, Subnota(subnotaCheckBox, subnotaFechaCreacion))
                        }

                        else -> {

                            throw IllegalArgumentException("Formato de línea CSV no válido: $linea")
                        }
                    }
                    notas.add(nota)

                    Log.e("Importaciones", nota.toString())
                    // Leer la siguiente línea
                    linea = reader.readLine()
                }

                showToast(context, "Importación exitosa")
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("ImportarCSV", "Error al importar desde CSV: ${e.message}")
            showToast(context, "Importación fallida")
        }

        return notas
    }

    /**
     * @author JiaCheng Zhang
     * Funcion que importa notas y categorias desde un fichero json
     * @return devuelve una lista de categorias importadas con las notas dentro
     */
    fun importarJson(context: Context, nombreArchivo: String): List<Categoria> {
        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), nombreArchivo)

        try {
            FileReader(filePath).use { reader ->
                val gson = Gson()
                val categoriasLista: List<List<Any>> = gson.fromJson(reader, List::class.java) as List<List<Any>>

                return categoriasLista.map { categoriaLista ->
                    val id = UUID.fromString(categoriaLista[0].toString())
                    val isChecked = categoriaLista[1] as Boolean
                    val prioridadCategoria = (categoriaLista[2] as Double).toInt()
                    val nombreCategoria = categoriaLista[3] as String
                    Log.e("ImportarJSON", "Importando Categoría: $nombreCategoria con prioridad: $prioridadCategoria")
                    val notas =
                        (categoriaLista[4] as List<List<Any>>).map { notaLista -> importarNotaDesdeLista(notaLista) }
                    Categoria(id, isChecked, prioridadCategoria, nombreCategoria, notas)
                }
            }

        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("ImportarJSON", "Error al importar desde JSON: ${e.message}")
            return emptyList()
        }
    }

    /**
     * @author JiaCheng Zhang
     * Funcion que importa las notas desde el fichero json y las clasifica dependiendo del tipo de nota
     * @return devuelve una la nota que se encuentra dentro de la categoria en la funcion padre.
     */
    private fun importarNotaDesdeLista(notaLista: List<Any>): Nota {
        val checkBox = notaLista[0] as Boolean
        val fechaCreacion = notaLista[1] as String

        return when (notaLista[2] as String) {
            "NotaText" -> {
                val texto = notaLista[3] as String
                val notaText = NotaText(checkBox, fechaCreacion, texto)
                Log.e("ImportarJSON", "Importando NotaText: $notaText")
                notaText
            }

            "NotaImagen" -> {
                val urlImage = notaLista[3] as String
                val notaImagen = NotaImagen(checkBox, fechaCreacion, urlImage)
                Log.e("ImportarJSON", "Importando NotaImagen: $notaImagen")
                notaImagen
            }

            "NotaAudio" -> {
                val audio = notaLista[3] as String
                val notaAudio = NotaAudio(checkBox, fechaCreacion, audio)
                Log.e("ImportarJSON", "Importando NotaAudio: $notaAudio")
                notaAudio
            }

            "NotaConSubnota" -> {
                val subnotaLista = notaLista[3] as List<Any>
                val subnota = importarSubnotaDesdeLista(subnotaLista)
                val notaConSubnota =
                    NotaConSubnota(checkBox, fechaCreacion, Subnota(subnota[0] as Boolean, subnota[1] as String))
                Log.e("ImportarJSON", "Importando NotaConSubnota: $notaConSubnota")
                notaConSubnota
            }

            else -> {
                // Tratar otro caso o lanzar una excepción si es necesario
                // Por ejemplo, lanzar una excepción para casos desconocidos
                throw IllegalArgumentException("Tipo de nota desconocido: ${notaLista[2]}")
            }
        }
    }

    /**
     * @author JiaCheng Zhang
     * Funcion que importa las subnotas desde el fichero json y las clasifica dependiendo del tipo de nota
     * @return devuelve una nota con subnota que se encuentra dentro de la categoria en la funcion padre.
     */
    private fun importarSubnotaDesdeLista(subnotaLista: List<Any>): List<Any> {
        val subnotaCheckBox = subnotaLista[0] as Boolean
        val subnotaFechaCreacion = subnotaLista[1] as String

        return listOf(subnotaCheckBox, subnotaFechaCreacion)
    }


    /**
     * @author JiaCheng Zhang
     * Funcion que muestra los toast.
     * @param context contexto de la clase main
     * @param s mensaje que se quiere mostrar en el toast.
     */
    private fun showToast(context: Context, s: String) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show()
    }
}




