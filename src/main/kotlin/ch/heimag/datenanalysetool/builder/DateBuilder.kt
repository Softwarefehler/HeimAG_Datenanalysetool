package ch.heimag.datenanalysetool.builder

import freemarker.template.utility.DateUtil
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.util.*

class DateBuilder() {
    private var date: LocalDate = LocalDate.now()
    private val replacementVariable: LocalDate = LocalDate.now()

    fun buildDate(dateString: String): LocalDate {
        if (dateString != null) {
            try {
                val OnlyDate = dateString.drop(4).take(11)

                val dayString = OnlyDate.drop(4).take(2)
                val yearString = OnlyDate.drop(7)

                val mothWordsString = OnlyDate.take(3)
                val monthValueString = when(mothWordsString) {
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
                date = replacementVariable  // If parsing fails, set the actual time
            }
        } else {
            date = replacementVariable  // If the dateString is null set the actual time
        }
        return date
    }
}




