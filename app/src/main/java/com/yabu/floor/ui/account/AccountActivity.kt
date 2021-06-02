package com.yabu.floor.ui.account

import android.content.Intent
import android.os.Bundle
import android.view.HapticFeedbackConstants
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.firebase.auth.FirebaseAuth
import com.yabu.floor.R
import com.yabu.floor.databinding.ActivityAccountBinding

class AccountActivity : AppCompatActivity() {
    private val logtag = AccountActivity::class.simpleName

    private lateinit var binding: ActivityAccountBinding

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_account)
        binding.apply {
            accountLogOutButton.setOnClickListener {
                auth.signOut()
                finish()
            }
            accountAvatar.clipToOutline = true
        }

        auth = FirebaseAuth.getInstance()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        binding.user = currentUser

        if (currentUser != null) {
            Glide.with(this)
                .load(currentUser.photoUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.accountAvatar)
        } else {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
            finish()
        }
    }

    fun onToolbarBack(v: View) {
        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
        onBackPressed()
    }
}