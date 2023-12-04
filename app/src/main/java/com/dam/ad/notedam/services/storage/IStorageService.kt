package com.dam.ad.notedam.services.storage

import android.content.Context
import com.dam.ad.notedam.models.Category

interface IStorageService {
    fun write(context: Context, content: List<Category>)
    fun read(context: Context): List<Category>
}
