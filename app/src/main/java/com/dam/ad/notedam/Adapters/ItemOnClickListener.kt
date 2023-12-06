package com.dam.ad.notedam.Adapters

import java.util.UUID

interface ItemOnClickListener<T> {
    fun onClick (uuid : UUID)
    fun onLongClickListener (uuid : UUID) : Boolean
}