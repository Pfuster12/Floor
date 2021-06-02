package com.yabu.floor.data.remote.iex.response

import com.yabu.floor.data.model.iex.SearchItem
import com.yabu.floor.data.remote.Mappable

class GetSearchResponse : ArrayList<GetSearchResponseItem>(), Mappable<List<SearchItem>> {
    override fun map(): List<SearchItem> {
        return this.filter {
            it.securityType == IEXSecurityType.COMMON_STOCK.type
                    || it.securityType == IEXSecurityType.ETF.type
        }.map {
            SearchItem(it.symbol ?: "",
            it.exchange ?: "",
            it.region ?: "",
            it.securityName ?: "",
            it.securityType ?: "")
        }.filter { // Filter only US securities as IEX historical data supports US only.
            it.region == IEXSecurityRegion.US.region
        }
    }
}

enum class IEXSecurityType(val type: String) {
    COMMON_STOCK("cs"),
    ETF("et")
}

enum class IEXSecurityRegion(val region: String) {
    US("US"),
    GB("GB")
}

data class GetSearchResponseItem(
    val exchange: String?,
    val region: String?,
    val securityName: String?,
    val securityType: String?,
    val symbol: String?,
    val sector: String?
)