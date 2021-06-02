package com.yabu.floor.data.remote.iex.response

import com.yabu.floor.data.model.iex.Company
import com.yabu.floor.data.remote.Mappable

data class GetCompanyResponse(
    val CEO: String?,
    val address: String?,
    val address2: String?,
    val city: String?,
    val companyName: String?,
    val country: String?,
    val description: String?,
    val employees: Int?,
    val exchange: String?,
    val industry: String?,
    val issueType: String?,
    val phone: String?,
    val primarySicCode: Int?,
    val sector: String?,
    val securityName: String?,
    val state: String?,
    val symbol: String?,
    val tags: List<String>?,
    val website: String?,
    val zip: String?
) : Mappable<Company> {
    override fun map(): Company {
        return Company(symbol ?: "",
            CEO ?: "",
        address ?: "",
        address2 ?: "",
        city ?: "",
        companyName ?: "",
        country ?: "",
        description ?: "",
        employees ?: 0,
        exchange ?: "",
        industry ?: "",
        issueType ?: "",
        phone ?: "",
        primarySicCode ?: 0,
        sector ?: "",
        securityName ?: "",
        state ?: "",
        tags?.joinToString() ?: "",
        website ?: "")
    }
}