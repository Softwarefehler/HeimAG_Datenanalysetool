package ch.heimag.datenanalysetool

import ch.heimag.datenanalysetool.*
import ch.heimag.datenanalysetool.plugins.configureRouting
import freemarker.cache.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.freemarker.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
            .start(wait = true)
}

fun Application.module() {
    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
    }
    configureRouting()
}
