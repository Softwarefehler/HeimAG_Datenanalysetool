package ch.heimag.datenanalysetool.routes

import ch.heimag.datenanalysetool.conditions.OperatingConditions
import ch.heimag.datenanalysetool.converter.converter

import ch.heimag.datenanalysetool.file.CSV
import ch.heimag.datenanalysetool.databases.Data
import ch.heimag.datenanalysetool.databases.DataPoint
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.InputStream
import java.io.InputStreamReader
import java.io.BufferedReader
import java.io.File


@Serializable
data class FirstResponseDatenanalyseView(
    val databaseStatus: String,
    val countryList: MutableList<String>,
    val latestDate: String
)

@Serializable
data class FirstResponseSettingsView(
    val databaseStatus: String,
    val latestDate: String
)

@Serializable
data class OperatingStateValueList(
    val kaltPeriode: MutableList<DataPoint>,
    val hauptanteilHeizperiode: MutableList<DataPoint>,
    val schwachlast: MutableList<DataPoint>,
)

const val IMAGE_DIRECTORY = "src/main/resources/images"


fun Application.configureDatenanalyse() {
    routing {
        authenticate("auth-session") {

            val databaseStatus = Data.database.checkDatabaseStatus()

            var latestDateString = Data.database.loadLatestDate()


            get("/get-DatenanalyseView") {


                var countryList = loadToSelectCountry("/document/Wetterstationen.xlsx")


                val firstResponse = FirstResponseDatenanalyseView(
                    databaseStatus = databaseStatus,
                    countryList = countryList,
                    latestDate = latestDateString
                )

                call.respond(firstResponse)

            }


            post("/search") {
                var startDateReceive: String? = null
                var endDateReceive: String? = null
                var selectedCountryReceive: String? = null


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

                val startDateString = startDateReceive.toString() ?: "1990.02.22"
                val endDateString = endDateReceive.toString() ?: "1990.02.22"
                val selectedCountry = selectedCountryReceive.toString() ?: "colorado"

                // Parse Daten
                val startDate = converter.frontendDateStringToInt(startDateString)
                val endDate = converter.frontendDateStringToInt(endDateString)

                var operatingState = OperatingConditions(startDate, endDate, selectedCountry)

                val valueListKaltPeriode = Data.database.loadOperatingStateKaltePeriode(operatingState)
                val valueListHauptanteilHeizperiode =
                    Data.database.loadOperatingStateHaupanteilHeizperiode(operatingState)
                val valueListSchwachlast = Data.database.loadOperatingStateSchwachlast(operatingState)

                val operatingStateValueList =
                    OperatingStateValueList(valueListKaltPeriode, valueListHauptanteilHeizperiode, valueListSchwachlast)
                // Debug-Ausgabe
                println("Input: Start Date: $startDate")
                println("Input: End Date: $endDate")
                println("Input: Selected Country: $selectedCountryReceive")
                println("Input: CountryCode: ${operatingState.countryCode}")

                call.respond(operatingStateValueList)
            }

            post("/csv-upload") {
                val multipart = call.receiveMultipart()
                val csvRecords = mutableListOf<CSV>()

                val oldDate = 18000101
                val latestDateCheck = if (latestDateString == "Keine Daten") {
                    oldDate
                } else {
                    converter.dateStringToInt(latestDateString)
                }

                multipart.forEachPart { part ->
                    if (part is PartData.FileItem) {
                        val reader = BufferedReader(InputStreamReader(part.streamProvider()))
                        reader.useLines { lines ->
                            val iterator = lines.iterator()

                            if (iterator.hasNext()) {
                                iterator.next() // Skip the header line
                            }

                            iterator.forEach { line ->
                                val values = line.split(';')
                                if (values.size == 30) { // Ensure it matches the number of columns
                                    val record = CSV(
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
                                    if (record.datum != null && latestDateCheck < record.datum) {
                                        csvRecords.add(record)
                                    }

                                }
                            }
                        }

                    }
                    part.dispose()

                }

                Data.database.setValuesToDatabase(csvRecords)
                latestDateString = Data.database.loadLatestDate()
                call.respond(
                    HttpStatusCode.OK,
                    mapOf("message" to "File successfully processed. Records count: ${csvRecords.size}")
                )

            }

            get("/get-SettingsView") {


                val firstResponse = FirstResponseSettingsView(
                    databaseStatus = databaseStatus,
                    latestDate = latestDateString
                )

                call.respond(firstResponse)

            }

            get("/swisstopographic") {
                val imageName = "Switzerland_topographic.png"
                val file = File("$IMAGE_DIRECTORY/$imageName")
                if (file.exists()) {
                    call.respondFile(file)
                } else {
                    call.respond(HttpStatusCode.NotFound, "Bild nicht gefunden")
                }
            }
        }
    }
}


fun loadToSelectCountry(resourcePath: String): MutableList<String> {
    val countryList = mutableListOf<String>()

    // Datei aus den Ressourcen laden
    val inputStream: InputStream? = object {}.javaClass.getResourceAsStream(resourcePath)
    if (inputStream != null) {
        val workbook = XSSFWorkbook(inputStream)
        val sheet = workbook.getSheetAt(0) // Die erste Tabelle im Dokument

        // Alle Zeilen durchlaufen
        for (row in sheet) {
            val cell = row.getCell(0) // Erste Zelle in der Zeile (Spalte A)
            if (cell != null) {
                val countryName = cell.stringCellValue
                countryList.add(countryName)
            }
        }
        workbook.close()
    }
    println(countryList)
    return countryList
}


