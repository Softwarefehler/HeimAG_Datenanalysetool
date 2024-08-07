package ch.heimag.datenanalysetool.converter

import freemarker.template.utility.DateUtil
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object converter {

    fun frontendDateStringToInt(dateString: String): Int {
        val replacementValue = LocalDate.now()
        var date: LocalDate

        if (dateString != null) {
            try {
                val onlyDate = dateString.drop(4).take(11)

                val dayString = onlyDate.drop(4).take(2)
                val yearString = onlyDate.drop(7)

                val monthWordsString = onlyDate.take(3)
                val monthValueString = when (monthWordsString) {
                    "Jan" -> "01"
                    "Feb" -> "02"
                    "Mar" -> "03"
                    "Apr" -> "04"
                    "May" -> "05"
                    "Jun" -> "06"
                    "Jul" -> "07"
                    "Aug" -> "08"
                    "Sep" -> "09"
                    "Oct" -> "10"
                    "Nov" -> "11"
                    "Dec" -> "12"
                    else -> "12"
                }


                val dateStringBuild = "$yearString-$monthValueString-$dayString"

                // Definiere das Date-Format
                val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

                // Parsen des Strings in ein LocalDate-Objekt
                date = LocalDate.parse(dateStringBuild, dateFormatter)

            } catch (e: DateUtil.DateParseException) {
                date = replacementValue  // If parsing fails, set the actual time
            }
        } else {
            date = replacementValue  // If the dateString is null set the actual time
        }
        // Formatieren zu yyyymmdd
        val year = date.year
        val month = date.monthValue
        val day = date.dayOfMonth
        val dateInt = String.format("%04d%02d%02d", year, month, day).toInt()
        return dateInt
    }

    fun intToDate(dateInt: Int): LocalDate {
        val replacementValue = LocalDate.now()
        var date: LocalDate

        if (dateInt != null) {
            try {
                // Integer in String umwandeln
                val dateString = dateInt.toString()
                // DateTimeFormatter mit dem Muster "yyyyMMdd" erstellen
                val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
                // String in LocalDate konvertieren
                date = LocalDate.parse(dateString, formatter)

            } catch (e: DateUtil.DateParseException) {
                date = replacementValue  // If parsing fails, set the actual time
            }
        } else {
            date = replacementValue  // If the dateString is null set the actual time
        }
        return date
    }


}



