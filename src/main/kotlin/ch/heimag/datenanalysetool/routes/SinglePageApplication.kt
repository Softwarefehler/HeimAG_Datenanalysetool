package ch.heimag.datenanalysetool.routes

import ch.heimag.datenanalysetool.builder.DateBuilder
import ch.heimag.datenanalysetool.plugins.UserSession
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(val username: String)
data class seachParam(val startDate: String, val endDate: String, val country: String)


fun Application.singlePageApplication() {
    routing {
        authenticate("auth-session") {
            singlePageApplication { vue("src/main/vue-project/dist") }




            get("/user-info") {
                val principal = call.principal<UserSession>();
                val userName = principal?.name.toString()
                call.respond(HttpStatusCode.OK, UserInfo(userName))
            }

            post("/") {
                val multipart = call.receiveMultipart()
                var startDateReceive: String? = null
                var endDateReceive: String? = null
                var selectedCountryReceive: String? = null

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
                val selectedCountryString = selectedCountryReceive.toString() ?: "colorado"

                // Parse Daten
               val dateBuild = DateBuilder()
                val startDate = dateBuild.buildDate(startDateString)
                val endDate = dateBuild.buildDate(endDateString)


                // Debug-Ausgabe
                println("Start Date: $startDate")
                println("End Date: $endDate")
                println("Selected Country: $selectedCountryReceive,$selectedCountryString")


                // Sende eine Antwort zurück
                call.respondText("Data received: startDate=$startDateReceive, endDate=$endDateReceive, selectedCountry=$selectedCountryReceive")
            }

        }
    }
}