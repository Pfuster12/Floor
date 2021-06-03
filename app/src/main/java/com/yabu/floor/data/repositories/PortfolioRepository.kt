package com.yabu.floor.data.repositories

import androidx.lifecycle.LiveData
import com.yabu.floor.data.local.floor.PortfolioDao
import com.yabu.floor.data.local.floor.QoalaDatabase
import com.yabu.floor.data.model.portfolio.Portfolio
import com.yabu.floor.data.model.portfolio.PortfolioItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Handles the Portfolio requests.
 * @property db [QoalaDatabase]
 */
class PortfolioRepository @Inject constructor(private val db: PortfolioDao) {

    /**
     * Get all portfolios.
     */
    suspend fun getAll(): List<Portfolio> {
        return withContext(Dispatchers.Default) {
            return@withContext db.getAll()
        }
    }

    /**
     * Subscribe to all portfolios.
     */
    suspend fun subscribeAllUserPortfolios(): LiveData<List<Portfolio>> {
        return withContext(Dispatchers.Default) {
            return@withContext db.subscribeAllUserPortfolios()
        }
    }

    /**
     * Get a Portfolio by id.
     * @param id [String]
     */
    suspend fun get(id: String): Portfolio? {
        return withContext(Dispatchers.Default) {
            return@withContext db.get(id)
        }
    }

    /**
     * Subscribe to a Portfolio.
     * @param id Portfolio id.
     */
    suspend fun subscribe(id: String): LiveData<Portfolio> {
        return withContext(Dispatchers.Default) {
            return@withContext db.subscribe(id)
        }
    }

    /**
     * Get a Portfolio by id.
     * @param id [String]
     */
    suspend fun getItems(id: String): List<PortfolioItem> {
        return withContext(Dispatchers.Default) {
            return@withContext db.get(id)?.items?.map { it } ?: listOf()
        }
    }

    /**
     * Insert a Portfolio in the local db.
     * @param portfolio [Portfolio]
     */
    suspend fun insert(portfolio: Portfolio) {
        withContext(Dispatchers.IO) {
            db.insert(portfolio)
        }
    }

    /**
     * Update a Portfolio in the local db.
     * @param portfolio [Portfolio]
     */
    suspend fun update(portfolio: Portfolio) {
        withContext(Dispatchers.IO) {
            db.update(portfolio)
        }
    }

    suspend fun moveItem(from: Int, to: Int, portfolio: Portfolio?) {
        portfolio?.move(from, to)
        withContext(Dispatchers.IO) {
            //db.update(portfolio)
        }
    }
}