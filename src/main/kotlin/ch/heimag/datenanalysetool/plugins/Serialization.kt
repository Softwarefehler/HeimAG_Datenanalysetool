package ch.heimag.datenanalysetool.plugins

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import org.slf4j.LoggerFactory

fun Application.installSerialization() {
    val logger = LoggerFactory.getLogger("Serialization")

    install(ContentNegotiation) {
        json()
    }

    logger.info("ContentNegotiation-Plugin mit JSON-Serialisierung installiert.")
}
