package com.yabu.floor.data.local.iex

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yabu.floor.data.model.iex.*

@Dao
interface IEXCloudDao {

    @Query("SELECT * FROM quote WHERE symbol IN (:symbols)")
    fun getQuotes(symbols: List<String>): List<Quote>

    @Query("SELECT * FROM quote WHERE symbol == :symbol")
    fun getQuote(symbol: String): Quote

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertQuote(quote: Quote): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertQuotes(quotes: List<Quote>): List<Long>

    @Query("SELECT * FROM company WHERE symbol == :symbol")
    fun getCompany(symbol: String): Company

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCompany(company: Company): Long

    @Query("SELECT * FROM keystats WHERE symbol == :symbol")
    fun getKeyStats(symbol: String): KeyStats

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertKeyStats(keyStats: KeyStats): Long

    @Query("SELECT * FROM charthistoricaldata WHERE compositeId = :compositeId")
    fun getChartHistoricalData(compositeId: String): ChartHistoricalData

    @Query("SELECT * FROM charthistoricaldata WHERE compositeId IN (:compositeIds)")
    fun getChartHistoricalDataSets(compositeIds: List<String>): List<ChartHistoricalData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertChartHistoricalData(oneDayChartHistoricalDataSet: ChartHistoricalData): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertChartHistoricalDataSets(chartHistoricalDataSet: List<ChartHistoricalData>)
    : List<Long>

    @Query("SELECT * FROM searchitem")
    fun getSearchItems(): List<SearchItem>

    @Query("DELETE FROM searchitem")
    fun deleteSearch()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSearchItems(searchItems: List<SearchItem>): List<Long>

    @Query("SELECT * FROM newsitem WHERE symbol == :symbol")
    fun getNews(symbol: String): List<NewsItem>

    @Query("DELETE FROM newsitem")
    fun deleteNews()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNews(sectors: List<NewsItem>): List<Long>

    @Query("SELECT * FROM sectorperformance")
    fun getSectorPerformance(): List<SectorPerformance>

    @Query("DELETE FROM sectorperformance")
    fun deleteSectorPerformance()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSectorPerformance(sectors: List<SectorPerformance>): List<Long>
}