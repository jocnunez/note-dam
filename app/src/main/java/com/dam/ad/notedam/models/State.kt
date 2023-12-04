package com.dam.ad.notedam.models

import android.os.Build
import androidx.annotation.RequiresApi
import com.dam.ad.notedam.controller.categories.ControllerCategories
import com.dam.ad.notedam.controller.categories.IControllerCategories
import com.dam.ad.notedam.repositories.categories.RepositoryCategoriesMemory
import com.dam.ad.notedam.services.storage.categories.CategoryStorageCsv
import com.dam.ad.notedam.services.storage.categories.CategoryStorageJson
import com.dam.ad.notedam.services.storage.categories.CategoryStorageService
import com.dam.ad.notedam.services.storage.categories.CategoryStorageXml

@RequiresApi(Build.VERSION_CODES.O)
data class State(
    val categoryController: IControllerCategories,
    var format: Format
) {
}