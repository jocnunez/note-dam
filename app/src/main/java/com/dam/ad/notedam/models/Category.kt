package com.dam.ad.notedam.models

import android.os.Build
import androidx.annotation.RequiresApi
import java.util.UUID

@RequiresApi(Build.VERSION_CODES.O)
data class Category(
    val uuid: UUID = UUID.randomUUID(),

    val title: String,

    val description: String,

    val priority: UInt,

    val notes: Iterable<Note<*>>
)
