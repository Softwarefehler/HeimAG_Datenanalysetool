package ch.heimag.datenanalysetool.routes

import io.ktor.server.application.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable


@Serializable
data class Foto(val url: String, val name: String)

fun Application.Datenanalyse(){
routing {
    get("/Datenanalyse"){


    }




}

}