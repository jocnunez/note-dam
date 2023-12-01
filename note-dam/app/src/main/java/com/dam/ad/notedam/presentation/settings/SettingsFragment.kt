package com.dam.ad.notedam.presentation.settings


import android.os.Bundle
import androidx.fragment.app.Fragment

import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import com.dam.ad.notedam.R

import com.dam.ad.notedam.presentation.home.MainActivity
import com.dam.ad.notedam.presentation.models.Preferences


class SettingsFragment : PreferenceFragmentCompat() {
    private lateinit var mActivity: MainActivity

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        mActivity = requireActivity() as MainActivity


        setUpBindings()
    }

    private fun setConfValue(mode: String) {
        mActivity.sharedPref.edit().putString(Preferences.APP_CONF_MODE.clave, mode).apply()
    }

    private fun setUpBindings() {
        findPreference<ListPreference>("data_source")?.setOnPreferenceChangeListener { preference, newValue ->
            when(newValue){
                "local" -> {
                    setConfValue("local")
                    true
                }
                "remote" -> {
                    setConfValue("remote")
                    true
                }

                else -> false
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        mActivity.supportActionBar?.setDisplayHomeAsUpEnabled(false)

        mActivity.setUpConfigurations() //Obligo a recargar la configuración al salir
    }

}