package com.dam.ad.notedam.utils.mappers

import android.os.Build
import androidx.annotation.RequiresApi
import com.dam.ad.notedam.models.Category
import com.dam.ad.notedam.models.Note
import com.dam.ad.notedam.models.SublistItem
import com.dam.ad.notedam.models.dto.CategoryDto
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
fun Category.toCsvRow(): String {
    return "$uuid,$title,$description,$priority,${notes.values.joinToString("|") { it.toCsvRow(';') }}\n"
}

@RequiresApi(Build.VERSION_CODES.O)
fun String.fromCsvRowToCategory(): Category {
    val values = split(",")
    val notes = values[4]
    require(values.size == 5) { "Invalid CSV row" }
    return Category(
        uuid = UUID.fromString(values[0]),
        title = values[1],
        description = values[2],
        priority = values[3].toUInt(),
        notes = if (notes.isEmpty()) mutableMapOf() else notes.split("|")
            .map { it.fromCsvRowToNote(';') }.associateBy { it.uuid }.toMutableMap()
    )
}

fun String.fromCsvRowToSublistItem(separator: Char): SublistItem {
    val values = split(separator)
    require(values.size == 2) { "Invalid CSV row" }
    return SublistItem(
        check = values[0].toBoolean(),
        subListValue = values[1]
    )
}

fun String.fromCsvRowToNote(separator: Char): Note<*> {
    val values = this.split(separator)
    require(values.size == 5)
    return when(values[0].lowercase()){
        "text" -> Note.Text.fromCsvRowToNote(values)
        "image" -> Note.Image.fromCsvRowToNote(values)
        "audio" -> Note.Audio.fromCsvRowToNote(values)
        "sublist" -> Note.Sublist.fromCsvRowToNote(values, "::")
        else -> {
            throw Exception("Type not found")
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun CategoryDto.toCategory(): Category {
    return Category(
        uuid = UUID.fromString(uuid),
        title = title,
        description = description,
        priority = level.toUInt(),
        notes = notes?.map { it.toNote() }?.associateBy { it.uuid }?.toMutableMap() ?: mutableMapOf()
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun Category.toCategoryDto(): CategoryDto {
    return CategoryDto(
        uuid = uuid.toString(),
        title = title,
        description = description,
        level = priority.toInt(),
        notes = notes.values.map { it.toNoteDto() }
    )
}
