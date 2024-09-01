package ch.heimag.datenanalysetool.databases

import ch.heimag.datenanalysetool.conditions.OperatingConditions
import ch.heimag.datenanalysetool.weatherdata.WeatherData
import org.slf4j.LoggerFactory

object Data {
    private val logger = LoggerFactory.getLogger("Data")

    val database = Database()

    fun databaseStatus(): String {
        logger.info("Checking database status...")
        val databaseStatus = database.checkDatabaseStatus()
        logger.info("Database status: $databaseStatus")
        return databaseStatus
    }


    fun loadLatestDate(): String {
        logger.info("Loading the latest date from the database...")
        val latestDate = database.loadLatestDate()
        logger.info("Latest date loaded: $latestDate")
        return latestDate
    }


    fun loadOperatingStateKaltePeriode(
        operatingState: OperatingConditions
    ): MutableList<DataPoint> {
        logger.info("Loading operating state for 'Kalte Periode' with parameters:  Start date: ${operatingState.startDate}, End date: ${operatingState.endDate}, Countries(as Code): ${operatingState.countryCode}, Min.Temperature: ${operatingState.kaltPeriodeMinTemperature}, Max.Temperature: ${operatingState.kaltPeriodeMaxTemperature}.")
        val valueListKaltPeriode = database.loadOperatingStateKaltePeriode(operatingState)
        logger.info("Operating state 'Kalte Periode' loaded with ${valueListKaltPeriode.size} data points.")
        return valueListKaltPeriode
    }


    fun loadOperatingStateHaupanteilHeizperiode(
        operatingState: OperatingConditions
    ): MutableList<DataPoint> {
        logger.info("Loading operating state for 'Haupanteil Heizperiode' with parameters:  Start date: ${operatingState.startDate}, End date: ${operatingState.endDate}, Countries(as Code): ${operatingState.countryCode}, Min.Temperature: ${operatingState.hauptanteilHeizperiodeMinTemperature}, Max.Temperature: ${operatingState.hauptanteilHeizperiodeMaxTemperature}.")
        val valueListHauptanteilHeizperiode =
            database.loadOperatingStateHaupanteilHeizperiode(operatingState)
        logger.info("Operating state 'Haupanteil Heizperiode' loaded with ${valueListHauptanteilHeizperiode.size} data points.")
        return valueListHauptanteilHeizperiode
    }


    fun loadOperatingStateSchwachlast(
        operatingState: OperatingConditions
    ): MutableList<DataPoint> {
        logger.info("Loading operating state for 'Schwachlast' with parameters:  Start date: ${operatingState.startDate}, End date: ${operatingState.endDate}, Countries(as Code): ${operatingState.countryCode}, Min.Temperature: ${operatingState.schwachlastMinTemperature}, Max.Temperature: ${operatingState.schwachlastMaxTemperature}.")
        val valueListSchwachlast = database.loadOperatingStateSchwachlast(operatingState)
        logger.info("Operating state 'Schwachlast' loaded with ${valueListSchwachlast.size} data points.")
        return valueListSchwachlast
    }


    fun setWeatherdataToDatabase(save: MutableList<WeatherData>): String {
        logger.info("Saving weather data to the database with ${save.size} records...")
        val reply = database.setWeatherdataToDatabase(save)
        logger.info("Saving weather is '$reply'")
        return reply
    }


}