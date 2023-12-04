package com.dam.ad.notedam.models

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import com.dam.ad.notedam.utils.mappers.fromCsvRowToSublistItem
import java.io.File
import java.time.LocalDateTime
import java.util.UUID
@RequiresApi(Build.VERSION_CODES.O)
sealed class Note<T>(open val uuid: UUID, val value: T, open val fechaCreate: LocalDateTime, open val check: Boolean) {
    abstract fun toCsvRow(separator: Char, tail: Char? = null): String


    data class Text(
        override val uuid: UUID = UUID.randomUUID(),
        val text: String,
        override val check: Boolean,
        override val fechaCreate: LocalDateTime,
    ): Note<String>(uuid, text, fechaCreate, check){
        override fun toCsvRow(separator: Char, tail: Char?): String {
            return "text$separator$uuid$separator$text$separator$check$separator$fechaCreate${tail ?: ""}"
        }
        companion object{
            fun fromCsvRowToNote(values: List<String>): Text {
                return Text(
                    uuid = UUID.fromString(values[1]),
                    text = values[2],
                    check = values[3].toBoolean(),
                    fechaCreate = LocalDateTime.parse(values[4])
                )
            }
        }
    }


    data class Image(

        override val uuid: UUID = UUID.randomUUID(),
        val image: String,
        override val check: Boolean,
        override val fechaCreate: LocalDateTime,
    ): Note<String>(uuid, image, fechaCreate, check) {
        override fun toCsvRow(separator: Char, tail: Char?): String {
            return "image$separator$uuid$separator$image$separator$check$separator$fechaCreate${tail ?: ""}"
        }

        companion object{
            fun fromCsvRowToNote(values: List<String>): Image {
                return Image(
                    uuid = UUID.fromString(values[1]),
                    image = values[2],
                    check = values[3].toBoolean(),
                    fechaCreate = LocalDateTime.parse(values[4])
                )
            }
        }
    }

    data class Audio(

        override val uuid: UUID = UUID.randomUUID(),
        val audio: File,
        override val check: Boolean,
        override val fechaCreate: LocalDateTime,
    ): Note<File>(uuid, audio, fechaCreate, check) {
        override fun toCsvRow(separator: Char, tail: Char?): String {
            return "audio$separator$uuid$separator${audio.path}$separator$check$separator$fechaCreate${tail ?: ""}"
        }

        companion object{
            fun fromCsvRowToNote(values: List<String>): Audio {
                return Audio(
                    uuid = UUID.fromString(values[1]),
                    audio = File(values[2]),
                    check = values[3].toBoolean(),
                    fechaCreate = LocalDateTime.parse(values[4])
                )
            }
        }
    }

    data class Sublist(
        override val uuid: UUID = UUID.randomUUID(),
        val sublist: MutableMap<String, SublistItem>,
        override val check: Boolean,
        override val fechaCreate: LocalDateTime,
    ): Note<MutableMap<String, SublistItem>>(uuid, sublist, fechaCreate, check) {
        override fun toCsvRow(separator: Char, tail: Char?): String {
            return "sublist$separator$uuid$separator${
                sublist.values.joinToString(separator = "::", transform = { it.toCsvRow('¬')})
            }$separator$check$separator$fechaCreate${tail ?: ""}"
        }

        companion object{
            fun fromCsvRowToNote(values: List<String>, separator: String): Sublist {
                val sublist = values[2]
                return Sublist(
                    uuid = UUID.fromString(values[1]),
                    sublist = if (sublist.isEmpty()) mutableMapOf() else sublist.split("::").map { it.fromCsvRowToSublistItem('¬') }.associateBy { it.subListValue }.toMutableMap(),
                    check = values[3].toBoolean(),
                    fechaCreate = LocalDateTime.parse(values[4])
                )
            }
        }

    }
}
