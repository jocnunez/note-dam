package com.dam.ad.notedam.models

import android.os.Build
import androidx.annotation.RequiresApi
import com.dam.ad.notedam.controller.categories.ControllerCategories
import com.dam.ad.notedam.controller.categories.IControllerCategories
import com.dam.ad.notedam.repositories.categories.RepositoryCategoriesMemory
import com.dam.ad.notedam.services.storage.categories.CategoryStorageCsv

@RequiresApi(Build.VERSION_CODES.O)
data class State(
    val categoryController: IControllerCategories = ControllerCategories(RepositoryCategoriesMemory(), CategoryStorageCsv(),""),
    var format: Format,
    var noteSelected: Note<*>? = null
) {
}