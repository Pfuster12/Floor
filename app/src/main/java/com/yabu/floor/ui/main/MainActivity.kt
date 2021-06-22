package com.yabu.floor.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.yabu.floor.R
import com.yabu.floor.data.model.portfolio.Portfolio
import com.yabu.floor.databinding.ActivityMainBinding
import com.yabu.floor.ui.splash.SplashActivity
import com.yabu.floor.utils.OnlyLaunch
import com.yabu.floor.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Launcher [AppCompatActivity] for Floored.
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val logtag = this::class.simpleName

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val intent = Intent(this, SplashActivity::class.java)
        startActivity(intent)

        createMyPortfolio()
        createIndexPortfolio()
    }

    /**
     * Create the main portfolio on startup only once.
     */
    private fun createMyPortfolio() {
        // Force init viewModel to avoid IllegalStateException.
        val viewModel = viewModel

        OnlyLaunch(sharedPrefs = Utils.getSharedPreferences(this)).apply {
            performActionOnce(Portfolio.PORTFOLIO_PREFERENCES_KEY) {
                CoroutineScope(Dispatchers.IO).launch {
                    viewModel.insertPortfolio(Portfolio())

                    // Store the default id as current.
                    Portfolio.saveCurrentPortfolio(this@MainActivity, Portfolio.PORTFOLIO_ID)
                }
            }
        }
    }

    /**
     * Create the index portfolio on startup only once.
     */
    private fun createIndexPortfolio() {
        // Force init viewModel to avoid IllegalStateException.
        val viewModel = viewModel

        OnlyLaunch(sharedPrefs = Utils.getSharedPreferences(this)).apply {
            performActionOnce(Portfolio.INDEX_PREFERENCES_KEY) {
                CoroutineScope(Dispatchers.IO).launch {
                    viewModel.insertPortfolio(Portfolio(
                            id = Portfolio.INDEX_ID,
                            name = "Main Indices",
                            visibleToUser = false,
                            items = Portfolio.initialIndexItems
                    ))
                }
            }
        }
    }
}