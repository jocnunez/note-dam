package com.dam.ad.notedam.papelera

import android.os.Build
import androidx.annotation.RequiresApi
import com.dam.ad.notedam.papelera.Mapper.toDTOToDoList
import com.dam.ad.notedam.papelera.Mapper.toToDOList
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.google.gson.GsonBuilder
import java.io.File
import java.util.*

class Storage {
//PARTE DE CONTROL CSV´S

    /***
     * Funcion que escribe en CSV
     */
    fun writeCSV(archivo: ToDOList) {
        val path = "${System.getProperty("user.dir")}${File.separator}data${File.separator}${archivo.uuid}.csv"
        val fichero = File(path)
        // si no existe lo creamos
        if (!fichero.exists()) {
            fichero.createNewFile()
        }
        // Escribimos el encabezado, separados por comas
        fichero.writeText("${archivo.nombreList},${archivo.prioridad},${archivo.uuid}\n")
        archivo.listaToDo.forEach {
            fichero.appendText("${it}\n")
        }
    }
    /***
     * Funcion que lee la carpeta completa
     * @return List<ToDoList>
     */
    fun readAllCSV(filePath: String):List<ToDOList> {
        var nombre:String =""
        var prioridad:Int = 99
        var uuid: UUID = UUID.randomUUID()
        var listaClase:MutableList<String> = mutableListOf()
        var salida= ToDOList(nombre)
        var listaSalida:MutableList<ToDOList> = mutableListOf()
        val files = File(filePath).listFiles { _, name -> name.endsWith(".csv") }
        val listas = mutableListOf<ToDOList>()
        files.forEach {
            val lines = it.readLines()
            // Verificar que haya al menos una línea en el archivo
            if (lines.isNotEmpty()) {
                // Obtener los valores de la primera línea
                val columnas = lines[0].split(",")
                nombre = columnas[0]
                prioridad = columnas[1].toIntOrNull() ?: 99
                uuid = UUID.fromString(columnas[2])
                // Agregar el resto de las líneas a la lista de la clase
                listaClase.addAll(lines.drop(1))
            }
            salida.uuid = uuid
            salida.prioridad = prioridad
            salida.listaToDo = listaClase
            listaSalida.add(salida)
        }
            return  listaSalida
    }
    /***
     * Funcion que escribe en CSV todas las listas
     */
    fun whriteAllCSV(lista:MutableList<ToDOList>) {
        lista.forEach {
            writeCSV(it)
        }
    }

// PARTE CONTROL J.JHONSON´S

    /***
     * Funcion que lee un JSON y devuelve una ToDOList
     * @return ToDOList
     */
    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalStdlibApi::class)
    fun readAllJSON(Path: String):MutableList<ToDOList>{
        var salida = mutableListOf<ToDOList>()
        var pivote = mutableListOf<DTOToDoList>()
        //Todos los ficheros que terminen en _categories.json
        val files = File(Path).listFiles { _, name -> name.endsWith("json") }
        if(files.isNullOrEmpty()){
            return salida
        }
        files.forEach { file ->
                        val gson = GsonBuilder()
                            .setPrettyPrinting()
                            .registerTypeAdapter(DTOToDoList::class.java, DTOToDOListAdapterGson())
                            .create()
                        val jsonString = file.readText()
                        pivote.add(gson.fromJson(jsonString, DTOToDoList::class.java))
                }
        pivote.forEach {
            salida.add(it.toToDOList())
        }
        return salida
    }
    /***
     * Funcion que escribe el JSON
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun whriteJSON(archivo: ToDOList){
        val path = "${System.getProperty("user.dir")}${File.separator}data${File.separator}${archivo.uuid}.json"
        val fichero = File(path)
        if (!fichero.exists()) {
            fichero.createNewFile()
        }
        val gson = GsonBuilder().setPrettyPrinting()
                        .registerTypeAdapter(DTOToDoList::class.java, DTOToDOListAdapterGson())
                        .create()
        val jsonString = gson.toJson(archivo.toDTOToDoList())
        fichero.writeText(jsonString)
    }
    /***
     * Funcion que escribe la lista en JSON
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun whriteAllJSON(lista: List<ToDOList>){
       lista.forEach {
           whriteJSON(it)
       }
    }

// PARTE CONTROL XML


    /***
     * Funcion que Lee el XML
     * @return ToDOList
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun readAllXML(path: String): MutableList<ToDOList> {
            val files = File(path).listFiles { _, name -> name.endsWith("xml") }
            if (files.isNullOrEmpty()) {
                return mutableListOf()
            }
            val lista = mutableListOf<DTOToDoList>()
            val xmlMapper = XmlMapper(JacksonXmlModule().apply { setDefaultUseWrapper(false) }
            ).apply {
                enable(SerializationFeature.INDENT_OUTPUT)
                enable(SerializationFeature.WRAP_ROOT_VALUE)
                enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT) }
            files.forEach { file ->
                val dto = xmlMapper.readValue(file, DTOToDoList::class.java)
                lista.add(dto)
            }
            val salida:MutableList<ToDOList> =  mutableListOf()
            lista.forEach { salida.add(it.toToDOList()) }
            return salida
    }
    /***
     * Funcion que escribe el XML
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun writeXML(archivo: ToDOList){
        val path = "${System.getProperty("user.dir")}${File.separator}data${File.separator}${archivo.uuid}.xml"
        val fichero = File(path)
        if (!fichero.exists()) {
            fichero.createNewFile()
        }
        val xmlMapper = XmlMapper(JacksonXmlModule().apply { setDefaultUseWrapper(false) }
        ).apply {
                    enable(SerializationFeature.INDENT_OUTPUT)
                    enable(SerializationFeature.WRAP_ROOT_VALUE)
                    enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)}
        val dto = archivo.toDTOToDoList()
        xmlMapper.writeValue(fichero,dto)
    }
    /***
     * Funcion que escribe lista el XML
     * @return mutablelist<TodoList>
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun whriteAllXML(lista:MutableList<ToDOList>){
        lista.forEach { writeXML(it) }
    }

    // FUNCION COMUN ARCHIVOS LOCALES
    fun deleteAllTipo(path: String,tipoFichero:String){
        val files = File(path).listFiles { _, name -> name.endsWith(tipoFichero) }
        files.forEach { it.delete() }
    }
    fun delete(path: String,uuid: UUID){
        val files = File(path).listFiles { _, name -> name.contains(uuid.toString()) }
        files.forEach { it.delete() }
    }
}