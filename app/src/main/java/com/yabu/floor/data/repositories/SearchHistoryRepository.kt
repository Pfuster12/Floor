package com.yabu.floor.data.repositories

import com.yabu.floor.data.local.qoala.SearchHistoryDao
import com.yabu.floor.data.model.search.SearchHistoryItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchHistoryRepository @Inject constructor(private val db: SearchHistoryDao) {

    /**
     * Get all search.
     */
    suspend fun getAll(): List<SearchHistoryItem> {
        return withContext(Dispatchers.Default) {
            return@withContext db.getAll()
        }
    }

    /**
     * Insert search items in the local db.
     * @param items [SearchHistoryItem]
     */
    suspend fun insertAll(items: List<SearchHistoryItem>) {
        withContext(Dispatchers.IO) {
            db.insertAll(items)
        }
    }

    /**
     * Get a search item by ticker.
     * @param ticker [String]
     */
    suspend fun get(ticker: String): SearchHistoryItem? {
        return withContext(Dispatchers.Default) {
            return@withContext db.get(ticker)
        }
    }

    /**
     * Insert a search item in the local db.
     * Cap the item count to 3.
     * @param item [SearchHistoryItem]
     */
    suspend fun insert(item: SearchHistoryItem) {
        withContext(Dispatchers.IO) {
            val items = db.getAll()

            // Delete oldest item if count is above 3.
            if (items.size > 3) {
                db.delete(items[0])
            }

            db.insert(item)
        }
    }

    /**
     * Update a search item in the local db.
     * @param item [SearchHistoryItem]
     */
    suspend fun update(item: SearchHistoryItem) {
        withContext(Dispatchers.IO) {
            db.update(item)
        }
    }
}