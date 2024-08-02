package ch.heimag.datenanalysetool.routes

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


fun Application.SinglePageApplication() {
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
                var startDate: String? = null
                var endDate: String? = null
                var selectedCountry: String? = null

                multipart.forEachPart { part ->
                    when (part) {
                        is PartData.FormItem -> {
                            when (part.name) {
                                "startDate" -> startDate = part.value
                                "endDate" -> endDate = part.value
                                "selectedCountry" -> selectedCountry = part.value
                            }
                        }
                        is PartData.FileItem -> {
                            // Handle file items if necessary
                        }
                        else -> {}
                    }
                    part.dispose()
                }

                // Debug-Ausgabe
                println("Start Date: $startDate")
                println("End Date: $endDate")
                println("Selected Country: $selectedCountry")

                // Sende eine Antwort zurück
                call.respondText("Data received: startDate=$startDate, endDate=$endDate, selectedCountry=$selectedCountry")
            }

        }
    }
}