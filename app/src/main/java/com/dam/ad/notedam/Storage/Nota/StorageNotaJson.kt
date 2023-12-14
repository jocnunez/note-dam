import android.util.Log
import com.dam.ad.notedam.Models.JsonAdapters.NotaTypeAdapter
import com.dam.ad.notedam.Models.nota.Nota
import com.dam.ad.notedam.Models.nota.NotaImagen
import com.dam.ad.notedam.Models.nota.NotaLista
import com.dam.ad.notedam.Models.nota.NotaTexto
import com.dam.ad.notedam.Storage.IStorageLocal
import com.dam.ad.notedam.utils.Utils
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.lang.reflect.Type
import java.util.*

class StorageNotaJson : IStorageLocal<Nota> {

    lateinit var gsonPretty : Gson

    init {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.registerTypeAdapter(NotaTexto::class.java, NotaTypeAdapter())
        gsonBuilder.registerTypeAdapter(NotaImagen::class.java, NotaTypeAdapter())
        gsonBuilder.registerTypeAdapter(NotaLista::class.java, NotaTypeAdapter())

        gsonBuilder.setPrettyPrinting()
        gsonPretty = gsonBuilder.create()
    }

    override fun loadAllItems(uuid: UUID): MutableList<Nota> {
        val folder = File(Utils.mainActivity!!.filesDir, "Notas")
        val file = File(folder, "$uuid.json")

        Log.i("StorageJson", "Leyendo JSON")
        Log.i("StorageJson", "Ruta del archivo: ${file.absolutePath}")

        val listaNotas = mutableListOf<Nota>()

        try {
            if (!file.exists()) {
                Log.e("StorageJson", "El archivo no existe")
                return listaNotas
            }

            BufferedReader(FileReader(file)).use { reader ->
                val jsonArray = JsonParser.parseReader(reader).asJsonArray
                for (jsonElement in jsonArray) {
                    val jsonObject = jsonElement.asJsonObject
                    val tipoNota = jsonObject.get("tipoNota").asString
                    // Utiliza el adaptador para deserializar la nota según su tipo
                    val nota: Nota = gsonPretty.fromJson(jsonObject, getType(tipoNota))
                    listaNotas.add(nota)
                }
            }
        } catch (e: Exception) {
            Log.e("StorageJson", "Error al leer el archivo", e)
        }

        listaNotas.apply { this.sortBy { it.prioridad } }
        var contador = 0
        listaNotas.forEach {
            it.prioridad = contador
            contador += 1
        }

        return listaNotas
    }


    override fun saveAllItems(uuid: UUID, listaItems: MutableList<Nota>) {
        Log.i("StorageJson", "Writing All Items JSON - Inicio")
        val folder = File(Utils.mainActivity!!.filesDir, "Notas")
        val file = File(folder, "$uuid.json")

        try {
            Log.i("StorageJson", "Before writing to file")
            file.bufferedWriter().use { writer ->
                gsonPretty.toJson(listaItems, writer)
            }
            Log.i("StorageJson", "After writing to file")
        } catch (e: Exception) {
            Log.e("StorageJson", "Error al escribir en el archivo", e)
        }
        Log.i("StorageJson", "Writing All Items JSON - Fin")
    }




    private fun getType(tipoNota: String?): Type {
        return when (tipoNota) {
            "Texto" -> object : TypeToken<NotaTexto>() {}.type
            "Imagen" -> object : TypeToken<NotaImagen>() {}.type
            "Lista" -> object : TypeToken<NotaLista>() {}.type
            else -> throw IllegalArgumentException("Tipo de nota desconocido: $tipoNota")
        }
    }
}