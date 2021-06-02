package com.yabu.floor.data.model.iex

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Company(
    @PrimaryKey
    val symbol: String,
    val CEO: String,
    val address: String,
    val address2: String,
    val city: String,
    val companyName: String,
    val country: String,
    val description: String,
    val employees: Int,
    val exchange: String,
    val industry: String,
    val issueType: String,
    val phone: String,
    val primarySicCode: Int,
    val sector: String,
    val securityName: String,
    val state: String,
    val tags: String,
    val website: String
)