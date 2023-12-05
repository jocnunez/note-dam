package com.dam.ad.notedam.presentation.settings

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.lifecycleScope
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import com.core.domain.models.Preferences
import com.dam.ad.notedam.R
import com.dam.ad.notedam.presentation.home.MainActivity
import com.dam.ad.notedam.presentation.home.dataStore
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SettingsFragment : PreferenceFragmentCompat() {
    private lateinit var mActivity: MainActivity
    private val key_data_source = stringPreferencesKey(Preferences.APP_CONF_MODE.clave)

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        mActivity = requireActivity() as MainActivity
        setUpDataAndConfig()
    }

    /**
     * Establecemos el modo de configuración al DataStore
     * @author Angel Maroto Chivite
     */
    private suspend fun setConfValue(mode: String) {
        mActivity.dataStore.edit { preferences ->
            preferences[key_data_source] = mode
        }
    }

    /**
     * Gestiona los cambios en las preferencias de la aplicación, mediante PreferenceScreen siendo este un XML
     * @author Angel Maroto Chivite
     */
    private fun setUpDataAndConfig() {
        findPreference<ListPreference>("data_source")?.setOnPreferenceChangeListener { _, newValue ->
            when (newValue) {
                Preferences.CSV.clave -> {
                    lifecycleScope.launch {
                        setConfValue(Preferences.CSV.clave)
                        getDataFromStorage()
                        Toast.makeText(context, "Cambio de modo a CSV", Toast.LENGTH_SHORT).show()
                    }
                    true
                }

                Preferences.JSON.clave -> {
                    lifecycleScope.launch {
                        setConfValue(Preferences.JSON.clave)
                        getDataFromStorage()
                        Toast.makeText(context, "Cambio de modo a JSON", Toast.LENGTH_SHORT).show()
                    }
                    true
                }

                Preferences.XML.clave -> {
                    lifecycleScope.launch {
                        setConfValue(Preferences.XML.clave)
                        getDataFromStorage()
                        Toast.makeText(context, "Cambio de modo a XML", Toast.LENGTH_SHORT).show()
                    }
                    true
                }

                else -> false
            }
        }
    }

    /**
     * Recogemos los datos en función de la configuración y los cargamos al SharedViewModel,
     * únicamente hará esto cuando la configuración elegida sea distinta a la que tengamos en el SharedViewModel
     * @author Angel Maroto Chivite
     */
    private suspend fun getDataFromStorage() {
        val dataStoreSourceConfig = mActivity.dataStore.data
            .map { preferences ->
                preferences[key_data_source] ?: "No data"
            }
            .firstOrNull() ?: "No data"

        if (mActivity.sharedViewModel.getActualDataSource()?.clave != dataStoreSourceConfig) {
            when (dataStoreSourceConfig) {
                Preferences.CSV.clave -> {
                    mActivity.sharedViewModel.clearSharedData()
                    mActivity.sharedViewModel.submitCategoriesLiveData(mActivity.storage.importarCSV(requireContext()))
                    mActivity.sharedViewModel.setActualDataSource(Preferences.CSV)
                }

                Preferences.JSON.clave -> {
                    mActivity.sharedViewModel.clearSharedData()
                    mActivity.sharedViewModel.submitCategoriesLiveData(
                        mActivity.storage.importarJson(
                            requireContext(),
                            Preferences.JSON_FILE.clave
                        )
                    )
                    mActivity.sharedViewModel.setActualDataSource(Preferences.JSON)
                }

                Preferences.XML.clave -> {
                    mActivity.sharedViewModel.clearSharedData()
                    mActivity.sharedViewModel.submitCategoriesLiveData(mActivity.storage.importDataXML(requireContext()))
                    mActivity.sharedViewModel.setActualDataSource(Preferences.XML)
                }

                else -> {
                    Log.e(ContentValues.TAG, "Modo de configuración no válido")
                }
            }
        }
    }
}
