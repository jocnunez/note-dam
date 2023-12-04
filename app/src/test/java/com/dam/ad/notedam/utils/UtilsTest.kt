package com.dam.ad.notedam.utils

import android.net.Uri
import com.dam.ad.notedam.models.Category
import com.dam.ad.notedam.models.Note
import com.dam.ad.notedam.models.SublistItem
import java.io.File
import java.time.LocalDateTime
import java.util.UUID

class UtilsTest {
    companion object{
      val categories = listOf( Category(UUID.randomUUID(), "Paisajes", "Sitios que visitar", 0u, mutableMapOf(
            UUID.fromString("9e3a85dc-92e7-11ee-b9d1-0242ac120002") to Note.Text(UUID.fromString("9e3a85dc-92e7-11ee-b9d1-0242ac120002"), "Esto es una tarea de texto", false, LocalDateTime.now()),
            UUID.fromString("f7ea82f8-92e7-11ee-b9d1-0242ac120002") to Note.Image(UUID.fromString("f7ea82f8-92e7-11ee-b9d1-0242ac120002") , "https://img.freepik.com/fotos-premium/hermosos-paisajes-paisajes-naturales-hacen-que-personas-relajen-disfruten-fondo-pantalla_917506-214429.jpg", false, LocalDateTime.now()),
            UUID.fromString("fe4ee224-92e7-11ee-b9d1-0242ac120003") to Note.Audio(UUID.fromString("fe4ee224-92e7-11ee-b9d1-0242ac120003"), File("url de la nota de audio"), false, LocalDateTime.now()),
            UUID.fromString("9e3a85dc-92e7-11ee-b9d1-0242ac120005") to Note.Sublist(UUID.fromString("9e3a85dc-92e7-11ee-b9d1-0242ac120005"), mutableMapOf("Tarea 1" to SublistItem(false, "Tarea 1"), "Tarea 2" to SublistItem(false, "Tarea 2")), true, LocalDateTime.now()),
            UUID.fromString("9e3a85dc-92e7-11ee-b9d1-0242ac120006") to Note.Text(UUID.fromString("9e3a85dc-92e7-11ee-b9d1-0242ac120006"), "Esto es otra tarea de texto", false, LocalDateTime.now()),
            UUID.fromString("9e3a85dc-92e7-11ee-b9d1-0242ac120007") to Note.Image(UUID.fromString("9e3a85dc-92e7-11ee-b9d1-0242ac120007"), "https://www.hofmann.es/blog/wp-content/uploads/2021/09/HF_3_Paisajes_WEB-23.jpg", false, LocalDateTime.now()),
            UUID.fromString("9e3a85dc-92e7-11ee-b9d1-0242ac120008") to Note.Sublist(UUID.fromString("9e3a85dc-92e7-11ee-b9d1-0242ac120008"), mutableMapOf(), false, LocalDateTime.now()),
            UUID.fromString("9e3a85dc-92e7-11ee-b9d1-0242ac120009") to Note.Text(UUID.fromString("9e3a85dc-92e7-11ee-b9d1-0242ac120009"), "Esto es la última tarea de texto", false, LocalDateTime.now()),
            UUID.fromString("9e3a85dc-92e7-11ee-b9d1-0242ac120010") to Note.Image(UUID.fromString("9e3a85dc-92e7-11ee-b9d1-0242ac120010"), "https://images.hola.com/imagenes/viajes/20200401164667/paisajes-de-espana-a-vista-de-pajaro-drone-aereas/0-806-353/paisajes-a-vista-de-pajaro-t.jpg", false, LocalDateTime.now()))),
      )
    }
}