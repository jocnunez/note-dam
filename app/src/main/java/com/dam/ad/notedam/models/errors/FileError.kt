package com.dam.ad.notedam.models.errors

sealed class FileError(val message: String) {
    class FileNotFound(type: String): FileError("Error file $type not found")
    class FileNotReadable(type: String): FileError("Error file $type not readable")
    class FileNotWritable(type: String): FileError("Error file $type not writable")
}