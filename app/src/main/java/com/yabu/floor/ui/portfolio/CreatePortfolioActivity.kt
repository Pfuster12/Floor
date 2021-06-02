package com.yabu.floor.ui.portfolio

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.yabu.floor.R
import com.yabu.floor.databinding.ActivityCreatePortfolioBinding

class CreatePortfolioActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreatePortfolioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,
            R.layout.activity_create_portfolio)
    }
}