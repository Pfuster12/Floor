package com.yabu.floor.ui.settings.features

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.yabu.floor.R

@Suppress("UNUSED")
class FeatureToggleFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.feature_toggle, rootKey)
    }
}