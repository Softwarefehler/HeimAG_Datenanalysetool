package ch.heimag.datenanalysetool.plugins

import ch.heimag.datenanalysetool.routes.singlePageApplication
import ch.heimag.datenanalysetool.routes.webPageApplication
import io.ktor.server.application.*
import org.slf4j.LoggerFactory

fun Application.configureRouting() {
    val logger = LoggerFactory.getLogger("HttpsRedirect")
    singlePageApplication()
    webPageApplication()

    logger.info("configure the routes for single-page-application and webpage-application ")
}


