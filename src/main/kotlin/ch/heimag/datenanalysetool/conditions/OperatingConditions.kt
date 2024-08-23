package ch.heimag.datenanalysetool.conditions

import kotlinx.serialization.Serializable


@Serializable
data class OperatingConditions(
    val startDate: Int,
    val endDate: Int,
    val country: String
) {

    val kaltPeriodeMinTemperature = -10.0
    val kaltPeriodeMaxTemperature = -5.0
    val hauptanteilHeizperiodeMinTemperature = 0.0
    val hauptanteilHeizperiodeMaxTemperature = 20.0
    val schwachlastMinTemperature = 15.0
    val schwachlastMaxTemperature = 25.0


    var countryCode: String = null.toString()

    init {
        countryCode = when (country) {
            "Altdorf UR" -> "alt"
            "Andermatt UR" -> "ant"
            "Basel Binningen BL" -> "bas"
            "Bern Zollikofen BE" -> "ber"
            "La Chaux-de-Fonds NE" -> "cdf"
            "Château-d'Oex VD" -> "chd"
            "Davos GR" -> "dav"
            "Elm GL" -> "elm"
            "Engelberg OW" -> "eng"
            "Grächen VS" -> "grc"
            "Grimsel Hospiz BE" -> "grh"
            "Col du Grand St-Bernard VS" -> "gsb"
            "Genève / Cointrin GE" -> "gve"
            "Jungfraujoch VS" -> "jun"
            "Lugano TI" -> "lug"
            "Luzern" -> "luz"
            "Meiringen" -> "mer"
            "Neuchâtel NE" -> "neu"
            "Locarno / Monti TI" -> "otl"
            "Payerne VD" -> "pay"
            "Bad Ragaz SG" -> "rag"
            "Säntis AI" -> "sae"
            "Samedan GR" -> "sam"
            "San Bernardino GR" -> "sbe"
            "Segl-Maria GR" -> "sia"
            "Sion VS" -> "sio"
            "Zürich Fluntern ZH" -> "sma"
            "St.Gallen SG" -> "STG"
            else -> {
                "unknown"
            }
        }
    }
}

