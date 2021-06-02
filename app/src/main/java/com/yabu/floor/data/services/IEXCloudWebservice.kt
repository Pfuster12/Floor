package com.yabu.floor.data.services

import com.yabu.floor.BuildConfig
import com.yabu.floor.data.remote.iex.response.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface IEXCloudWebservice {

    @GET("stock/{symbol}/company")
    fun getCompany(@Path("symbol") symbol: String,
                 @Query("token") token: String = BuildConfig.IEX_PRODUCTION_KEY)
            : Call<GetCompanyResponse>

    @GET("stock/{symbol}/quote")
    fun getQuote(@Path("symbol") symbol: String,
                 @Query("filter") filter: String =
                     "symbol,latestPrice,latestSource,latestTime,latestUpdate," +
                             "latestVolume,change,changePercent,extendedPrice,extendedChange," +
                             "extendedChangePercent,extendedPriceTime,peRatio,week52High,week52Low,"+
                             "ytdChange,isUSMarketOpen,companyName",
                 @Query("displayPercent") displayPercent: Boolean = true,
                 @Query("token") token: String = BuildConfig.IEX_PRODUCTION_KEY)
            : Call<GetQuoteResponse>

    @GET("stock/market/batch")
    fun batchQuotes(@Query("symbols") symbols: String,
                    @Query("types") types: String = "quote",
                    @Query("filter") filter: String =
                     "symbol,latestPrice,latestSource,latestTime,latestUpdate," +
                             "latestVolume,change,changePercent,extendedPrice,extendedChange," +
                             "extendedChangePercent,extendedPriceTime,peRatio,week52High,week52Low,"+
                             "ytdChange,isUSMarketOpen,companyName",
                    @Query("token") token: String = BuildConfig.IEX_PRODUCTION_KEY,
                    @Query("displayPercent") displayPercent: Boolean = true)
            : Call<GetBatchQuoteResponse>

    @GET("stock/{symbol}/stats")
    fun getKeyStats(@Path("symbol") symbol: String,
                    @Query("displayPercent") displayPercent: Boolean = true,
                    @Query("token") token: String = BuildConfig.IEX_PRODUCTION_KEY)
            : Call<GetKeyStatsResponse>

    @GET("stock/{symbol}/chart/{range}")
    fun getChart(@Path("symbol") symbol: String,
                 @Path("range") range: String,
                 @Query("filter") filter: String =
                     "close,marketAverage,label,date",
                 @Query("displayPercent") displayPercent: Boolean = true,
                 @Query("chartLast") chartLast: Int? = null,
                 @Query("includeToday") includeToday: Boolean?,
                 @Query("sort") sort: String? = null,
                 @Query("chartInterval") chartInterval: Int? = null,
                 @Query("chartCloseOnly") chartCloseOnly: Boolean? = null,
                 @Query("token") token: String = BuildConfig.IEX_PRODUCTION_KEY)
            : Call<GetChartHistoricalDataResponse>

    @GET("stock/market/batch")
    fun batchCharts(@Query("symbols") symbols: String,
                    @Query("types") types: String = "chart",
                    @Query("range") range: String = "5dm",
                    @Query("includeToday") includeToday: Boolean = true,
                    @Query("chartLast") chartLast: Int? = null,
                    @Query("chartInterval") chartInterval: Int? = 10,
                    @Query("filter") filter: String =
                        "close,marketAverage,label,date",
                    @Query("displayPercent") displayPercent: Boolean = true,
                    @Query("sort") sort: String = "asc",
                    @Query("token") token: String = BuildConfig.IEX_PRODUCTION_KEY)
            : Call<GetBatchChartHistoricalDataResponse>

    @GET("search/{search}")
    fun getSearch(@Path("search") search: String,
                  @Query("token") token: String = BuildConfig.IEX_PRODUCTION_KEY)
            : Call<GetSearchResponse>

    @GET("stock/{symbol}/news/last/")
    fun getNews(@Path("symbol") symbol: String,
                @Query("token") token: String = BuildConfig.IEX_PRODUCTION_KEY)
    : Call<GetNewsResponse>

    @GET("stock/market/sector-performance")
    fun getSectorPerformance(@Query("token") token: String = BuildConfig.IEX_PRODUCTION_KEY)
        : Call<GetSectorPerformanceResponse>
}