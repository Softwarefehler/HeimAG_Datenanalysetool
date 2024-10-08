package ch.heimag.datenanalysetool

import org.slf4j.LoggerFactory

import ch.heimag.datenanalysetool.plugins.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.coroutineScope


suspend fun main() {
    val logger = LoggerFactory.getLogger("Application")

    val enableHttps = System.getenv("DISABLE_HTTPS") != "true"
    val httpPort = System.getenv("HTTP_PORT")?.toIntOrNull() ?: 8080
    val httpsPort = System.getenv("HTTPS_PORT")?.toIntOrNull() ?: 8443
    val hostBinding = System.getenv("HOST_BINDING") ?: "0.0.0.0"

    logger.info("Application starting with HTTP port: $httpPort and HTTPS port: $httpsPort")
    logger.debug("Enable HTTPS: $enableHttps")


    coroutineScope {
        val appEnginEnv = applicationEngineEnvironment {
            module {
                installKoinDependencyInjection()

                if (enableHttps) {
                    installHttpsRedirect(httpsPort)
                }

                installForwardedHeaders()
                installSessionAndAuthentication()
                installFreeMarkerTemplating()
                installSerialization()
                configureRouting()
            }

            configureHttp(hostBinding, httpPort)

            if (enableHttps) {
                configureHttps(hostBinding, httpsPort)
            }
            watchPaths = listOf("classes", "resources")
        }

        embeddedServer(Netty, appEnginEnv)
            .start(wait = true)
    }
}

