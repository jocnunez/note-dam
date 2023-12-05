package com.dam.ad.notedam.presentation.adapter

import com.core.domain.models.Categoria
import java.util.*

interface OnCategoryClickListener {
    fun editCategory(categoria: Categoria)
    fun deleteCategory(categoriaId: UUID)
    fun editSelectedCategory(categoria: Categoria)
}
