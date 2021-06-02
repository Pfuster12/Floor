package com.yabu.floor.data.remote.iex.response

import com.yabu.floor.data.model.iex.SectorPerformance

class GetSectorPerformanceResponse : ArrayList<GetSectorPerformanceResponseItem>() {
    fun map(): List<SectorPerformance> {
        return this.map listMap@{
            return@listMap SectorPerformance(
                it.name,
                it.type,
                it.performance*100f,
                it.lastUpdated
            )
        }
    }
}

data class GetSectorPerformanceResponseItem(
    val lastUpdated: Long,
    val name: String,
    val performance: Double,
    val type: String
)