package ch.heimag.datenanalysetool.plugins


import ch.heimag.datenanalysetool.routes.singlePageApplication
import ch.heimag.datenanalysetool.routes.webPageApplication
import io.ktor.server.application.*

fun Application.configureRouting() {
    singlePageApplication()
    webPageApplication()
}


