package dev.ivanronco.primeraappaccesodatos.dto.mapper

import android.os.Build
import androidx.annotation.RequiresApi
import dev.ivanronco.primeraappaccesodatos.dto.TODO.*
import dev.ivanronco.primeraappaccesodatos.dto.categoria.CategoriaDto
import dev.ivanronco.primeraappaccesodatos.dto.categoria.ListaCategoriasDto
import dev.ivanronco.primeraappaccesodatos.models.TODO.*
import dev.ivanronco.primeraappaccesodatos.models.TipoTODO.TipoTODO
import dev.ivanronco.primeraappaccesodatos.models.categoria.Categoria
import dev.ivanronco.primeraappaccesodatos.models.categoria.ListaCategorias
import java.time.LocalDateTime

fun ListaCategorias.toDto(): ListaCategoriasDto{
    return ListaCategoriasDto(
        this.categorias.map { cat ->
            CategoriaDto(
                cat.nombre,
                cat.prioridad,
                ListadoTODOValorDto(cat.notasValorTODO
                    .map { todo ->
                        TODOValorDto(
                            todo.fechaCreacion.toString(),
                            todo.isChecked.toString(),
                            todo.valor,
                            todo.tipoTODO.texto
                        )
                    }),
                ListadoTODOSublistaDto(cat.notasSublistaTODO
                    .map { todo ->
                        TODOSublistaDto(
                            todo.fechaCreacion.toString(),
                            todo.isChecked.toString(),
                            ListadoTODODto(
                                todo.valor.listadoTODO
                                    .map { todo ->
                                        TODODto(
                                            todo.fechaCreacion.toString(),
                                            todo.isChecked.toString()
                                        )
                                    }
                            )
                        )
                    })
            )
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun ListaCategoriasDto.toCategorias(): ListaCategorias{
    return ListaCategorias(
        this.categorias.map { cat ->
            Categoria(
                cat.nombre,
                cat.prioridad,
                cat.notasValorTODO.todos
                    .map {todo ->
                         when(todo.tipoTODO){
                             TipoTODO.TEXTO.texto -> {
                                 TextoTODO(
                                     fechaCreacion = LocalDateTime.parse(todo.fechaCreacion),
                                     isChecked = todo.isChecked.equals("true"),
                                     valor = todo.valor
                                 )
                             }
                             TipoTODO.IMAGEN.texto -> {
                                 ImagenTODO(
                                     fechaCreacion = LocalDateTime.parse(todo.fechaCreacion),
                                     isChecked = todo.isChecked.equals("true"),
                                     valor = todo.valor
                                 )
                             }
                             else -> {
                                 AudioTODO(
                                     fechaCreacion = LocalDateTime.parse(todo.fechaCreacion),
                                     isChecked = todo.isChecked.equals("true"),
                                     valor = todo.valor
                                 )
                             }
                         }
                    },
                cat.notasSublistaTODO.todos
                    .map {todo ->
                        SublistaTODO(
                            fechaCreacion = LocalDateTime.parse(todo.fechaCreacion),
                            isChecked = todo.isChecked.equals("true"),
                            valor = ListadoTODO(todo.valor.todos
                                .map { t ->
                                    TODO(
                                        fechaCreacion = LocalDateTime.parse(t.fechaCreacion),
                                        isChecked = t.isChecked.equals("true")
                                    )
                                })
                        )
                    }
            )
        }
    )
}