package com.yabu.floor.data.model.search

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class SearchHistoryItem(@PrimaryKey
                        val ticker: String,
                        val name: String)