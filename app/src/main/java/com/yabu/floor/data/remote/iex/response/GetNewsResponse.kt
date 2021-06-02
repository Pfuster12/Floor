package com.yabu.floor.data.remote.iex.response

import androidx.room.Entity
import com.yabu.floor.data.model.iex.NewsItem

class GetNewsResponse(elements: Collection<NewsItemResponse>) : ArrayList<NewsItemResponse>(elements)
{
    fun map(symbol: String): List<NewsItem> {
        return this.map listMap@{
            return@listMap NewsItem(
                symbol = symbol,
                datetime = it.datetime,
                headline = it.headline,
                source = it.source,
                url = it.url,
                summary = it.summary,
                related = it.related,
                image = it.image,
                lang = it.lang,
                hasPaywall = it.hasPaywall
            )
        }
    }
}

@Entity
data class NewsItemResponse (
    val datetime: Long,
    val headline: String,
    val source: String,
    val url: String,
    val summary: String,
    val related: String,
    val image: String,
    val lang: String,
    val hasPaywall: Boolean
)
