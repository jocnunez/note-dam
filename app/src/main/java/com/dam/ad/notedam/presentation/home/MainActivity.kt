package com.dam.ad.notedam.presentation.home

import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.core.domain.models.Preferences
import com.dam.ad.notedam.data.storage.CategoriaStorage
import com.dam.ad.notedam.databinding.ActivityMainBinding
import com.dam.ad.notedam.presentation.viewModel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


val Context.dataStore by preferencesDataStore(name = Preferences.APP_CONF_MODE.clave)

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    lateinit var storage: CategoriaStorage
    lateinit var sharedViewModel: SharedViewModel
    private val firstRunKey = booleanPreferencesKey(Preferences.FIRST_RUN.clave)
    private val key_data_source = stringPreferencesKey(Preferences.APP_CONF_MODE.clave)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        storage = CategoriaStorage()

        setContentView(binding.root)
        initNavMenu()
        sharedViewModel = ViewModelProvider(this)[SharedViewModel::class.java]

        // Configuración del data source
        lifecycleScope.launch {
            if (isFirstRun()) {
                showInitialConfigDialog()
            } else {
                setData()
            }
        }
    }

    /**
     * Configura los datos en función de la configuración de origen de datos almacenada en el Almacenamiento de Datos.
     * Dependiendo del modo de configuración, importa los datos correspondientes y actualiza el ViewModel compartido.
     * Esta función es suspendida ya que accede al Almacenamiento de Datos.
     * @autor Angel Maroto Chivite
     */
    private suspend fun setData() {
        val dataStoreSourceConfig = dataStore.data
            .map { preferences ->
                preferences[key_data_source] ?: "No data"
            }
            .firstOrNull() ?: "No data"

        when (dataStoreSourceConfig) {
            Preferences.CSV.clave -> {
                sharedViewModel.clearSharedData()
                sharedViewModel.submitCategoriesLiveData(storage.importarCSV(this))
                sharedViewModel.setActualDataSource(Preferences.CSV)
            }

            Preferences.JSON.clave -> {
                sharedViewModel.clearSharedData()
                sharedViewModel.submitCategoriesLiveData(storage.importarJson(this, Preferences.JSON_FILE.clave))
                sharedViewModel.setActualDataSource(Preferences.JSON)
            }

            Preferences.XML.clave -> {
                sharedViewModel.clearSharedData()
                sharedViewModel.submitCategoriesLiveData(storage.importDataXML(this))
                sharedViewModel.setActualDataSource(Preferences.XML)
            }

            else -> {
                Log.e(ContentValues.TAG, "Modo de configuración no válido")
            }
        }
    }

    /**
     * Muestra un cuadro de diálogo para que el usuario elija el modo de configuración de datos inicial.
     * Después de la elección, la configuración se almacena en el Almacenamiento de Datos y se marca que no es la primera ejecución.
     * @autor Angel Maroto Chivite
     */
    private fun initNavMenu() {
        val navHost =
            supportFragmentManager.findFragmentById(com.dam.ad.notedam.R.id.fragmentContainerView) as NavHostFragment
        navController = navHost.navController
        binding.bottomNavView.setupWithNavController(navController)
    }

    private fun showInitialConfigDialog() {
        val dataSourceOptions = arrayOf(
            Preferences.CSV.clave,
            Preferences.JSON.clave,
            Preferences.XML.clave
        )

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Elige un modo de configuración de datos inicial (después puedes cambiarlo en ajustes)")
        builder.setItems(dataSourceOptions) { _, optionDataSource ->
            val selectedSourceType = dataSourceOptions[optionDataSource]

            lifecycleScope.launch {
                // Guardar la configuración elegida por el usuario
                setConfValue(selectedSourceType)

                // Marcamos que no es la primera ejecución
                setFirstRunPreference(false)

                Toast.makeText(this@MainActivity, "Configuración guardada a $selectedSourceType", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        builder.show()
    }

    /**
     * Establece el valor de configuración (mode) en el Almacenamiento de Datos utilizando DataStore.
     * @autor Angel Maroto Chivite
     * @param mode Modo de configuración que se desea establecer.
     * Este valor se almacenará en el Almacenamiento de Datos para su recuperación posterior.
     */
    private suspend fun setConfValue(mode: String) {
        val keyDataSource = stringPreferencesKey(Preferences.APP_CONF_MODE.clave)

        dataStore.edit { preferences ->
            preferences[keyDataSource] = mode
        }
    }

    /**
     * Establece la preferencia de "primera ejecución" en el Almacenamiento de Datos utilizando DataStore.
     * @autor Angel Maroto Chivite
     * @param isFirstRun Booleano que indica si la aplicación se está ejecutando por primera vez.
     * Este valor se almacenará en el Almacenamiento de Datos para su recuperación posterior.
     */
    private suspend fun setFirstRunPreference(isFirstRun: Boolean) {
        dataStore.edit { preferences ->
            preferences[firstRunKey] = isFirstRun
        }
    }

    /**
     * Comprueba si la aplicación se está ejecutando por primera vez según la preferencia almacenada en el Almacenamiento de Datos.
     * @autor Angel Maroto Chivite
     * @return `true` si es la primera ejecución, `false` si la aplicación ya se ha ejecutado anteriormente.
     */
    private suspend fun isFirstRun(): Boolean {
        return dataStore.data.map { preferences ->
            preferences[firstRunKey] ?: true
        }.firstOrNull() ?: true
    }

    /**
     * Exporta los datos de las categorías en función de la fuente de datos elegida en la configuración de la app
     * @autor Angel Maroto Chivite
     */
    fun exportBySourceData() {
        when (sharedViewModel.getActualDataSource()?.clave) {
            Preferences.CSV.clave -> {
                storage.exportarCSV(this, sharedViewModel.getCategories().toMutableList())
            }

            Preferences.JSON.clave -> {
                storage.exportarJson(this, sharedViewModel.getCategories().toMutableList())
            }

            Preferences.XML.clave -> {
                storage.exportDataXML(this, sharedViewModel.getCategories())
            }
        }
    }
}
