package com.yabu.floor.data.local.floor

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yabu.floor.data.model.portfolio.PortfolioItem

class PortfolioConverter {
    @TypeConverter
    fun fromString(value: String): List<PortfolioItem> {
        val listType = object : TypeToken<ArrayList<PortfolioItem>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<PortfolioItem>): String? {
        val gson = Gson()
        return gson.toJson(list)
    }
}