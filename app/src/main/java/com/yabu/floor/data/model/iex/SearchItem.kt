package com.yabu.floor.data.model.iex

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SearchItem(@PrimaryKey val symbol: String,
                      val exchange: String,
                      val region: String,
                      val securityName: String,
                      val securityType: String)