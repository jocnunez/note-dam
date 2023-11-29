package com.dam.ad.notedam.utils.validators

import com.dam.ad.notedam.models.Category
import com.dam.ad.notedam.models.errors.CategoryError
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result

fun Category.validate(): Result<Category, CategoryError>{
    return when{
        this.title.isBlank() -> Err(CategoryError.ValidationError(
            "Title can't be blank"
        ))
        this.description.isBlank() -> Err(CategoryError.ValidationError(
            "Description can't be blank"
        ))
        else -> Ok(this)
    }
}