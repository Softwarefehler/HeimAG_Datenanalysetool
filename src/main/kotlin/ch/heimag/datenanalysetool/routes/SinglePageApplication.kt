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
import java.time.LocalDate
import java.util.*

@Serializable
data class UserInfo(val username: String)

fun Application.singlePageApplication() {
    routing {
        authenticate("auth-session") {
            singlePageApplication { vue("src/main/vue-project/dist") }




            get("/user-info") {
                val principal = call.principal<UserSession>();
                val userName = principal?.name.toString()
                call.respond(HttpStatusCode.OK, UserInfo(userName))
            }



        }
    }
}