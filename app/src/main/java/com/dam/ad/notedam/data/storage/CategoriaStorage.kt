package com.dam.ad.notedam.data.storage

import android.content.Context
import android.os.Environment
import android.util.Log
import android.widget.Toast
import com.core.domain.models.Categoria
import com.core.domain.models.ListaCategorias
import com.core.domain.models.Preferences
import com.core.domain.models.notes.*
import com.google.gson.Gson
import org.simpleframework.xml.core.Persister
import org.simpleframework.xml.transform.RegistryMatcher
import org.simpleframework.xml.transform.Transform
import java.io.*
import java.util.*

@Suppress("UNCHECKED_CAST")
class CategoriaStorage {
    private val listaCategorias = mutableListOf<Categoria>()

    /**
     * @author JiaCheng Zhang
     * @param context contexto de main, necesario para poder hacer los toast
     * @param categoria, lista de categorias que se van a exportar
     * Funcion que exporta a un fichero csv cada categoria que hay
     */
    fun exportarCSV(context: Context, categorias: MutableList<Categoria>) {

        categorias.forEach {
            val fileName = "${it.nombre}.csv"
            val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName)

            try {
                BufferedWriter(FileWriter(filePath)).use { writer ->
                    // Escribir encabezados
                    writer.append("categoriaUuid|categoriaCheckbox|prioridad|categoriaNombre|uuid|checkBox|fechaCreacion|texto|urlImage|audio|subnotaUuid|subnotaCheckBox|subnotaFechaCreacion\n")

                    // Escribir datos
                    it.notas.forEach { nota ->
                        when (nota) {
                            is NotaText -> writer.append("${it.id}|${it.isChecked}|${it.prioridad}|${it.nombre}|${nota.uuid}|${nota.checkBox}|${nota.fechaCreacion}|${nota.texto}||||\n")
                            is NotaImagen -> writer.append("${it.id}|${it.isChecked}|${it.prioridad}|${it.nombre}|${nota.uuid}|${nota.checkBox}|${nota.fechaCreacion}||${nota.urlImage}|||\n")
                            is NotaAudio -> writer.append("${it.id}|${it.isChecked}|${it.prioridad}|${it.nombre}|${nota.uuid}|${nota.checkBox}|${nota.fechaCreacion}|||${nota.audio}||\n")
                            is NotaConSubnota -> {
                                nota.subnotaList.forEach { subnota ->
                                    writer.append("${it.id}|${it.isChecked}|${it.prioridad}|${it.nombre}|${nota.uuid}|${nota.checkBox}|${nota.fechaCreacion}||||${subnota.uuid}|${subnota.checkBox}|${subnota.fechaCreacion}\n")
                                }
                            }

                            else -> {
                                // idk
                            }
                        }
                    }
                    // Si no hay notas solamente escribe la categoría
                    if (it.notas.size == 0) {
                        writer.append("${it.id}|${it.isChecked}|${it.prioridad}|${it.nombre}|||||||||\n")
                    }
                }

                // Mostrar Toast de éxito
                showToast(context, "Exportación exitosa")
                Log.d("ExportCategoria", it.nombre)
            } catch (e: IOException) {
                e.printStackTrace()

                // Mostrar Toast de error
                showToast(context, "Error al exportar")
            }
        }
    }

    /**
     * @author JiaCheng Zhang
     * @param context contexto de main, necesario para poder hacer los toast
     * @param categorias, lista de categorias que se van a exportar
     * Función que exporta las categorias y las notas a un fichero json
     */
    fun exportarJson(context: Context, categorias: List<Categoria>) {
        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), Preferences.JSON_FILE.clave)

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
            nota.uuid,
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
                notaExportada.add(nota.subnotaList.map { subnota -> exportarSubnota(subnota) })
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
            subnota.uuid,
            subnota.checkBox,
            subnota.fechaCreacion
        )
    }

    /**
     * @author JiaCheng Zhang
     * Funcion que importa las notas de una categoria concreta desde un fichero csv
     * @return devuelve la lista de notas importadaas
     **/
    fun importarCSV(context: Context): MutableList<Categoria> {
        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString())
        val csvFiles = filePath.listFiles { _, nombre -> nombre.lowercase(Locale.ROOT).endsWith(".csv") }?.toList()
        val categoriasImportadas = mutableListOf<Categoria>()
        var categoriasFinales = mutableListOf<Categoria>()

        if (!csvFiles.isNullOrEmpty()) {
            csvFiles.forEach { file ->
                try {
                    val notas = mutableListOf<Nota?>()
                    val subnotasTempMap = mutableMapOf<UUID, MutableList<Subnota>>()

                    BufferedReader(FileReader(file)).use { reader ->
                        // Leer la primera línea (encabezados) y omitirla
                        reader.readLine()

                        // Leer las líneas restantes
                        var linea: String? = reader.readLine()
                        while (linea != null) {
                            val columnas = linea.split("|")

                            Log.e("Importaciones", "Columnas CSV: $columnas")

                            if (columnas.size >= 4) {
                                val categoriaUuid = UUID.fromString(columnas[0])
                                val categoriaCheckbox = columnas[1].toBoolean()
                                val prioridad = columnas[2].toInt()
                                val nombreCategoria = columnas[3]

                                // Si no hay notas, pues no se procesan
                                if (columnas[4] == " ") {
                                    val uuid = UUID.fromString(columnas[4])
                                    val checkBox = columnas[5].toBoolean()
                                    val fechaCreacion = columnas[6]

                                    // Determinar el tipo de nota según las columnas disponibles
                                    val nota: Nota? = when {
                                        (columnas.size >= 7 && columnas[7].isNotEmpty()) -> NotaText(
                                            uuid,
                                            checkBox,
                                            fechaCreacion,
                                            columnas[7]
                                        )

                                        (columnas.size >= 9 && columnas[8].isNotEmpty()) -> NotaImagen(
                                            uuid,
                                            checkBox,
                                            fechaCreacion,
                                            columnas[8]
                                        )

                                        (columnas.size >= 10 && columnas[9].isNotEmpty()) -> NotaAudio(
                                            uuid,
                                            checkBox,
                                            fechaCreacion,
                                            columnas[9]
                                        )

                                        (columnas.size >= 12 && columnas[10].isNotEmpty() && columnas[11].isNotEmpty()) -> {
                                            val subnotaUuid = UUID.fromString(columnas[10])
                                            val subnotaCheckBox = columnas[11].toBoolean()
                                            val subnotaFechaCreacion = columnas[12]

                                            subnotasTempMap
                                                .getOrPut(uuid) { mutableListOf() }
                                                .add(Subnota(subnotaUuid, subnotaCheckBox, subnotaFechaCreacion))
                                            null
                                        }

                                        else -> {
                                            throw IllegalArgumentException("Formato de línea CSV no válido: $linea, tamaño: %${columnas.size}")
                                        }
                                    }
                                    notas.add(nota)
                                }

                                val notasTemporales = mutableListOf<Nota>()

                                val categoria = Categoria(
                                    categoriaUuid,
                                    categoriaCheckbox,
                                    prioridad,
                                    nombreCategoria,
                                    notasTemporales
                                )
                                categoriasImportadas.add(categoria)
                            } else {
                                Log.e("Importaciones", "Error: Columnas insuficientes en la línea: $linea")
                            }

                            // Leer la siguiente línea
                            linea = reader.readLine()
                        }
                    }

                    // Procesar subnotas agrupándolas por UUID
                    subnotasTempMap.forEach { (notaUuid, subnotas) ->
                        val notaPrincipal = notas.find { it is Nota && it.uuid == notaUuid }

                        if (notaPrincipal != null && notaPrincipal is NotaConSubnota) {
                            // Si encontramos la nota principal, agregamos las subnotas
                            notaPrincipal.subnotaList.addAll(subnotas)

                        } else {
                            // Si no encontramos la nota principal, creamos una nueva nota con subnota
                            notas.add(
                                NotaConSubnota(
                                    notaUuid,
                                    false,
                                    "",
                                    mutableListOf<Subnota>().apply { addAll(subnotas) })
                            )
                        }
                    }

                    //
                    categoriasFinales = categoriasImportadas.distinctBy { it.id } as MutableList<Categoria>
                    categoriasImportadas.clear()
                    categoriasImportadas.addAll(categoriasFinales)

                    //Elimino los nulls de la lista de notas, ya que en el caso de haber una subnota, esta se almacena aqui como null
                    val notasLimpias = limpiarNotas(notas)
                    categoriasFinales.forEach {
                        if (it.notas.size == 0) {
                            it.addNotas(notasLimpias)
                        }
                    }


                } catch (e: IOException) {
                    e.printStackTrace()
                    Log.e("ImportarCSV", "Error al importar desde CSV: ${e.message}")
                    showToast(context, "Importación fallida")
                }
            }
        }

        categoriasFinales.forEach { categoria ->
            Log.e("Importaciones", "Categoría Importada: ${categoria.nombre}")
            categoria.notas.forEachIndexed { index, nota ->
                Log.e("Importaciones", "  Nota $index: $nota")
            }
        }

        return categoriasFinales
    }

    private fun limpiarNotas(notas: MutableList<Nota?>): MutableList<Nota> {

        val notasLimpias = mutableListOf<Nota>()
        val iterator = notas.iterator()
        while (iterator.hasNext()) {
            val nota = iterator.next()
            if (nota == null) {
                iterator.remove()
            } else {
                notasLimpias.add(nota)
            }
        }

        return notasLimpias
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
                    println(categoriaLista[2].toString())
                    val id = UUID.fromString(categoriaLista[0].toString())
                    val isChecked = categoriaLista[1] as Boolean
                    val prioridadCategoria = (categoriaLista[2] as Double).toInt()

                    val nombreCategoria = categoriaLista[3] as String
                    Log.e("ImportarJSON", "Importando Categoría: $nombreCategoria con prioridad: $prioridadCategoria")
                    val notas =
                        (categoriaLista[4] as List<List<Any>>).map { notaLista -> importarNotaDesdeLista(notaLista) }
                    Categoria(id, isChecked, prioridadCategoria, nombreCategoria, notas as MutableList<Nota>)
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
        val uuid = UUID.fromString(notaLista[0].toString())
        val checkBox = notaLista[1] as Boolean
        val fechaCreacion = notaLista[2] as String

        return when (notaLista[3] as String) {
            "NotaText" -> {
                val texto = notaLista[4] as String
                val notaText = NotaText(uuid, checkBox, fechaCreacion, texto)
                Log.e("ImportarJSON", "Importando NotaText: $notaText")
                notaText
            }

            "NotaImagen" -> {
                val urlImage = notaLista[4] as String
                val notaImagen = NotaImagen(uuid, checkBox, fechaCreacion, urlImage)
                Log.e("ImportarJSON", "Importando NotaImagen: $notaImagen")
                notaImagen
            }

            "NotaAudio" -> {
                val audio = notaLista[4] as String
                val notaAudio = NotaAudio(uuid, checkBox, fechaCreacion, audio)
                Log.e("ImportarJSON", "Importando NotaAudio: $notaAudio")
                notaAudio
            }


            "NotaConSubnota" -> {
                val subnotaLista = notaLista[4] as List<List<Any>>
                val subnotas = subnotaLista.map { subnota -> importarSubnotaDesdeLista(subnota) }
                Log.e("ImportarJSON", "Importando NotaConSubnota: $subnotas")
                val subnotasCorrectas = subnotas.map { subnota ->
                    Subnota(
                        UUID.fromString(subnota[0].toString()),
                        subnota[1] as Boolean,
                        subnota[2].toString()
                    )
                }
                NotaConSubnota(uuid, checkBox, fechaCreacion, subnotasCorrectas.toMutableList())
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
        val subnotaUuid = UUID.fromString(subnotaLista[0].toString())
        val subnotaCheckBox = subnotaLista[1] as Boolean
        val subnotaFechaCreacion = subnotaLista[2] as String
        return listOf(subnotaUuid, subnotaCheckBox, subnotaFechaCreacion)
    }

    /**
     * @author JiaCheng Zhang
     * @return Funcion que devuelve todas las categorias
     */
    fun getAllCategories(): MutableList<Categoria> {
        return listaCategorias
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


    private val persister: Persister

    init {
        val matcher = RegistryMatcher()
        matcher.bind(UUID::class.java, UUIDTransform())
        persister = Persister(matcher)
    }

    fun importDataXML(context: Context): List<Categoria> {
        val file = File(context.getExternalFilesDir(null)?.absolutePath!!, "DataXml")
        val physicalFile = File(file, "categorias.xml")
        if (!physicalFile.exists()) return listOf()
        return persister.read(ListaCategorias::class.java, physicalFile).categorias
    }

    fun exportDataXML(context: Context, data: List<Categoria>) {
        val file = File(context.getExternalFilesDir(null)?.absolutePath!!, "DataXml")
        val physicalFile = File(file, "categorias.xml")
        if (!file.exists()) {
            file.mkdirs()
        }
        persister.write(ListaCategorias(data), physicalFile)
        BufferedReader(FileReader(physicalFile)).use {
            while (it.ready()){
                Log.i("xml", it.readLine().toString())
            }
        }
    }

    class UUIDTransform : Transform<UUID> {
        override fun read(value: String?): UUID {
            return UUID.fromString(value)
        }

        override fun write(value: UUID?): String {
            return value?.toString() ?: ""
        }
    }
}



