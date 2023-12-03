package com.dam.ad.notedam.models.xml_converts

import com.dam.ad.notedam.models.SublistItem
import com.dam.ad.notedam.models.dto.NoteDto
import org.simpleframework.xml.convert.Converter
import org.simpleframework.xml.stream.InputNode
import org.simpleframework.xml.stream.OutputNode

class NoteDtoConverter : Converter<NoteDto> {
    override fun read(node: InputNode): NoteDto {
        // Implementa la lógica de lectura para convertir un nodo XML en un objeto NoteDto
        val uuid = node.getNext("uuid").value
        val type = node.getNext("type").value
        val fechaCreate = node.getNext("fechaCreate").value
        val check = node.getNext("check").value
        val text = node.getNext("text")?.value
        val image = node.getNext("image")?.value
        val audio = node.getNext("audio")?.value


        // ==========================================================================================
        val subList = mutableListOf<SublistItem>()

        val subListNode = node.getNext("subList")
        subListNode?.let { listNode ->
            while (true) {
                val childNode = listNode.next()

                if (childNode == null || childNode.name != "subListItem") {
                    break
                }

                val check = childNode.getNext("check").value.toBoolean()
                val valor = childNode.getNext("valor").value
                subList.add(SublistItem(check, valor))
            }
        }
        // Lee y asigna otros campos de la misma manera
        // ...

        return NoteDto(uuid,type,fechaCreate,check.toBoolean(),text,image,audio,subList)
    }

    override fun write(node: OutputNode, value: NoteDto) {
        // Implementa la lógica de escritura para convertir un objeto NoteDto a nodos XML
        node.child("uuid").value = value.uuid
        node.child("type").value = value.type
        // Escribe otros campos de la misma manera
        // ...
    }
}