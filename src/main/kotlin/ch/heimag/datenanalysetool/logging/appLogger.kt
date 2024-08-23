package ch.heimag.datenanalysetool.logging

import org.slf4j.Logger
import org.slf4j.LoggerFactory

object AppLogger {
    val logger: Logger = LoggerFactory.getLogger(AppLogger::class.java)

    fun info(message: String) {
        logger.info(message)
    }

    fun debug(message: String) {
        logger.debug(message)
    }

    fun error(message: String, throwable: Throwable? = null) {
        logger.error(message, throwable)
    }

    // Weitere Logging-Methoden kannst du hier hinzufügen
}