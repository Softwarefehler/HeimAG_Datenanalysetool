package ch.heimag.datenanalysetool.weatherdata

import kotlinx.serialization.Serializable


@Serializable
data class WeatherData(
    val datum: Int?,
    val alt: Double?,
    val ant: Double?,
    val bas: Double?,
    val ber: Double?,
    val cdf: Double?,
    val chd: Double?,
    val chm: Double?,
    val dav: Double?,
    val elm: Double?,
    val eng: Double?,
    val grc: Double?,
    val grh: Double?,
    val gsb: Double?,
    val gve: Double?,
    val jun: Double?,
    val lug: Double?,
    val luz: Double?,
    val mer: Double?,
    val neu: Double?,
    val otl: Double?,
    val pay: Double?,
    val rag: Double?,
    val sae: Double?,
    val sam: Double?,
    val sbe: Double?,
    val sia: Double?,
    val sio: Double?,
    val sma: Double?,
    val stg: Double?
)









