package com.dam.ad.notedam.services.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.dam.ad.notedam.models.Category

object StorageManager {
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")
    var lastStorageService: IStorageService? = null
    var storageService: IStorageService? = null
    val categories = mutableListOf<Category>()
}