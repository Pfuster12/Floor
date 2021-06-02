package com.yabu.floor.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.yabu.floor.FloorApplication

object Utils {

    fun showKeyboard(view: View) {
        view.post {
            view.requestFocus()
            val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    fun hideKeyboard(activity: Activity?) {
        if (activity == null)
            return

        val imm: InputMethodManager =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view: View? = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun getSharedPreferences(context: Context): SharedPreferences
            = context.getSharedPreferences(FloorApplication.SHARED_PREFERENCES_KEY,
        Context.MODE_PRIVATE)
}