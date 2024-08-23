package ch.heimag.datenanalysetool.converter

import freemarker.template.utility.DateUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object Converter {
    val logger: Logger = LoggerFactory.getLogger("Converter")


    fun databaseDateStringToInt(dateString: String): Int {
        logger.info("Für die Datenbank zu formatierender DatumString:  $dateString")

        val replacementValue = LocalDate.now()
        var date: LocalDate

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
            logger.warn("Umgeschriebener String: $dateStringBuild")

            // Definiere das Date-Format
            val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

            // Parsen des Strings in ein LocalDate-Objekt
            date = LocalDate.parse(dateStringBuild, dateFormatter)
            logger.debug("geparste Variable: {}", date)

        } catch (e: DateUtil.DateParseException) {
            date = replacementValue  // If parsing fails, set the actual time
            logger.error("Variable konnte nicht geparst werden. jetziger Zeitwert wird genommen: $date")
        }

        // Formatieren zu yyyymmdd
        val year = date.year
        val month = date.monthValue
        val day = date.dayOfMonth
        val dateInt = String.format("%04d%02d%02d", year, month, day).toInt()

        logger.debug("Formatierte Variable im Typ INT: {}", dateInt)

        return dateInt
    }


    fun intToString(dateInt: Int): String {
        logger.info("Formatumwandlung: Datums-integer zu Datum-String umwandeln. Zu umwandelnder Wert:  $dateInt")

        val dateStringRaw = dateInt.toString()

        // String in das Format yyyy-MM-dd umwandeln
        val dateString =
            "${dateStringRaw.substring(0, 4)}-${dateStringRaw.substring(4, 6)}-${dateStringRaw.substring(6, 8)}"

        logger.debug("Wert der im Format yyyy-mm-dd umgewandelter: $dateString")

        return dateString
    }


    fun dateStringToInt(dateString: String): Int {
        logger.info("Formatumwandlung: Datums-String (yyyy-mm-dd) zu Datum-Int umwandeln. Zu umwandelnder Wert:  $dateString")

        val modifiedString = dateString.replace("-", "")
        val dateInt = modifiedString.toInt()

        logger.debug("Umgewandelte Datums-Int Variable: {}", dateInt)

        return dateInt
    }

}

