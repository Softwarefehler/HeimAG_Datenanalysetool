package ch.heimag.datenanalysetool.databases

import ch.heimag.datenanalysetool.conditions.OperatingConditions
import ch.heimag.datenanalysetool.converter.Converter
import ch.heimag.datenanalysetool.weatherdata.WeatherData
import kotlinx.serialization.Serializable
import org.slf4j.LoggerFactory
import java.sql.DriverManager
import java.sql.SQLException


@Serializable
data class DataPoint(val date: String, val temperature: String)

class Database {
    val PROTOCOL = "jdbc:mysql"
    val HOST = "localhost"
    val PORT = 3306
    val DATABASE = "heimag"
    val OPTIONS =
        "useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Europe/Berlin"
    val URL = "$PROTOCOL://$HOST:$PORT/$DATABASE?$OPTIONS"
    val USER = "datenanalysetool"
    val PASSWORD = "HeimAGS2we@!"

    private val logger = LoggerFactory.getLogger("Data.Database")


    fun checkDatabaseStatus(): String {
        var connection: java.sql.Connection? = null
        return try {
            logger.info("Überprüfe den Status der Datenbankverbindung.")
            connection = DriverManager.getConnection(URL, USER, PASSWORD)
            logger.info("Verbindung zur Datenbank hergestellt.")

            // Führe eine einfache Abfrage aus, um die Verbindung zu testen
            val statement = connection.createStatement()
            val resultSet = statement.executeQuery("SELECT 1")

            // Überprüfe, ob die Abfrage erfolgreich war
            if (resultSet.next()) {
                logger.warn("Datenbank ist vorhanden und erreichbar.")
                "Datenbank vorhanden"
            } else {
                logger.error("Die Datenbank konnte nicht erreicht werden.")
                "Datenbank nicht vorhanden"
            }
        } catch (e: SQLException) {
            logger.error("Fehler beim Herstellen der Datenbankverbindung: ${e.message}", e)
            e.printStackTrace()
            "Datenbank nicht vorhanden"
        } finally {
            connection?.close()
            logger.info("Datenbankverbindung geschlossen.")
        }
    }


    fun loadLatestDate(): String {
        logger.info("Lade das neueste Datum aus der Datenbank.")
        var latestDateInt = 0


        val connection = DriverManager.getConnection(URL, USER, PASSWORD)

        // create statement
        val statement = connection.createStatement()

        // SQL statement to load rows from database
        val sql = "SELECT * FROM wetterdaten ORDER BY datum DESC LIMIT 1"

        // SQL statement execute
        val data = statement.executeQuery(sql)

        // Output rows
        if (data.next()) {
            latestDateInt = data.getInt("datum")
            logger.debug("Neuester Datensatz: $latestDateInt")
        } else {
            logger.info("Keine Datensätze in der Tabelle wetterdaten gefunden.")
        }
        data.close()
        statement.close()
        connection.close()


        val latestDateString = if (latestDateInt == 0) {
            "Keine Daten"
        } else {
            Converter.intToString(latestDateInt)
        }
        logger.info("Der letzte Datum der Datenbank ist: $latestDateString")
        return latestDateString
    }


    fun loadOperatingStateKaltePeriode(
        operatingState: OperatingConditions
    ): MutableList<DataPoint> {
        logger.info("Datenbankabfrage kaltePeriode: (Startdatum: ${operatingState.startDate}, Enddatum: ${operatingState.endDate}, Ort(als Code): ${operatingState.countryCode}, Min.Temperatur: ${operatingState.kaltPeriodeMinTemperature}, Max.Temperatur: ${operatingState.kaltPeriodeMaxTemperature}).")

        val sqlRespondList = mutableListOf<DataPoint>()

        try {
            val connection = DriverManager.getConnection(URL, USER, PASSWORD)

            // create statement
            val statement = connection.createStatement()

            // SQL statement to load rows from database
            val sql = """
        SELECT ${operatingState.countryCode}, datum 
            FROM wetterdaten
            WHERE ${operatingState.countryCode} > ${operatingState.kaltPeriodeMinTemperature} 
            AND ${operatingState.countryCode} < ${operatingState.kaltPeriodeMaxTemperature}
            AND datum BETWEEN ${operatingState.startDate} AND ${operatingState.endDate};
            """.trimIndent()

            // SQL statement execute
            logger.debug("kaltePeriode: SQL-Abfrage: $sql")
            val data = statement.executeQuery(sql)

            // put output rows in to another list
            while (data.next()) {
                val dateInt = data.getInt("datum")
                val temperature = data.getDouble(operatingState.countryCode)

                val dateString = Converter.intToString(dateInt)
                val temperatureString = temperature.toString()

                val dataPoint = DataPoint(dateString, temperatureString)
                sqlRespondList.add(dataPoint)

                logger.debug("kaltePeriode: Datensatz hinzugefügt: Datum: $dateString, Temperatur: $temperatureString")
            }
            data.close()
            statement.close()
            connection.close()
        } catch (e: SQLException) {
            logger.error("Fehler beim Laden der Daten für die kaltePeriode: ${e.message}", e)
        }

        return (sqlRespondList)
    }


