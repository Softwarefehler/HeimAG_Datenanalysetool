package ch.heimag.datenanalysetool.services

import io.ktor.server.auth.*
import io.ktor.util.*
import org.slf4j.LoggerFactory


class AuthenticationService {
    private val logger = LoggerFactory.getLogger("AuthenticationService")

    private val digestFunction = getDigestFunction("SHA-256") { "ktor${it.length}" }
    private val usernameToPasswordMap = mutableMapOf<String, ByteArray>()

    init {
        usernameToPasswordMap["heimag"] = digestFunction("QM8Holz")
        usernameToPasswordMap["admin"] = digestFunction("NDSsa2Renato")

        logger.info("AuthenticationService initialize with ${usernameToPasswordMap.size} user.")
    }


    fun authenticate(credential: UserPasswordCredential): UserIdPrincipal? {
        val username = credential.name
        val password = credential.password
        val isAuthenticated =
            usernameToPasswordMap.containsKey(username) && usernameToPasswordMap[username].contentEquals(
                digestFunction(password)
            )

        if (isAuthenticated) {
            logger.info("Authentication successful for user $username")
            return UserIdPrincipal(username)
        } else {
            logger.warn("Authentication failed for user $username")
            return null
        }
    }
}
