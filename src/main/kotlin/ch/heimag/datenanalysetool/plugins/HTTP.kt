package ch.heimag.datenanalysetool.plugins

import io.ktor.network.tls.certificates.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.plugins.forwardedheaders.*
import io.ktor.server.plugins.httpsredirect.*
import java.io.File
import org.slf4j.LoggerFactory

fun Application.installHttpsRedirect(port: Int) {
    val logger = LoggerFactory.getLogger("HttpsRedirect")
    install(HttpsRedirect) {
        sslPort = port
        permanentRedirect = false
    }
    logger.info("HTTPS Redirect installiert auf Port $port.")
}

fun Application.installForwardedHeaders() {
    val logger = LoggerFactory.getLogger("ForwardedHeaders")
    install(ForwardedHeaders)
    install(XForwardedHeaders)
    logger.info("Forwarded Headers und XForwardedHeaders Plugins installiert.")
}

fun ApplicationEngineEnvironmentBuilder.configureHttps(hostBinding: String, port: Int) {
    val logger = LoggerFactory.getLogger("ConfigureHttps")
    val keyStoreFile = File("build/keystore.jks")
    val keyStore = buildKeyStore {
        certificate("sampleAlias") {
            password = "foobar"
            domains = listOf("127.0.0.1", hostBinding, "localhost")
        }
    }
    keyStore.saveToFile(keyStoreFile, "123456")

    logger.info("Keystore erstellt und unter ${keyStoreFile.absolutePath} gespeichert.")


    sslConnector(
        keyStore = keyStore,
        keyAlias = "sampleAlias",
        keyStorePassword = { "123456".toCharArray() },
        privateKeyPassword = { "foobar".toCharArray() }) {
        this.port = port
        keyStorePath = keyStoreFile
    }
    logger.info("SSL Connector konfiguriert auf Port $port.")
}

fun ApplicationEngineEnvironmentBuilder.configureHttp(hostBinding: String, port: Int) {
    val logger = LoggerFactory.getLogger("ConfigureHttp")
    connector {
        host = hostBinding
        this.port = port
    }
    logger.info("HTTP Connector konfiguriert auf Host $hostBinding und Port $port.")
}
