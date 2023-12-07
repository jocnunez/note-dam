package com.dam.ad.notedam.Models

import java.util.UUID

class ToDOList(var nombreList:String="") {
    var uuid:UUID = UUID.randomUUID()
    var prioridad:Int = 99
    var listaToDo:MutableList<String> = mutableListOf()
}