package com.yabu.floor.data.repositories

import com.yabu.floor.data.NetworkResource
import com.yabu.floor.data.Resource
import com.yabu.floor.data.local.iex.IEXCloudDao
import com.yabu.floor.data.local.iex.IEXCloudDatabase
import com.yabu.floor.data.model.iex.*
import com.yabu.floor.data.remote.iex.request.IEXChartRange
import com.yabu.floor.data.remote.iex.response.*
import com.yabu.floor.data.services.IEXCloudWebservice
import retrofit2.Call
import javax.inject.Inject

/**
 * Handles the IEX Cloud data requests from network and locally.
 * @property webservice [IEXCloudWebservice]
 * @property db [IEXCloudDatabase]
 */
class IEXCloudRepository @Inject constructor(private val webservice: IEXCloudWebservice,
                                             private val db: IEXCloudDao) {

    /**
     * Get Quote for a given symbol.
     * @param symbol
     */
    suspend fun getCompany(symbol: String): Resource<Company> {
        return object : NetworkResource<Company, GetCompanyResponse>() {
            override fun loadFromDisk(): Company {
                return db.getCompany(symbol)
            }

            override fun shouldFetch(disk: Company?): Boolean {
                return true
            }

            override fun loadData(): Call<GetCompanyResponse> {
                return webservice.getCompany(symbol)
            }

            override fun processResponse(data: GetCompanyResponse): Company {
                return data.map()
            }

            override fun writeToDisk(data: Company): Boolean {
                return db.insertCompany(data) > 0
            }
        }.emit()
    }

    /**
     * Get Quote for a given symbol.
     * @param symbol
     */
    suspend fun getQuote(symbol: String): Resource<Quote> {
        return object : NetworkResource<Quote, GetQuoteResponse>() {
            override fun loadFromDisk(): Quote {
                return db.getQuote(symbol)
            }

            override fun shouldFetch(disk: Quote?): Boolean {
                return true
            }

            override fun loadData(): Call<GetQuoteResponse> {
                return webservice.getQuote(symbol)
            }

            override fun processResponse(data: GetQuoteResponse): Quote {
                return data.map()
            }

            override fun writeToDisk(data: Quote): Boolean {
                return db.insertQuote(data) > 0
            }
        }.emit()
    }

    /**
     * Batch quotes for a comma separated list of symbols.
     * @param symbols
     */
    suspend fun batchQuotes(symbols: List<String>): Resource<List<Quote>> {
        return object : NetworkResource<List<Quote>, GetBatchQuoteResponse>() {
            override fun loadFromDisk(): List<Quote> {
                return db.getQuotes(symbols)
            }

            override fun shouldFetch(disk: List<Quote>?): Boolean {
                return true
            }

            override fun loadData(): Call<GetBatchQuoteResponse> {
                return webservice.batchQuotes(symbols.joinToString())
            }

            override fun processResponse(data: GetBatchQuoteResponse): List<Quote> {
                return data.map()
            }

            override fun writeToDisk(data: List<Quote>): Boolean {
                return db.insertQuotes(data).isNotEmpty()
            }
        }.emit()
    }

    /**
     * Get Key Stats for a given symbol.
     * @param symbol
     */
    suspend fun getKeyStats(symbol: String): Resource<KeyStats> {
        return object : NetworkResource<KeyStats, GetKeyStatsResponse>() {
            override fun loadFromDisk(): KeyStats {
                return db.getKeyStats(symbol)
            }

            override fun shouldFetch(disk: KeyStats?): Boolean {
                return true
            }

            override fun loadData(): Call<GetKeyStatsResponse> {
                return webservice.getKeyStats(symbol)
            }

            override fun processResponse(data: GetKeyStatsResponse): KeyStats {
                return data.map(symbol)
            }

            override fun writeToDisk(data: KeyStats): Boolean {
                return db.insertKeyStats(data) > 0
            }
        }.emit()
    }

    /**
     * Batch quotes for a comma separated list of symbols.
     * @param symbol
     * @param range
     */
    suspend fun getChartHistoricalDataSet(symbol: String,
                                  range: IEXChartRange = IEXChartRange.FIVE_DAYS_10_MIN_INTERVALS,
                                  includeToday: Boolean?,
                                  chartInterval: Int?,
                                  chartLast: Int?,
                                  chartCloseOnly: Boolean?)
            : Resource<ChartHistoricalData> {
        return object : NetworkResource<ChartHistoricalData, GetChartHistoricalDataResponse>() {
            override fun loadFromDisk(): ChartHistoricalData {
                return db.getChartHistoricalData(
                    compositeId = ChartHistoricalData.createCompositeId(symbol,
                        range.range,
                        chartInterval)
                )
            }

            override fun shouldFetch(disk: ChartHistoricalData?): Boolean {
                return disk == null
                        || disk.timestamp + (1000L*60*5) < System.currentTimeMillis()
            }

            override fun loadData(): Call<GetChartHistoricalDataResponse> {
                return webservice.getChart(
                    symbol = symbol,
                    range = range.range,
                    includeToday = includeToday,
                    chartInterval =  chartInterval,
                    chartLast = chartLast,
                    chartCloseOnly = chartCloseOnly
                )
            }

            override fun processResponse(data: GetChartHistoricalDataResponse): ChartHistoricalData {
                return data.map(symbol, range.range, chartInterval ?: 1)
            }

            override fun writeToDisk(data: ChartHistoricalData): Boolean {
                return db.insertChartHistoricalData(data) > 0
            }
        }.emit()
    }

    /**
     * Batch quotes for a comma separated list of symbols.
     * @param symbols
     * @param range
     */
    suspend fun batchChartHistoricalDataSets(symbols: List<String>,
                                             chartLast: Int = 39,
                                             range: IEXChartRange = IEXChartRange.FIVE_DAYS_10_MIN_INTERVALS,
                                             chartInterval: Int? = null)
            : Resource<List<ChartHistoricalData>> {
        return object : NetworkResource<List<ChartHistoricalData>,
                GetBatchChartHistoricalDataResponse>() {
            override fun loadFromDisk(): List<ChartHistoricalData> {
                return db.getChartHistoricalDataSets(symbols.map {
                    ChartHistoricalData.createCompositeId(it, range.range, chartInterval)
                })
            }

            override fun shouldFetch(disk: List<ChartHistoricalData>?): Boolean {
                var flag = 0
                if (disk.isNullOrEmpty()) {
                    flag++
                }

                disk?.forEach {
                    // 5 min stale
                    if (it.timestamp + (1000L*60*5) < System.currentTimeMillis()) {
                        flag++
                    }
                }
                return flag > 0
            }

            override fun loadData(): Call<GetBatchChartHistoricalDataResponse> {
                return webservice.batchCharts(
                    symbols = symbols.joinToString(),
                    chartLast = chartLast,
                    chartInterval = chartInterval,
                    includeToday = true,
                    range = range.range
                )
            }

            override fun processResponse(data: GetBatchChartHistoricalDataResponse)
                    : List<ChartHistoricalData> {
                return data.map(range.range, chartInterval ?: 1)
            }

            override fun writeToDisk(data: List<ChartHistoricalData>): Boolean {
                return db.insertChartHistoricalDataSets(data).isNotEmpty()
            }
        }.emit()
    }

    /**
     * Get search results from search keywords.
     * @param search
     */
    suspend fun querySearch(search: String): Resource<List<SearchItem>> {
        return object : NetworkResource<List<SearchItem>, GetSearchResponse>() {
            override fun loadFromDisk(): List<SearchItem> {
                return db.getSearchItems()
            }

            override fun shouldFetch(disk: List<SearchItem>?): Boolean {
                return true
            }

            override fun loadData(): Call<GetSearchResponse> {
                return webservice.getSearch(search)
            }

            override fun processResponse(data: GetSearchResponse): List<SearchItem> {
                return data.map()
            }

            override fun writeToDisk(data: List<SearchItem>): Boolean {
                // Delete old search items in disk and write the new data.
                db.deleteSearch()
                return db.insertSearchItems(data).isNotEmpty()
            }
        }.emit()
    }

    suspend fun getNews(symbol: String): Resource<List<NewsItem>> {
        return object : NetworkResource<List<NewsItem>, GetNewsResponse>() {
            override fun loadFromDisk(): List<NewsItem> {
                return db.getNews(symbol)
            }

            override fun shouldFetch(disk: List<NewsItem>?): Boolean {
                return true
            }

            override fun loadData(): Call<GetNewsResponse> {
                return webservice.getNews(symbol)
            }

            override fun processResponse(data: GetNewsResponse): List<NewsItem> {
                return data.map(symbol)
            }

            override fun writeToDisk(data: List<NewsItem>): Boolean {
                // Delete old search items in disk and write the new data.
                db.deleteNews()
                return db.insertNews(data).isNotEmpty()
            }
        }.emit()
    }

    suspend fun getSectorPerformance(): Resource<List<SectorPerformance>> {
        return object : NetworkResource<List<SectorPerformance>, GetSectorPerformanceResponse>() {
            override fun loadFromDisk(): List<SectorPerformance> {
                return db.getSectorPerformance()
            }

            override fun shouldFetch(disk: List<SectorPerformance>?): Boolean {
                var flag = 0
                if (disk.isNullOrEmpty()) {
                    flag++
                }

                disk?.forEach {
                    // 5 min stale
                    if (it.timestamp + (1000L*60*5) < System.currentTimeMillis()) {
                        flag++
                    }
                }
                return flag > 0
            }

            override fun loadData(): Call<GetSectorPerformanceResponse> {
                return webservice.getSectorPerformance()
            }

            override fun processResponse(data: GetSectorPerformanceResponse): List<SectorPerformance> {
                return data.map()
            }

            override fun writeToDisk(data: List<SectorPerformance>): Boolean {
                // Delete old search items in disk and write the new data.
                db.deleteSectorPerformance()
                return db.insertSectorPerformance(data).isNotEmpty()
            }
        }.emit()
    }
}