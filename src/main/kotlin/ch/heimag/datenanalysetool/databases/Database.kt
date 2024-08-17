package ch.heimag.datenanalysetool.databases

import ch.heimag.datenanalysetool.conditions.OperatingConditions
import ch.heimag.datenanalysetool.converter.converter
import ch.heimag.datenanalysetool.file.CSV
import kotlinx.serialization.Serializable
import java.sql.DriverManager
import java.sql.SQLException


@Serializable
data class DataPoint(val date: String, val temperature: String)

class Database {
    val PROTOCOL = "jdbc:mysql"
    val HOST = "localhost"
    val PORT = 3306
    val DATABASE = "heimag"
    val OPTIONS = "useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Europe/Berlin"
    val URL = "$PROTOCOL://$HOST:$PORT/$DATABASE?$OPTIONS"
    val USER = "datenanalysetool"
    val PASSWORD = "HeimAGS2we@!"


    fun checkDatabaseStatus(): String {
        var connection: java.sql.Connection? = null
        return try {
            // Versuche, eine Verbindung zur Datenbank herzustellen
            connection = DriverManager.getConnection(URL, USER, PASSWORD)

            // Führe eine einfache Abfrage aus, um die Verbindung zu testen
            val statement = connection.createStatement()
            val resultSet = statement.executeQuery("SELECT 1")

            // Überprüfe, ob die Abfrage erfolgreich war
            if (resultSet.next()) {
                "Datenbank vorhanden"
            } else {
                "Datenbank nicht vorhanden"
            }
        } catch (e: SQLException) {
            // Falls ein Fehler auftritt, gib eine Fehlermeldung zurück
            e.printStackTrace()
            "Datenbank nicht vorhanden"
        } finally {
            // Schließe die Verbindung, falls sie geöffnet wurde
            connection?.close()
        }
    }


    fun loadLatestDate(): String {
        var latestDateInt = 0
        // build connection to database
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

        }
        data.close()
        statement.close()
        connection.close()


        var latestDateString = if (latestDateInt == 0) {
            "Keine Daten"
        } else {
            converter.intToString(latestDateInt)
        }
        return latestDateString
    }


    fun loadOperatingStateKaltePeriode(
        operatingState: OperatingConditions
    ): MutableList<DataPoint> {

        val startDate = operatingState.startDate
        val endDate = operatingState.endDate
        val minTemperature = operatingState.kaltPeriodeMinTemperature
        val maxTemperature = operatingState.kaltPeriodeMaxTemperature
        val countryCode = operatingState.countryCode

        //sqlRespondList.clear()
        val sqlRespondList = mutableListOf<DataPoint>()


        // build connection to database
        val connection = DriverManager.getConnection(URL, USER, PASSWORD)

        // create statement
        val statement = connection.createStatement()

        // SQL statement to load rows from database
        val sql = """
        SELECT $countryCode, datum 
        FROM wetterdaten
        WHERE $countryCode > $minTemperature AND $countryCode < $maxTemperature
        AND datum BETWEEN $startDate AND $endDate;
        """.trimIndent()

        // SQL statement execute
        val data = statement.executeQuery(sql)

        // put output rows in to another list
        while (data.next()) {
            val dateInt = data.getInt("datum")
            val temperature = data.getDouble(countryCode)

            val dateString = converter.intToString(dateInt)
            val temperatureString = temperature.toString()

            val dataPoint = DataPoint(dateString, temperatureString)
            sqlRespondList.add(dataPoint)

            //println("$dateInt, $temperature") // Only for Display
        }

        // close resources
        data.close()
        statement.close()
        connection.close()

        return (sqlRespondList)
    }

    fun loadOperatingStateHaupanteilHeizperiode(
        operatingState: OperatingConditions
    ): MutableList<DataPoint> {

        val startDate = operatingState.startDate
        val endDate = operatingState.endDate
        val minTemperature = operatingState.hauptanteilHeizperiodeMinTemperature
        val maxTemperature = operatingState.hauptanteilHeizperiodeMaxTemperature
        val countryCode = operatingState.countryCode

        //sqlRespondList.clear()
        val sqlRespondList = mutableListOf<DataPoint>()


        // build connection to database
        val connection = DriverManager.getConnection(URL, USER, PASSWORD)

        // create statement
        val statement = connection.createStatement()

        // SQL statement to load rows from database
        val sql = """
        SELECT $countryCode, datum 
        FROM wetterdaten
        WHERE $countryCode > $minTemperature AND $countryCode < $maxTemperature
        AND datum BETWEEN $startDate AND $endDate;
        """.trimIndent()

        // SQL statement execute
        val data = statement.executeQuery(sql)

        // put output rows in to another list
        while (data.next()) {
            val dateInt = data.getInt("datum")
            val temperature = data.getDouble(countryCode)

            val dateString = converter.intToString(dateInt)
            val temperatureString = temperature.toString()

            val dataPoint = DataPoint(dateString, temperatureString)
            sqlRespondList.add(dataPoint)

            //println("$dateInt, $temperature") // Only for Display
        }

        // close resources
        data.close()
        statement.close()
        connection.close()

        return (sqlRespondList)
    }

    fun loadOperatingStateSchwachlast(
        operatingState: OperatingConditions
    ): MutableList<DataPoint> {

        val startDate = operatingState.startDate
        val endDate = operatingState.endDate
        val minTemperature = operatingState.schwachlastMinTemperature
        val maxTemperature = operatingState.schwachlastMaxTemperature
        val countryCode = operatingState.countryCode

        //sqlRespondList.clear()
        val sqlRespondList = mutableListOf<DataPoint>()


        // build connection to database
        val connection = DriverManager.getConnection(URL, USER, PASSWORD)

        // create statement
        val statement = connection.createStatement()

        // SQL statement to load rows from database
        val sql = """
        SELECT $countryCode, datum 
        FROM wetterdaten
        WHERE $countryCode > $minTemperature AND $countryCode < $maxTemperature
        AND datum BETWEEN $startDate AND $endDate;
        """.trimIndent()

        // SQL statement execute
        val data = statement.executeQuery(sql)

        // put output rows in to another list
        while (data.next()) {
            val dateInt = data.getInt("datum")
            val temperature = data.getDouble(countryCode)

            val dateString = converter.intToString(dateInt)
            val temperatureString = temperature.toString()

            val dataPoint = DataPoint(dateString, temperatureString)
            sqlRespondList.add(dataPoint)

            //println("$dateInt, $temperature") // Only for Display
        }

        // close resources
        data.close()
        statement.close()
        connection.close()

        return (sqlRespondList)
    }


    fun setValuesToDatabase(save: MutableList<CSV>) {


        // build connection to database
        val connection = DriverManager.getConnection(URL, USER, PASSWORD)

        // create statement
        //val statement = connection.createStatement()


        val sql = """
            INSERT INTO wetterdaten (
                datum, alt, ant, bas, ber, cdf, chd, chm, dav, elm, eng, grc, grh, gsb, gve, jun, lug, luz, mer, neu, otl, pay, rag, sae, sam, sbe, sia, sio, sma, stg
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """

        // Prepare statement
        connection.prepareStatement(sql).use { preparedStatement ->
            try {
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

                // Execute batch insert
                preparedStatement.executeBatch()
            } catch (e: SQLException) {
                e.printStackTrace()
            } finally {
                connection.close()
            }
        }
    }


}