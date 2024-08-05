package ch.heimag.datenanalysetool.databases

import java.sql.DriverManager
import java.time.LocalDate
import java.util.Date

data class DataPoint(val date:Date?, val temerature: Double?)


class Datenbank : DatenbankInfo {
    override val PROTOCOL = "jdbc:mysql"
    override val HOST = "localhost"
    override val PORT = 3306
    override val DATABASE = "heimag"
    override val OPTIONS = "useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Europe/Berlin"
    override val URL = "$PROTOCOL://$HOST:$PORT/$DATABASE?$OPTIONS"
    override val USER = "datenanalysetool"
    override val PASSWORD = "HeimAGS2we@!"


    fun loadValuesInRange(
        startDate: LocalDate,
        endDate: LocalDate,
        selectedCountry: String
    ): MutableList<DataPoint> {


        val sqlRespondList = mutableListOf<DataPoint>()
        sqlRespondList.clear()

        // build connection to database
        val connection = DriverManager.getConnection(URL, USER, PASSWORD)

        // create statement
        val statement = connection.createStatement()

        // SQL statement to load rows from database
        val sql = "SELECT * FROM processdata WHERE timeevent between  '$startDate' and '$endDate'"

        // SQL statement execute
        val data = statement.executeQuery(sql)

        // put output rows in to another list
        while (data.next()) {
            val date = data.getDate("Date")
            val temperature = data.getDouble("temperature")

            println("$date, $temperature") // Only for Display

            val dataPoint = DataPoint(date,temperature)
            sqlRespondList.add(dataPoint)
        }

        // close resources
        data.close()
        statement.close()
        connection.close()

        return(sqlRespondList)
    }
}
/*
fun checkLoginInformation(loginInformation: LoginInformation): vorhandeneBenutzer {
    var passwort = loginInformation.passwortWebseite


    val listeVorhandenerBenutzer = mutableListOf<String>()


    // build connection to database
    val connection = DriverManager.getConnection(URL, USER, PASSWORD)

    // create statement
    val statement = connection.createStatement()

    // SQL statement to load rows from database
    val sql = "SELECT benutzer from heimag.login where passwort = '$passwort'"

    // SQL execute
    val data = statement.executeQuery(sql)

    // Output Message
    while (data.next()) {
        listeVorhandenerBenutzer.add(data.getString("benutzer"))
    }

    data.close()
    statement.close()
    connection.close()

    vorhandeneBenutzer.benutzer = listeVorhandenerBenutzer

    return vorhandeneBenutzer
}

*/
