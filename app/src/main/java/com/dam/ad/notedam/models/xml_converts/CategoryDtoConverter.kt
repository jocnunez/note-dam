package com.dam.ad.notedam.models.xml_converts

import com.dam.ad.notedam.models.dto.CategoryDto
import com.dam.ad.notedam.models.dto.NoteDto
import org.simpleframework.xml.convert.Converter
import org.simpleframework.xml.stream.InputNode
import org.simpleframework.xml.stream.OutputNode

class CategoryDtoConverter : Converter<CategoryDto> {
    override fun read(node: InputNode): CategoryDto {
        // Implementa la lógica de lectura para convertir un nodo XML en un objeto CategoryDto
        val uuid = node.getNext("uuid").value
        val title = node.getNext("title").value
        // Lee y asigna otros campos de la misma manera
        // ...

        // Para las notas, puedes leer la lista de nodos "note" y convertirlos a objetos NoteDto
        val notes = mutableListOf<NoteDto>()
        val notesNode = node.getNext("notes")
        while (notesNode != null) {
            notes.add(NoteDtoConverter().read(notesNode))
        }

        return CategoryDto(uuid, title /*, otros campos*/, notes)
    }

    override fun write(node: OutputNode, value: CategoryDto) {
        // Implementa la lógica de escritura para convertir un objeto CategoryDto a nodos XML
        node.child("uuid").value = value.uuid
        node.child("title").value = value.title
        // Escribe otros campos de la misma manera
        // ...

        // Para las notas, escribe cada objeto NoteDto como un nodo "note"
        value.notes.forEach { note ->
            val noteNode = node.child("notes")
            NoteDtoConverter().write(noteNode, note)
        }
    }
}