package com.dam.ad.notedam.utils.validators

import com.dam.ad.notedam.models.errors.FileError
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import java.io.File

fun File.validate(fileAction: FileAction): Result<File, FileError>{
    when(fileAction){
        FileAction.READ -> {
            if (!this.exists()) return Err(FileError.FileNotFound(this.name))
            if (!this.canRead()) return Err(FileError.FileNotReadable(this.name))
        }
        FileAction.WRITE -> {
            if (this.exists() && !this.canWrite()) return Err(FileError.FileNotWritable(this.name))
        }
    }
    return Ok(this)
}

enum class FileAction{
    READ,
    WRITE
}