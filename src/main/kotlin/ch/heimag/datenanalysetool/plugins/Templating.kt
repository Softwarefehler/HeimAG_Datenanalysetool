package ch.heimag.datenanalysetool.plugins

import freemarker.cache.ClassTemplateLoader
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import org.slf4j.LoggerFactory

fun Application.installFreeMarkerTemplating() {
    val logger = LoggerFactory.getLogger("FreeMarkerTemplating")

    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
    }

    logger.info("FreeMarker-Templating plugin with TemplateLoader configured.")
}
