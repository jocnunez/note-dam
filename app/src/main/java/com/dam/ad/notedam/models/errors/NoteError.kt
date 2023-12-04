package com.dam.ad.notedam.models.errors

sealed class NoteError(val message: String){
    class NoteNotFound: NoteError("Error note not found")
    class NoteNotSaved: NoteError("Error note not saved")
    class NoteNotDeleted: NoteError("Error note not deleted")

    class ValidationError(type: String): NoteError("Error while validate note: $type")
}