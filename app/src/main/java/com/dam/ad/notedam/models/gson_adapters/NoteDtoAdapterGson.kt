package com.dam.ad.notedam.models.gson_adapters

import com.dam.ad.notedam.models.SublistItem
import com.dam.ad.notedam.models.dto.NoteDto
import com.google.gson.*
import java.lang.reflect.Type

class NoteDtoAdapterGson : JsonSerializer<NoteDto>, JsonDeserializer<NoteDto> {
    override fun serialize(src: NoteDto, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val jsonObject = JsonObject()
        jsonObject.addProperty("uuid", src.uuid)
        jsonObject.addProperty("type", src.type)
        jsonObject.addProperty("fechaCreate", src.fechaCreate)
        jsonObject.addProperty("check", src.check)

        src.text?.let { jsonObject.addProperty("text", it) }
        src.image?.let { jsonObject.addProperty("image", it) }
        src.audio?.let { jsonObject.addProperty("audio", it) }
        src.subList?.let { sublist ->
            val sublistArray = JsonArray()
            sublist.forEach { sublistItem ->
                val sublistItemObject = JsonObject()
                sublistItemObject.addProperty("check", sublistItem.check)
                sublistItemObject.addProperty("valor", sublistItem.valor)
                sublistArray.add(sublistItemObject)
            }
            jsonObject.add("subList", sublistArray)
        }

        return jsonObject
    }

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): NoteDto {
        val jsonObject = json.asJsonObject
        val uuid = jsonObject["uuid"].asString
        val type = jsonObject["type"].asString
        val fechaCreate = jsonObject["fechaCreate"].asString
        val check = jsonObject["check"].asBoolean

        val text = jsonObject["text"]?.asString
        val image = jsonObject["image"]?.asString
        val audio = jsonObject["audio"]?.asString
        val subList = if (jsonObject.has("subList")) {
            val sublistArray = jsonObject.getAsJsonArray("subList")
            sublistArray.map { sublistItem ->
                val sublistItemObject = sublistItem.asJsonObject
                SublistItem(
                    sublistItemObject["check"].asBoolean,
                    sublistItemObject["valor"].asString
                )
            }
        } else null

        return NoteDto(uuid, type, fechaCreate, check, text, image, audio, subList)
    }
}
