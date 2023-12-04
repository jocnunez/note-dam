package com.dam.ad.notedam.utils

import android.net.Uri
import com.dam.ad.notedam.models.Category
import com.dam.ad.notedam.models.SublistItem
import java.io.File
import java.time.LocalDateTime
import java.util.UUID

class UtilsTest {
    companion object{
        val id = UUID.randomUUID()
      val categories = listOf( Category(id, "Paisajes", "Sitios que visitar", 0u, listOf(
            com.dam.ad.notedam.models.Note.Text(UUID.randomUUID(), "Esto es una tarea de texto", false, LocalDateTime.now()),
            com.dam.ad.notedam.models.Note.Image(UUID.randomUUID(), "https://img.freepik.com/fotos-premium/hermosos-paisajes-paisajes-naturales-hacen-que-personas-relajen-disfruten-fondo-pantalla_917506-214429.jpg", false, LocalDateTime.now()),
            com.dam.ad.notedam.models.Note.Audio(UUID.randomUUID(), File("url de la nota de audio"), false, LocalDateTime.now()),
            com.dam.ad.notedam.models.Note.Sublist(UUID.randomUUID(), listOf(SublistItem(false, "Tarea 1"), SublistItem(false, "Tarea 2")), true, LocalDateTime.now()),
            com.dam.ad.notedam.models.Note.Text(UUID.randomUUID(), "Esto es otra tarea de texto", false, LocalDateTime.now()),
            com.dam.ad.notedam.models.Note.Image(UUID.randomUUID(), "https://www.hofmann.es/blog/wp-content/uploads/2021/09/HF_3_Paisajes_WEB-23.jpg", false, LocalDateTime.now()),
            com.dam.ad.notedam.models.Note.Sublist(UUID.randomUUID(), listOf(), false, LocalDateTime.now()),
            com.dam.ad.notedam.models.Note.Text(UUID.randomUUID(), "Esto es la última tarea de texto", false, LocalDateTime.now()),
            com.dam.ad.notedam.models.Note.Image(UUID.randomUUID(), "https://images.hola.com/imagenes/viajes/20200401164667/paisajes-de-espana-a-vista-de-pajaro-drone-aereas/0-806-353/paisajes-a-vista-de-pajaro-t.jpg", false, LocalDateTime.now())))
    )
    }
}