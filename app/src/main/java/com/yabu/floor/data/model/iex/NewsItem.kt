package com.yabu.floor.data.model.iex

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
class NewsItem(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val symbol: String,
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