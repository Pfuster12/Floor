package com.yabu.floor.data.local.iex

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yabu.floor.data.model.iex.HistoricalDataItem

class IEXCloudConverter {
    @TypeConverter
    fun fromString(value: String): List<HistoricalDataItem> {
        val listType = object : TypeToken<ArrayList<HistoricalDataItem>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<HistoricalDataItem>): String? {
        val gson = Gson()
        return gson.toJson(list)
    }
}