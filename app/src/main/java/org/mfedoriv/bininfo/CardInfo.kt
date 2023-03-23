package org.mfedoriv.bininfo

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CardInfo(
    @Transient var bin: Int? = null,
    val bank: Bank?,
    val brand: String?,
    val country: Country?,
    val number: Number?,
    val prepaid: Boolean?,
    val scheme: String?,
    val type: String?
)

fun Boolean?.convert(): String {
    return when (this) {
        true -> "Yes"
        false -> "No"
        else -> ""
    }
}

@JsonClass(generateAdapter = true)
data class Bank(
    val city: String?,
    val name: String?,
    val phone: String?,
    val url: String?
)

@JsonClass(generateAdapter = true)
data class Country(
    val alpha2: String?,
    val currency: String?,
    val emoji: String?,
    val latitude: Int?,
    val longitude: Int?,
    val name: String?,
    val numeric: String?
)

@JsonClass(generateAdapter = true)
data class Number(
    val length: Int?,
    val luhn: Boolean?
)