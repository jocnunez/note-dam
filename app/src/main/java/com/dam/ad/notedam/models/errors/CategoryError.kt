package com.dam.ad.notedam.models.errors

sealed class CategoryError(val message: String){
    class CategoryNotFound: CategoryError("Error category not found")
    class CategoryNotSaved: CategoryError("Error category not saved")
    class CategoryNotDeleted: CategoryError("Error category not deleted")

    class ExportError(type: String): CategoryError("Error exporting $type category")
    class ImportError(type: String): CategoryError("Error importing $type category")

    class ValidationError(type: String): CategoryError("Error while validate category: $type")
}