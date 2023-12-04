package com.dam.ad.notedam.Config

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.dam.ad.notedam.Activities.MainActivity
import com.dam.ad.notedam.Enums.StorageType

object ConfigStorageType {

    //val preferencesName = (R.string.prefrences_name).toString()
    val preferencesName = "mi_aplciacion_prefs"

    fun loadStorageType (mainActivity: MainActivity) :StorageType {
        val sharedPreferences : SharedPreferences = mainActivity.getSharedPreferences(preferencesName,Context.MODE_PRIVATE)

        val res = sharedPreferences.getString("storageType", "Local")

        return StorageType.valueOf(res!!)
    }

    fun saveStorageType (mainActivity: MainActivity, storageType: StorageType) {
        try {
            val prefs: SharedPreferences = mainActivity.getSharedPreferences(preferencesName,Context.MODE_PRIVATE)
            val edit: SharedPreferences.Editor = prefs.edit()
            Log.v("Preferences", " New StorageType : $storageType")
            edit.putString("storageType", storageType.toString())
            edit.apply()

        } catch (e: Exception) {
            // Manejar cualquier excepción que pueda ocurrir al guardar las preferencias
            e.printStackTrace()
        }
    }

}