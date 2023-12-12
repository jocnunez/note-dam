package com.dam.ad.notedam.presentation.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.dam.ad.notedam.R
import com.dam.ad.notedam.databinding.ActivityMainBinding
import com.dam.ad.notedam.models.Category
import com.dam.ad.notedam.services.storage.CsvStorageService
import com.dam.ad.notedam.services.storage.JsonStorageService
import com.dam.ad.notedam.services.storage.StorageManager
import com.dam.ad.notedam.services.storage.StorageManager.dataStore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        lifecycleScope.launch {
            firstTimeInfo()
        }

        // Set storage service for reading
        var lastStorageService: Int? = null
        lifecycleScope.launch {
            lastStorageService = readSelectedStorageService()
        }
        StorageManager.lastStorageService = when (lastStorageService) {
            0 -> CsvStorageService()
            1 -> JsonStorageService()
            else -> null
        }

        // Set storage service for writing
        val selectedStorageService = selectStorageService()
        println("selectedStorageService: $selectedStorageService")
        StorageManager.storageService = when (selectedStorageService) {
            0 -> CsvStorageService()
            1 -> JsonStorageService()
            else -> null
        }
        lifecycleScope.launch { writeSelectedStorageService(selectedStorageService) }

        // Read categories
        readCategories().forEach { StorageManager.categories.add(it) }

        setContentView(binding.root)
        initNavMenu()
    }

    override fun onPause() {
        super.onPause()
        StorageManager.storageService?.write(this, StorageManager.categories)
        println("onPause")
    }

    private fun initNavMenu() {
        val navHost = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHost.navController
        binding.bottomNavView.setupWithNavController(navController)
    }

    private suspend fun firstTimeInfo() {
        val firstTime = booleanPreferencesKey("first_time")
        dataStore.data.map { it[firstTime] ?: true }.collect { isFirstTime ->
            if (isFirstTime) {
                showWelcomeDialog()
                this.dataStore.edit { it[firstTime] = false }
            }
            println("firstTimeInfo: $isFirstTime")
        }
    }

    private fun showWelcomeDialog() {
        AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.welcome))
            setMessage(getString(R.string.welcome_message))
            setNeutralButton(getString(R.string.ok)) { _, _ -> }
        }.show()
    }

    private fun selectStorageService(): Int {
        var selectedStorageService = 0
        AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.storage_service))
            setMessage(getString(R.string.select_storage_service))
            setNegativeButton("CSV") { _, _ -> selectedStorageService = 0 }
            setPositiveButton("JSON") { _, _ -> selectedStorageService = 1 }
            setCancelable(false)
        }.show()

        return selectedStorageService
    }

    private suspend fun readSelectedStorageService(): Int? {
        val selectedStorageServiceKey = intPreferencesKey("selected_storage_service")
        var selectedStorageService: Int? = null
        dataStore.data.map { it[selectedStorageServiceKey] }
            .collect { selectedStorageService = it }
        println("readSelectedStorageService: $selectedStorageService")
        return selectedStorageService
    }

    private suspend fun writeSelectedStorageService(selectedStorageService: Int) {
        val selectedStorageServiceKey = intPreferencesKey("selected_storage_service")
        dataStore.edit { it[selectedStorageServiceKey] = selectedStorageService }
        println("writeSelectedStorageService: $selectedStorageService")
    }

    private fun readCategories(): List<Category> {
        val categories = StorageManager.storageService?.read(this) ?: emptyList()
        println("readCategories: $categories")
        return categories
    }
}
