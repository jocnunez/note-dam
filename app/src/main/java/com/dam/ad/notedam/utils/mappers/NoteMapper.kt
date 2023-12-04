package com.dam.ad.notedam.utils.mappers

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import com.dam.ad.notedam.models.Note
import com.dam.ad.notedam.models.dto.NoteDto
import java.io.File
import java.time.LocalDateTime
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
fun NoteDto.toNote(): Note<*> {
    return when (type) {
        "text" -> Note.Text(
            uuid = UUID.fromString(uuid),
            text = text!!,
            check = check,
            fechaCreate = LocalDateTime.parse(fechaCreate)
        )
        "image" -> Note.Image(
            uuid = UUID.fromString(uuid),
            image = image!!,
            check = check,
            fechaCreate = LocalDateTime.parse(fechaCreate)
        )
        "audio" -> Note.Audio(
            uuid = UUID.fromString(uuid),
            audio = File(audio!!),
            check = check,
            fechaCreate = LocalDateTime.parse(fechaCreate)
        )
        "sublist" -> Note.Sublist(
            uuid = UUID.fromString(uuid),
            sublist = subList ?: listOf(),
            check = check,
            fechaCreate = LocalDateTime.parse(fechaCreate)
        )
        else -> throw IllegalArgumentException("Unknown type")
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun Note<*>.toNoteDto(): NoteDto {
    return when (this) {
        is Note.Text -> NoteDto(
            type = "text",
            uuid = uuid.toString(),
            text = value,
            check = check,
            fechaCreate = fechaCreate.toString()
        )
        is Note.Image -> NoteDto(
            type = "image",
            uuid = uuid.toString(),
            image = value.toString(),
            check = check,
            fechaCreate = fechaCreate.toString()
        )
        is Note.Audio -> NoteDto(
            type = "audio",
            uuid = uuid.toString(),
            audio = value.toString(),
            check = check,
            fechaCreate = fechaCreate.toString()
        )
        is Note.Sublist -> NoteDto(
            type = "sublist",
            uuid = uuid.toString(),
            subList = value.toList(),
            check = check,
            fechaCreate = fechaCreate.toString()
        )
    }
}