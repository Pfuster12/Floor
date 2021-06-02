package com.yabu.floor.data.local.qoala

import androidx.room.*
import com.yabu.floor.data.model.search.SearchHistoryItem

@Dao
interface SearchHistoryDao {
    @Query("SELECT * FROM searchhistoryitem")
    suspend fun getAll(): List<SearchHistoryItem>

    @Query("SELECT * FROM searchhistoryitem WHERE ticker == :ticker")
    suspend fun get(ticker: String): SearchHistoryItem?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: SearchHistoryItem): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<SearchHistoryItem>): List<Long>

    @Update
    suspend fun update(item: SearchHistoryItem): Int

    @Delete
    suspend fun delete(item: SearchHistoryItem)
}