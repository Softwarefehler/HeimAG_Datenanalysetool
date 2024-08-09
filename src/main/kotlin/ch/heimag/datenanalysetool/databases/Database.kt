package ch.heimag.datenanalysetool.databases

import ch.heimag.datenanalysetool.converter.converter
import kotlinx.serialization.Serializable
import java.sql.DriverManager
import java.sql.SQLException

@Serializable
data class DataPoint(val date: String, val temperature: String)

class Datenbank {
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
        val sql = "SELECT * FROM merged_output ORDER BY datum DESC LIMIT 1"

        // SQL statement execute
        val data = statement.executeQuery(sql)

        // Output rows
        if (data.next()) {
            latestDateInt = data.getInt("datum")

        }
        data.close()
        statement.close()
        connection.close()

        val latestDateString = converter.intToString(latestDateInt)

        return latestDateString
    }


    fun loadValuesInRange(
        startDate: Int,
        endDate: Int,
        selectedCountry: String
    ): MutableList<DataPoint> {

        val starttemperature = 0.0
        val endtemperature = 10.0

        //sqlRespondList.clear()
        val sqlRespondList = mutableListOf<DataPoint>()


        // build connection to database
        val connection = DriverManager.getConnection(URL, USER, PASSWORD)

        // create statement
        val statement = connection.createStatement()

        // SQL statement to load rows from database
        val sql = """
        SELECT $selectedCountry, datum 
        FROM merged_output
        WHERE $selectedCountry > $starttemperature AND $selectedCountry < $endtemperature
        AND datum BETWEEN $startDate AND $endDate;
        """.trimIndent()

        // SQL statement execute
        val data = statement.executeQuery(sql)

        // put output rows in to another list
        while (data.next()) {
            val dateInt = data.getInt("datum")
            val temperature = data.getDouble(selectedCountry)

            val dateString = converter.intToString(dateInt)
            val temperatureString =temperature.toString()

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
}
