package dev.ivanronco.primeraappaccesodatos.dataStorageService.json

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dev.ivanronco.primeraappaccesodatos.dataStorageService.IDataSetorage
import dev.ivanronco.primeraappaccesodatos.dto.categoria.ListaCategoriasDto
import dev.ivanronco.primeraappaccesodatos.dto.mapper.toCategorias
import dev.ivanronco.primeraappaccesodatos.dto.mapper.toDto
import dev.ivanronco.primeraappaccesodatos.models.categoria.ListaCategorias
import java.io.File
import java.nio.file.Files

@OptIn(ExperimentalStdlibApi::class)
class JsonStorageService(
    context: Context
): IDataSetorage<ListaCategorias> {

    private val file = File(context.getExternalFilesDir(null)?.absolutePath!!, "DataJson")
    private val pyshicalFile = File(file, "categorias.json")
    private val jsonAdapter = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build().adapter<ListaCategoriasDto>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun importData(): ListaCategorias {
        if(!pyshicalFile.exists()) return ListaCategorias(emptyList())
        return jsonAdapter.fromJson(pyshicalFile.readText())!!.toCategorias()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun exportData(data: ListaCategorias) {
        if(!file.exists()){
            file.mkdirs()
        }
        pyshicalFile.writeText(jsonAdapter.indent("   ").toJson(data.toDto()))
    }
}