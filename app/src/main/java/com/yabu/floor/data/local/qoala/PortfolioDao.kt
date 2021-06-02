package com.yabu.floor.data.local.qoala

import androidx.lifecycle.LiveData
import androidx.room.*
import com.yabu.floor.data.model.portfolio.Portfolio

@Dao
interface PortfolioDao {
    @Query("SELECT * FROM portfolio")
    suspend fun getAll(): List<Portfolio>

    @Query("SELECT * FROM portfolio")
    fun subscribeAll(): LiveData<List<Portfolio>>

    @Query("SELECT * FROM portfolio WHERE visibleToUser == 1")
    suspend fun getAllUserPortfolios(): List<Portfolio>

    @Query("SELECT * FROM portfolio WHERE visibleToUser == 1")
    fun subscribeAllUserPortfolios(): LiveData<List<Portfolio>>

    @Query("SELECT * FROM portfolio WHERE id == :id")
    suspend fun get(id: String): Portfolio?

    @Query("SELECT * FROM portfolio WHERE id == :id")
    fun subscribe(id: String): LiveData<Portfolio>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(portfolio: Portfolio): Long

    @Update
    suspend fun update(portfolio: Portfolio): Int

    @Delete
    suspend fun delete(portfolio: Portfolio)
}