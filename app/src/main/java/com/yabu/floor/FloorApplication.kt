package com.yabu.floor

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FloorApplication : Application() {
    companion object {
        const val SHARED_PREFERENCES_KEY = "floored.SHARED_PREFERENCES_KEY"
    }

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }

    /**
     * Store the [FirebaseUser] globally for auth purposes.
     */
    var user: FirebaseUser? = null

    fun isSignedIn(): Boolean {
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        user = FirebaseAuth.getInstance().currentUser
        return user != null
    }
}