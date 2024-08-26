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

class Database : DatabaseInfo {
    override val PROTOCOL = "jdbc:mysql"
    override val HOST = "localhost"
    override val PORT = 3306
    override val DATABASE = "heimag"
    override val OPTIONS = "useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Europe/Berlin"
    override val URL = "$PROTOCOL://$HOST:$PORT/$DATABASE?$OPTIONS"
    override val USER = "datenanalysetool"
    override val PASSWORD = "HeimAGS2we@!"

    private val logger = LoggerFactory.getLogger("Data.Database")


    fun checkDatabaseStatus(): String {
        var connection: java.sql.Connection? = null
        return try {
            logger.info("Check the status of the database connection.")
            connection = DriverManager.getConnection(URL, USER, PASSWORD)
            logger.info("Connection to the database established.")

            // Make an easy request, to check the connection
            val statement = connection.createStatement()
            val resultSet = statement.executeQuery("SELECT 1")

            if (resultSet.next()) {
                logger.warn("Database is available and accessible.")
                "Datenbank vorhanden"
            } else {
                logger.error("The database could not be accessed.")
                "Datenbank nicht vorhanden"
            }
        } catch (e: SQLException) {
            logger.error("Error when establishing the database connection: ${e.message}", e)
            e.printStackTrace()
            "Datenbank nicht vorhanden"
        } finally {
            connection?.close()
            logger.info("Database connection closed.")
        }
    }


    fun loadLatestDate(): String {
        logger.info("Load the latest date from the database.")
        var latestDateInt = 0

        try {
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
                logger.debug("Latest record in the Database: $latestDateInt")
            } else {
                logger.info("No data records found in the weather data table.")
            }
            data.close()
            statement.close()
            connection.close()

        } catch (e: SQLException) {
            latestDateInt = 0
        }

        val latestDateString = if (latestDateInt == 0) {
            "Keine Daten"
        } else {
            Converter.intToString(latestDateInt)
        }
        logger.info("Latest date in the database: $latestDateString")
        return latestDateString
    }


    fun loadOperatingStateKaltePeriode(
        operatingState: OperatingConditions
    ): MutableList<DataPoint> {
        logger.info("Database query kaltePeriode: (Start date: ${operatingState.startDate}, End date: ${operatingState.endDate}, Countries(as Code): ${operatingState.countryCode}, Min.Temperature: ${operatingState.kaltPeriodeMinTemperature}, Max.Temperature: ${operatingState.kaltPeriodeMaxTemperature}).")

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
            logger.debug("kaltePeriode: SQL-Query: $sql")
            val data = statement.executeQuery(sql)

            // put output rows in to another list
            while (data.next()) {
                val dateInt = data.getInt("datum")
                val temperature = data.getDouble(operatingState.countryCode)

                val dateString = Converter.intToString(dateInt)
                val temperatureString = temperature.toString()

                val dataPoint = DataPoint(dateString, temperatureString)
                sqlRespondList.add(dataPoint)

                logger.debug("kaltePeriode: Datapoint added: Date: $dateString, Temperature: $temperatureString")
            }
            data.close()
            statement.close()
            connection.close()
        } catch (e: SQLException) {
            logger.error("Error loading the data for kaltePeriode: ${e.message}", e)
        }

        return (sqlRespondList)
    }


    fun loadOperatingStateHaupanteilHeizperiode(
        operatingState: OperatingConditions
    ): MutableList<DataPoint> {
        logger.info("Database query HauptanteilHeizperiode: (Start date: ${operatingState.startDate}, End date: ${operatingState.endDate}, Countries(as Code): ${operatingState.countryCode}, Min.Temperature: ${operatingState.hauptanteilHeizperiodeMinTemperature}, Max.Temperature: ${operatingState.hauptanteilHeizperiodeMaxTemperature}).")

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
            logger.debug("HauptanteilHeizperiode: SQL-Query: $sql")
            val data = statement.executeQuery(sql)

            // put output rows in to another list
            while (data.next()) {
                val dateInt = data.getInt("datum")
                val temperature = data.getDouble(operatingState.countryCode)

                val dateString = Converter.intToString(dateInt)
                val temperatureString = temperature.toString()

                val dataPoint = DataPoint(dateString, temperatureString)
                sqlRespondList.add(dataPoint)
                logger.debug("HauptanteilHeizperiode:  Datapoint added: Date: $dateString, Temperature: $temperatureString")
            }
            data.close()
            statement.close()
            connection.close()
        } catch (e: SQLException) {
            logger.error("Error loading the data for HauptanteilHeizperiode: ${e.message}", e)
        }

        return (sqlRespondList)
    }


    fun loadOperatingStateSchwachlast(
        operatingState: OperatingConditions
    ): MutableList<DataPoint> {
        logger.info("Database query Schwachlast: (Start date: ${operatingState.startDate}, End date: ${operatingState.endDate}, Countries(as Code): ${operatingState.countryCode}, Min.Temperature: ${operatingState.kaltPeriodeMinTemperature}, Max.Temperature: ${operatingState.kaltPeriodeMaxTemperature}).")

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

            logger.debug("Schwachlast: SQL-Query: $sql")
            val data = statement.executeQuery(sql)

            // put output rows in to another list
            while (data.next()) {
                val dateInt = data.getInt("datum")
                val temperature = data.getDouble(operatingState.countryCode)

                val dateString = Converter.intToString(dateInt)
                val temperatureString = temperature.toString()

                val dataPoint = DataPoint(dateString, temperatureString)
                sqlRespondList.add(dataPoint)
                logger.debug("Schwachlast: Datapoint added: Date: $dateString, Temperature: $temperatureString")
            }

            data.close()
            statement.close()
            connection.close()
        } catch (e: SQLException) {
            logger.error("Error loading the data for Schwachlast: ${e.message}", e)
        }

        return (sqlRespondList)
    }


    fun setWeatherdataToDatabase(save: MutableList<WeatherData>) {
        logger.info("Save weather data in the database.")

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

                logger.info("Batch insert for weather data is executed.")
                preparedStatement.executeBatch()
            }
            connection.close()
            logger.info("Weather data successfully saved.")

        } catch (e: SQLException) {
            logger.error("Error when saving the weather data: ${e.message}", e)
        }
    }
}


