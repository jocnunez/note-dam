package com.dam.ad.notedam.papelera.Mapper

import android.os.Build
import androidx.annotation.RequiresApi
import com.dam.ad.notedam.papelera.DTOToDoList
import com.dam.ad.notedam.papelera.ToDOList
import java.util.UUID


@RequiresApi(Build.VERSION_CODES.O)
fun DTOToDoList.toToDOList(): ToDOList {
   var salida = ToDOList(this.name)
    salida.uuid= UUID.fromString(this.uuid)
    salida.prioridad = this.prioridad.toInt()
    salida.listaToDo = this.listaToDo.toMutableList()
    return salida
}
@RequiresApi(Build.VERSION_CODES.O)
fun ToDOList.toDTOToDoList(): DTOToDoList {
    return DTOToDoList(this.nombreList,this.uuid.toString(),this.prioridad.toString(),this.listaToDo.toList())
}