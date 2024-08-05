package ch.heimag.datenanalysetool.routes

import ch.heimag.datenanalysetool.builder.DateBuilder
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable



@Serializable
data class SQLDataTest(val date: String, val temperature: String)


// @Serializable
// data class SQLTest(val testList: MutableList<SQLDataTest>)

//data class searchParam(val startDate: LocalDate, val endDate: LocalDate, val selectedCountry: String)


fun Application.configureDatenanalyse() {
    routing {
        authenticate("auth-session") {
            post("/") {
                var startDateReceive: String? = null
                var endDateReceive: String? = null
                var selectedCountryReceive: String? = null

                //Noch nicht in Benutzung
                //val datenbank = Datenbank()

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
                val dateBuild = DateBuilder()
                val startDate = dateBuild.buildDate(startDateString)
                val endDate = dateBuild.buildDate(endDateString)

                ////Noch nicht in Benutzung
                //val sqlResponse = datenbank.loadValuesInRange(startDate, endDate, selectedCountry)

                val testList = mutableListOf<SQLDataTest>()
                // Zum Testen des Frontends
                val startDateTestString = startDate.toString()
                val endDateTestString = endDate.toString()
                val nullDate = startDate.toString()
                val temperature1 = "39.8"
                val temperature2 = "38.8"
                val temperature3 = "37.8"
                val newtestList1 = SQLDataTest(startDateTestString, temperature1)
                testList.add(newtestList1)
                val newtestList2 = SQLDataTest(endDateTestString, temperature2)
                testList.add(newtestList2)
                val newtestList3 = SQLDataTest(nullDate, temperature3)
                testList.add(newtestList3)
                val sqlResponse = testList






                // Debug-Ausgabe
                println("Start Date: $startDate")
                println("End Date: $endDate")
                println("Selected Country: $selectedCountryReceive,$selectedCountry")
                println(sqlResponse)

                // Sende eine Antwort zurück
                // call.respondText("Data received: startDate=$startDateReceive, endDate=$endDateReceive, selectedCountry=$selectedCountryReceive")
                call.respond(sqlResponse)

            }

        }

    }
}