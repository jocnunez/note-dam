package com.dam.ad.notedam.Config

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.dam.ad.notedam.Activities.MainActivity
import com.dam.ad.notedam.Enums.FileType

object ConfigFileType {
    //val preferencesName = (R.string.prefrences_name).toString()
    val preferencesName = "mi_aplciacion_prefs"

    fun loadFileType (mainActivity: MainActivity) : FileType {
        val sharedPreferences : SharedPreferences = mainActivity.getSharedPreferences(preferencesName,
            Context.MODE_PRIVATE)

        val res = sharedPreferences.getString("fileType", "CSV")

        return FileType.valueOf(res!!)
    }

    fun saveStorageFile (mainActivity: MainActivity, fileType: FileType) {
        try {
            val prefs: SharedPreferences = mainActivity.getSharedPreferences(preferencesName,
                Context.MODE_PRIVATE)
            val edit: SharedPreferences.Editor = prefs.edit()
            Log.v("Preferences", " New FileType : $fileType")
            edit.putString("fileType", fileType.toString())
            edit.apply()
        } catch (e: Exception) {
            // Manejar cualquier excepción que pueda ocurrir al guardar las preferencias
            e.printStackTrace()
        }
    }
}