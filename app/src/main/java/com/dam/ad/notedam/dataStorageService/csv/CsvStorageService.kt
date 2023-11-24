package dev.ivanronco.primeraappaccesodatos.dataStorageService.csv

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import dev.ivanronco.primeraappaccesodatos.dataStorageService.IDataSetorage
import dev.ivanronco.primeraappaccesodatos.models.TODO.*
import dev.ivanronco.primeraappaccesodatos.models.TipoTODO.TipoTODO
import dev.ivanronco.primeraappaccesodatos.models.categoria.Categoria
import java.io.*
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
class CsvStorageService(
    context: Context
): IDataSetorage<List<Categoria>> {

    private val file = File(context.getExternalFilesDir(null)?.absolutePath!!, "DataCsv")
    private lateinit var pyshicalFile: File;

    override fun exportData(data: List<Categoria>) {
        if(!file.exists()){
            file.mkdirs()
        }

        data.forEach { cat ->
            pyshicalFile = File(file, cat.nombre+".csv")
            BufferedWriter(FileWriter(pyshicalFile)).use { bw ->
                bw.write("nombre;prioridad;TODOValores;TODOSublistas"+"\n")
                bw.append("${cat.nombre};${cat.prioridad};${pasarACSVTODOValores(cat.notasValorTODO)};${pasarACSVTODOSublista(cat.notasSublistaTODO)}")
            }
        }
    }

    private fun pasarACSVTODOSublista(notasValorTODO: List<IValorTODO<ListadoTODO>>): String {
        return notasValorTODO.map { todo ->
            "${todo.fechaCreacion}*${todo.isChecked}*${pasarACSVTODO(todo.valor)}"
        }.joinToString("|")
    }

    private fun pasarACSVTODO(valor: ListadoTODO): String {
        return valor.listadoTODO.map { todo ->
            "${todo.fechaCreacion}º${todo.isChecked}"
        }.joinToString("¿")
    }

    private fun pasarACSVTODOValores(notasValorTODO: List<IValorTODO<String>>): String {
        return notasValorTODO.map { todo ->
            "${todo.fechaCreacion}*${todo.isChecked}*${todo.valor}*${todo.tipoTODO.texto}"
        }.joinToString("|")
    }

    private fun cargarCategoria(br: BufferedReader): Categoria {
        val apartados = br.readLines().drop(1).map { it.split(";") }[0] //Solo hay una linea por fichero!
        val nombre = apartados[0]
        val prioridad = Integer.parseInt(apartados[1])
        val listaValores = cargarDeCSVTODOValores(apartados[2])
        val listaSublistas = cargarDeCSVTODOSublistas(apartados[3])

        return Categoria(
            nombre, prioridad, listaValores, listaSublistas
        )
    }

    private fun cargarDeCSVTODOSublistas(todos: String): List<IValorTODO<ListadoTODO>>{
        return todos.split("|")
            .map { it.split("*") }
            .map { todo ->
                SublistaTODO(
                    fechaCreacion = LocalDateTime.parse(todo[0]),
                    isChecked = todo[1].equals("true"),
                    valor = cargarDeCsvTODO(todo[2])
                )
            }
    }

    private fun cargarDeCsvTODO(todos: String): ListadoTODO {
        return ListadoTODO(
            todos.split("¿")
                .map { it.split("º") }
                .map { todo ->
                    TODO(
                        fechaCreacion = LocalDateTime.parse(todo[0]),
                        isChecked = todo[1].equals("true")
                    )
                }
        )
    }

    private fun cargarDeCSVTODOValores(todos: String): List<IValorTODO<String>> {
        var tipoTODO = ""
        return todos.split("|")
            .map { it.split("*") }
            .map { todo ->
                tipoTODO = todo[3]
                when(tipoTODO){
                    TipoTODO.TEXTO.texto -> {
                        TextoTODO(
                            fechaCreacion = LocalDateTime.parse(todo[0]),
                            isChecked = todo[1].equals("true"),
                            valor = todo[2]
                        )
                    }
                    TipoTODO.IMAGEN.texto -> {
                        ImagenTODO(
                            fechaCreacion = LocalDateTime.parse(todo[0]),
                            isChecked = todo[1].equals("true"),
                            valor = todo[2]
                        )
                    }
                    else -> {
                        AudioTODO(
                            fechaCreacion = LocalDateTime.parse(todo[0]),
                            isChecked = todo[1].equals("true"),
                            valor = todo[2]
                        )
                    }
                }
            }
    }

    override fun importData(): List<Categoria> {
        val categorias: MutableList<Categoria> = mutableListOf()
        if (!file.exists()){
            return categorias
        }
        val archivos = listarArchivosEnCarpeta()
        archivos.forEach { archivo ->
            try {
                pyshicalFile = File(file, archivo)
                BufferedReader(FileReader(pyshicalFile)).use { br ->
                    categorias.add(
                        cargarCategoria(br)
                    )
                }
            }catch (e: Exception){
                return categorias
            }
        }

        return categorias
    }

    fun listarArchivosEnCarpeta(): List<String> {
        val archivos: Array<out File>? = file.listFiles()

        val nombresArchivos = mutableListOf<String>()

        archivos?.forEach { archivo ->
            nombresArchivos.add(archivo.name)
        }

        return nombresArchivos
    }

}