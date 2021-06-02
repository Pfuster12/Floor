package com.yabu.floor.ui.company

import android.view.HapticFeedbackConstants
import android.view.View
import javax.inject.Inject

class CompanyDataFragmentHandler @Inject constructor() {

    fun onLongPress(v: View): Boolean {
        v.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
        return true
    }
}