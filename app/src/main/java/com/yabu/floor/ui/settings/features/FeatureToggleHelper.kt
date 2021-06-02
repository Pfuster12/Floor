package com.yabu.floor.ui.settings.features

import android.content.Context
import androidx.preference.PreferenceManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Feature toggle interface with the [PreferenceManager].
 * Use the [Feature] enums to find if a feature toggle is active.
 */
class FeatureToggleHelper @Inject constructor(@ApplicationContext val context: Context) {

    /**
     * Find the feature flag state.
     */
    fun isActivated(feature: Feature): Boolean {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return sharedPreferences.getBoolean(context.getString(feature.resource), true)
    }
}