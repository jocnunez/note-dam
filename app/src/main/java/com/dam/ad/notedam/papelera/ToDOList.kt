package com.dam.ad.notedam.papelera

import java.util.*

class ToDOList(var nombreList:String="") {
    var uuid: UUID = UUID.randomUUID()
    var prioridad:Int = 99
    var listaToDo:MutableList<String> = mutableListOf()
}