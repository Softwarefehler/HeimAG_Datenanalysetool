package ch.heimag.datenanalysetool.routes

import ch.heimag.datenanalysetool.converter.converter
import ch.heimag.datenanalysetool.databases.Datenbank
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable


@Serializable
data class SQLDataTest(val date: String, val temperature: String)

@Serializable
data class FirstResponse(
    val databaseStatus: String,
    val countryList: MutableList<String>,
    val latestDate: String
)

fun Application.configureDatenanalyse() {
    routing {
        authenticate("auth-session") {
            //Noch nicht in Benutzung
            val datenbank = Datenbank()

            get("/get-data") {
                /*
                var countryList = mutableListOf("USA","Europa","Japan")
                var latestDate = "2021-08-31"

                val databaseStatus = datenbank.checkDatabaseStatus()
                val firstResponse = FirstResponse(
                    databaseStatus = databaseStatus,
                    countryList = countryList.toMutableList(),
                    latestDate = latestDate
                )*/

                var countryList = mutableListOf("-")
                var latestDate = "-"
                val databaseStatus = datenbank.checkDatabaseStatus()

                if (databaseStatus == "Datenbank vorhanden") {
                    countryList = loadToSelectCountry()
                     latestDate = datenbank.loadLatestDate()

                }
                val firstResponse = FirstResponse(
                    databaseStatus = databaseStatus,
                    countryList = countryList.toMutableList(),
                    latestDate = latestDate)


                //println("${firstResponse.countryList},${firstResponse.latestDate},${firstResponse.databaseStatus}")

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
                val startDate = converter.stringToInt(startDateString)
                val endDate = converter.stringToInt(endDateString)


                ////Noch nicht in Benutzung
                // val sqlResponse1 = datenbank.loadValuesInRange(startDate, endDate, selectedCountry)

                val testList = mutableListOf<SQLDataTest>()
                // Zum Testen des Frontends
                val startDateTestString = "1990.02.26"
                val endDateTestString = "2024.08.06"
                val nullDate = "1886.09.12"
                val temperature1 = "39.8"
                val temperature2 = "38.8"
                val temperature3 = "37.8"
                val newtestList1 = SQLDataTest(startDateTestString, temperature1)
                testList.add(newtestList1)
                val newtestList2 = SQLDataTest(endDateTestString, temperature2)
                testList.add(newtestList2)
                val newtestList3 = SQLDataTest(nullDate, temperature3)
                testList.add(newtestList3)


                // Debug-Ausgabe
                println("Start Date: $startDate")
                println("End Date: $endDate")
                println("Selected Country: $selectedCountryReceive,$selectedCountry")
                println(testList)

                // Sende eine Antwort zurück
                // call.respondText("Data received: startDate=$startDateReceive, endDate=$endDateReceive, selectedCountry=$selectedCountryReceive")
                call.respond(testList)

            }

        }

    }
}


fun loadToSelectCountry(): MutableList<String> {

    val OW = "Obwalden"
    val THR = "Thurgau"
    val LU = "Luzern"
    val countryList = mutableListOf(OW, THR, LU)

    return countryList
}