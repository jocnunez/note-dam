package com.dam.ad.notedam.Models.JsonAdapters

import com.dam.ad.notedam.Models.DTO.DTOToDoList
import com.google.gson.JsonArray
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

class DTOToDOListAdapterGson: JsonSerializer<DTOToDoList>, JsonDeserializer<DTOToDoList> {
    override fun serialize(src: DTOToDoList, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val jsonObject = JsonObject()
        jsonObject.addProperty("ToDoListName", src.name)
        jsonObject.addProperty("ToDoListId", src.uuid)
        jsonObject.addProperty("prioridad", src.prioridad)
        src.listaToDo?.let { listaToDo ->
            val lista = JsonArray()
            listaToDo.forEach { Item ->
                val ItemObject = JsonObject()
                ItemObject.addProperty("nota", Item)
                lista.add(Item)
            }
            jsonObject.add("listaToDo", lista)
        }

        return jsonObject
    }

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): DTOToDoList {
        val jsonObject = json.asJsonObject
        val name = jsonObject["ToDoListName"].asString
        val uuid = jsonObject["ToDoListId"].asString
        val prioridad = jsonObject["prioridad"].asString
        var salida: MutableList<String> = mutableListOf()
        val listaToDo = if (jsonObject.has("listaToDo")) {
            val lista = jsonObject.getAsJsonArray("listaToDo")
            lista.forEach {
              salida.add(it.asString)
            }
        } else null
        return DTOToDoList(name,uuid, prioridad, salida)
    }
}
