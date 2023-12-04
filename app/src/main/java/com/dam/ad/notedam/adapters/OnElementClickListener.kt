package com.dam.ad.notedam.adapters

import com.dam.ad.notedam.models.Category

interface OnElementClickListener<T> {

    fun onClick(item: T)
    fun onLongClick(item: T)

}