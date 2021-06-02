package com.yabu.floor.data.model.iex

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class SectorPerformance(@PrimaryKey
                        val name: String,
                        val type: String,
                        val performance: Double,
                        val lastUpdated: Long,
                        val timestamp: Long = System.currentTimeMillis())