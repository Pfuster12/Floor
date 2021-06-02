package com.yabu.floor.ui.account

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.HapticFeedbackConstants
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.yabu.floor.R
import com.yabu.floor.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private val logtag = AppCompatActivity::class.simpleName

    companion object {
        const val RC_SIGN_IN = 101
    }

    private lateinit var binding: ActivityLoginBinding

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.google_web_client))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.apply {
            loginGoogle.setOnClickListener{
                it.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
                val signInIntent = googleSignInClient.signInIntent
                startActivityForResult(signInIntent, RC_SIGN_IN)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.e(logtag, "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val accountIntent = Intent(this, AccountActivity::class.java)
                    startActivity(accountIntent)
                    finish()
                }
            }
    }

    fun onToolbarBack(v: View) {
        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
        onBackPressed()
    }
}