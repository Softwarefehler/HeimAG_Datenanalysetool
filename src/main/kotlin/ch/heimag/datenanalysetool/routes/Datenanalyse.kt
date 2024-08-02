package ch.heimag.datenanalysetool.routes

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable


@Serializable
data class search(val date: String, val temp: String)

fun Application.configureDatenanalyse() {
    routing {
        authenticate("auth-session") {
            get("/datenanalyse") {


            }


        }

    }
}