    fun loadOperatingStateHaupanteilHeizperiode(
        operatingState: OperatingConditions
    ): MutableList<DataPoint> {
        logger.info("Datenbankabfrage HauptanteilHeizperiode: (Startdatum: ${operatingState.startDate}, Enddatum: ${operatingState.endDate}, Ort(als Code): ${operatingState.countryCode}, Min.Temperatur: ${operatingState.hauptanteilHeizperiodeMinTemperature}, Max.Temperatur: ${operatingState.hauptanteilHeizperiodeMaxTemperature}).")

        val sqlRespondList = mutableListOf<DataPoint>()

        try {
            val connection = DriverManager.getConnection(URL, USER, PASSWORD)

            // create statement
            val statement = connection.createStatement()

            // SQL statement to load rows from database
            val sql = """
        SELECT ${operatingState.countryCode}, datum 
            FROM wetterdaten
            WHERE ${operatingState.countryCode} > ${operatingState.hauptanteilHeizperiodeMinTemperature} 
            AND ${operatingState.countryCode} < ${operatingState.hauptanteilHeizperiodeMaxTemperature}
            AND datum BETWEEN ${operatingState.startDate} AND ${operatingState.endDate};
            """.trimIndent()

            // SQL statement execute
            logger.debug("HauptanteilHeizperiode: SQL-Abfrage: $sql")
            val data = statement.executeQuery(sql)

            // put output rows in to another list
            while (data.next()) {
                val dateInt = data.getInt("datum")
                val temperature = data.getDouble(operatingState.countryCode)

                val dateString = Converter.intToString(dateInt)
                val temperatureString = temperature.toString()

                val dataPoint = DataPoint(dateString, temperatureString)
                sqlRespondList.add(dataPoint)
                logger.debug("HauptanteilHeizperiode: Datensatz hinzugefügt: Datum: $dateString, Temperatur: $temperatureString")
            }
            data.close()
            statement.close()
            connection.close()
        } catch (e: SQLException) {
            logger.error("Fehler beim Laden der Daten für den HauptanteilHeizperiode: ${e.message}", e)
        }

        return (sqlRespondList)
    }


    fun loadOperatingStateSchwachlast(
        operatingState: OperatingConditions
    ): MutableList<DataPoint> {
        logger.info("Datenbankabfrage Schwachlast: (Startdatum: ${operatingState.startDate}, Enddatum: ${operatingState.endDate}, Ort(als Code): ${operatingState.countryCode}, Min.Temperatur: ${operatingState.kaltPeriodeMinTemperature}, Max.Temperatur: ${operatingState.kaltPeriodeMaxTemperature}).")

        val sqlRespondList = mutableListOf<DataPoint>()


        try {
            val connection = DriverManager.getConnection(URL, USER, PASSWORD)

            // create statement
            val statement = connection.createStatement()

            // SQL statement to load rows from database
            val sql = """
        SELECT ${operatingState.countryCode}, datum 
            FROM wetterdaten
            WHERE ${operatingState.countryCode} > ${operatingState.schwachlastMinTemperature} 
            AND ${operatingState.countryCode} < ${operatingState.schwachlastMaxTemperature}
            AND datum BETWEEN ${operatingState.startDate} AND ${operatingState.endDate};
            """.trimIndent()

            logger.debug("Schwachlast: SQL-Abfrage: $sql")
            val data = statement.executeQuery(sql)

            // put output rows in to another list
            while (data.next()) {
                val dateInt = data.getInt("datum")
                val temperature = data.getDouble(operatingState.countryCode)

                val dateString = Converter.intToString(dateInt)
                val temperatureString = temperature.toString()

                val dataPoint = DataPoint(dateString, temperatureString)
                sqlRespondList.add(dataPoint)
                logger.debug("Schwachlast: Datensatz hinzugefügt: Datum: $dateString, Temperatur: $temperatureString")
            }

            data.close()
            statement.close()
            connection.close()
        } catch (e: SQLException) {
            logger.error("Fehler beim Laden der Daten für die Schwachlast: ${e.message}", e)
        }

        return (sqlRespondList)
    }


    fun setWeatherdataToDatabase(save: MutableList<WeatherData>) {
        logger.info("Speichere Wetterdaten in der Datenbank.")

        try {
            val connection = DriverManager.getConnection(URL, USER, PASSWORD)

            val sql = """
            INSERT INTO heimag.wetterdaten (
                datum, alt, ant, bas, ber, cdf, chd, chm, dav, elm, eng, grc, grh, gsb, gve, jun, lug, luz, mer, neu, otl, pay, rag, sae, sam, sbe, sia, sio, sma, stg
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """

            // Prepare statement
            connection.prepareStatement(sql).use { preparedStatement ->
                // Iterate over each record in the list and set values to the prepared statement
                for (record in save) {
                    preparedStatement.apply {
                        setObject(1, record.datum)
                        setObject(2, record.alt)
                        setObject(3, record.ant)
                        setObject(4, record.bas)
                        setObject(5, record.ber)
                        setObject(6, record.cdf)
                        setObject(7, record.chd)
                        setObject(8, record.chm)
                        setObject(9, record.dav)
                        setObject(10, record.elm)
                        setObject(11, record.eng)
                        setObject(12, record.grc)
                        setObject(13, record.grh)
                        setObject(14, record.gsb)
                        setObject(15, record.gve)
                        setObject(16, record.jun)
                        setObject(17, record.lug)
                        setObject(18, record.luz)
                        setObject(19, record.mer)
                        setObject(20, record.neu)
                        setObject(21, record.otl)
                        setObject(22, record.pay)
                        setObject(23, record.rag)
                        setObject(24, record.sae)
                        setObject(25, record.sam)
                        setObject(26, record.sbe)
                        setObject(27, record.sia)
                        setObject(28, record.sio)
                        setObject(29, record.sma)
                        setObject(30, record.stg)
                        addBatch()
                    }
                }

                logger.info("Batch-Insert für Wetterdaten wird ausgeführt.")
                preparedStatement.executeBatch()
            }

            connection.close()
            logger.info("Wetterdaten erfolgreich gespeichert.")
        } catch (e: SQLException) {
            logger.error("Fehler beim Speichern der Wetterdaten: ${e.message}", e)
        }
    }
}


