package com.dam.ad.notedam.services.storage

import com.dam.ad.notedam.models.Category

object StorageManager {
    var lastStorageService: IStorageService? = null
    var storageService: IStorageService? = null
    val categories = mutableListOf<Category>()
}