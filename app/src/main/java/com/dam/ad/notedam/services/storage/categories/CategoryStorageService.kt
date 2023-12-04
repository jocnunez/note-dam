package com.dam.ad.notedam.services.storage.categories

import com.dam.ad.notedam.models.Category
import com.dam.ad.notedam.models.errors.CategoryError
import com.dam.ad.notedam.services.storage.StorageService

interface CategoryStorageService: StorageService<Category, CategoryError>