package com.yabu.floor.utils

import android.content.SharedPreferences

/**
 * Library to perform an action only once during first launch of the App.
 * Uses a Preference Key to store a flag set in the App's onCreate.
 * @param sharedPrefs [SharedPreferences] to store action flags.
 */
class OnlyLaunch(private val sharedPrefs: SharedPreferences) {

    /**
     * Performs the given action once and stores a flag set to true with [actionKey].
     * @param actionKey Shared Preferences Boolean key.
     * @param action Lambda action to perform once.
     */
    fun performActionOnce(actionKey: String, action: () -> Unit) {
        val actionFlag = sharedPrefs.getBoolean(actionKey, false)

        if (!actionFlag) {
            action.invoke()

            sharedPrefs.edit()
                .putBoolean(actionKey, true)
                .apply()
        }
    }
}