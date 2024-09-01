package ch.heimag.datenanalysetool.routes

import ch.heimag.datenanalysetool.plugins.UserSession
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.slf4j.LoggerFactory


@Serializable
data class UserInfo(val username: String)

fun Application.singlePageApplication() {
    val logger = LoggerFactory.getLogger("SinglePageApplication")

    routing {
        authenticate("auth-session") {
            singlePageApplication { vue("src/main/vue-project/dist") }


            get("/user-info") {
                val principal = call.principal<UserSession>()
                val userName = principal?.name.toString()

                logger.info("Request for user informationen for user $userName")

                call.respond(HttpStatusCode.OK, UserInfo(userName))
            }
        }
    }
}