package ch.heimag.datenanalysetool.routes

import ch.heimag.datenanalysetool.conditions.OperatingConditions
import ch.heimag.datenanalysetool.converter.Converter
import ch.heimag.datenanalysetool.weatherdata.WeatherData
import ch.heimag.datenanalysetool.databases.Data
import ch.heimag.datenanalysetool.databases.DataPoint
import ch.heimag.datenanalysetool.country.FileReader
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.slf4j.LoggerFactory
import java.io.InputStreamReader
import java.io.BufferedReader
import java.io.File


@Serializable
data class FirstResponseDatenanalyseView(
    val countryList: MutableList<String>,
)

@Serializable
data class FirstResponseSettingsView(
    val latestDate: String
)

@Serializable
data class OperatingStateValueList(
    val kaltPeriode: MutableList<DataPoint>,
    val hauptanteilHeizperiode: MutableList<DataPoint>,
    val schwachlast: MutableList<DataPoint>,
)

const val IMAGE_DIRECTORY = "src/main/resources/images"


fun Application.webPageApplication() {
    val logger = LoggerFactory.getLogger("WebPageApplication")

    routing {
        authenticate("auth-session") {

            // First steps, wenn you start de routing
            val databaseStatus = Data.database.checkDatabaseStatus()
            var latestDateString = if (databaseStatus == "Datenbank vorhanden") {
                Data.database.loadLatestDate()
            } else {
                "Datenbank nicht vorhanden"
            }

            val countryList = FileReader.country.loadToSelectCountry("/document/Wetterstationen.xlsx")


            get("/datenanalyseView") {
                logger.info("GET request from /datenanalyseView")

                val firstResponse = FirstResponseDatenanalyseView(
                    countryList = countryList,
                )
                logger.debug(
                    "DatenanalyseView response: {}",
                    firstResponse.countryList
                )
                call.respond(firstResponse)
            }


            // Search Date and Temperature
            post("/search") {
                logger.info("POST request for /search")

                // Init
                var startDateReceive: String? = null
                var endDateReceive: String? = null
                var selectedCountryReceive: String? = null

                // Receive data
                val multipart = call.receiveMultipart()
                multipart.forEachPart { part ->
                    when (part) {
                        is PartData.FormItem -> {
                            when (part.name) {
                                "startDate" -> startDateReceive = part.value
                                "endDate" -> endDateReceive = part.value
                                "selectedCountry" -> selectedCountryReceive = part.value
                            }
                        }

                        else -> {}
                    }
                    part.dispose()
                }

                val startDateString = startDateReceive.toString()
                val endDateString = endDateReceive.toString()
                val selectedCountry = selectedCountryReceive.toString()

                logger.debug("Received Data - Start date: $startDateString, End date: $endDateString, Selected country: $selectedCountry")

                // Parse Data
                val startDate = Converter.databaseDateStringToInt(startDateString)
                val endDate = Converter.databaseDateStringToInt(endDateString)

                // Group data into an object
                val operatingState = OperatingConditions(startDate, endDate, selectedCountry)
                logger.debug("Search parameters: {}", operatingState)

                // Database request for every period
                val valueListKaltPeriode = Data.database.loadOperatingStateKaltePeriode(operatingState)
                val valueListHauptanteilHeizperiode =
                    Data.database.loadOperatingStateHaupanteilHeizperiode(operatingState)
                val valueListSchwachlast = Data.database.loadOperatingStateSchwachlast(operatingState)

                // Group every List to one Value
                val operatingStateValueList =
                    OperatingStateValueList(valueListKaltPeriode, valueListHauptanteilHeizperiode, valueListSchwachlast)

                logger.debug("Calculated country code: ${operatingState.countryCode}")

                call.respond(operatingStateValueList)
            }


            post("/csv-upload") {
                logger.info("POST request for /csv-upload")

                val multipart = call.receiveMultipart()
                val csvRecords = mutableListOf<WeatherData>()

                //Init
                val actualDateInt = 18000101 // 01.01.1800
                val latestDateCheck = if (latestDateString == "Keine Daten") {
                    actualDateInt
                } else {
                    Converter.dateStringToInt(latestDateString)
                }

                // Receive data
                multipart.forEachPart { part ->
                    if (part is PartData.FileItem) {
                        val reader = BufferedReader(InputStreamReader(part.streamProvider()))
                        reader.useLines { lines ->
                            val iterator = lines.iterator()

                            if (iterator.hasNext()) {
                                iterator.next() // Skip the header line
                            }

                            // Group all data from a csv-line to an object
                            iterator.forEach { line ->
                                val values = line.split(';')
                                if (values.size == 30) {
                                    val record = WeatherData(
                                        datum = values[0].toIntOrNull(),
                                        alt = values[1].toDoubleOrNull(),
                                        ant = values[2].toDoubleOrNull(),
                                        bas = values[3].toDoubleOrNull(),
                                        ber = values[4].toDoubleOrNull(),
                                        cdf = values[5].toDoubleOrNull(),
                                        chd = values[6].toDoubleOrNull(),
                                        chm = values[7].toDoubleOrNull(),
                                        dav = values[8].toDoubleOrNull(),
                                        elm = values[9].toDoubleOrNull(),
                                        eng = values[10].toDoubleOrNull(),
                                        grc = values[11].toDoubleOrNull(),
                                        grh = values[12].toDoubleOrNull(),
                                        gsb = values[13].toDoubleOrNull(),
                                        gve = values[14].toDoubleOrNull(),
                                        jun = values[15].toDoubleOrNull(),
                                        lug = values[16].toDoubleOrNull(),
                                        luz = values[17].toDoubleOrNull(),
                                        mer = values[18].toDoubleOrNull(),
                                        neu = values[19].toDoubleOrNull(),
                                        otl = values[20].toDoubleOrNull(),
                                        pay = values[21].toDoubleOrNull(),
                                        rag = values[22].toDoubleOrNull(),
                                        sae = values[23].toDoubleOrNull(),
                                        sam = values[24].toDoubleOrNull(),
                                        sbe = values[25].toDoubleOrNull(),
                                        sia = values[26].toDoubleOrNull(),
                                        sio = values[27].toDoubleOrNull(),
                                        sma = values[28].toDoubleOrNull(),
                                        stg = values[29].toDoubleOrNull()
                                    )
                                    // Check date from database with date from object and add it to a list
                                    if (record.datum != null && latestDateCheck < record.datum) {
                                        csvRecords.add(record)
                                    }
                                }
                            }
                        }
                        logger.debug("csv-File processed, Size of data records: ${csvRecords.size}")
                    }
                    part.dispose()
                }

                // transfer the List to the database
                Data.database.setWeatherdataToDatabase(csvRecords)
                latestDateString = Data.database.loadLatestDate()
                logger.info("Database updated, newest date: $latestDateString")

                call.respond(
                    HttpStatusCode.OK,
                    mapOf("message" to "File successfully processed. records count: ${csvRecords.size}")
                )
            }


            get("/settingsView") {
                logger.info("GET request from /settingsView")

                val firstResponse = FirstResponseSettingsView(
                    latestDate = latestDateString
                )
                logger.debug(
                    "SettingsView response: {},{}",
                    firstResponse.latestDate,
                    firstResponse.latestDate
                )
                call.respond(firstResponse)
            }


            get("/swisstopographic") {
                logger.info("GET request from /swisstopographic")

                val imageName = "Switzerland_topographic.png"
                val file = File("$IMAGE_DIRECTORY/$imageName")
                if (file.exists()) {
                    logger.debug("Picture $imageName found, send data.")
                    call.respondFile(file)
                } else {
                    logger.error("Picture $imageName not found, send data.")
                    call.respond(HttpStatusCode.NotFound, "Picture not found")
                }
            }
        }
    }
}






