package ch.heimag.datenanalysetool.plugins

import ch.heimag.datenanalysetool.services.*
import io.ktor.server.application.*
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.slf4j.LoggerFactory

// https://insert-koin.io/
var defaultKoinModule = module {
    singleOf(::AuthenticationService)
}

fun Application.installKoinDependencyInjection() {
    val logger = LoggerFactory.getLogger("DependencyInjection")
    install(Koin) {
        modules(defaultKoinModule)
        logger.info("Koin Dependency Injection install with Standardmodule: defaultKoinModule.")
    }
}
