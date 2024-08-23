package ch.heimag.datenanalysetool.plugins

import ch.heimag.datenanalysetool.databases.Data
import ch.heimag.datenanalysetool.services.AuthenticationService
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.freemarker.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import org.koin.ktor.ext.inject
import org.slf4j.LoggerFactory

data class UserSession(val name: String) : Principal


const val LOGIN_URL = "/login"
const val USER_PARAM_NAME = "username"
const val PASSWORD_PARAM_NAME = "password"

var databaseStatus = Data.database.checkDatabaseStatus()


fun Application.installSessionAndAuthentication() {
    val logger = LoggerFactory.getLogger("SessionAndAuthentication")
    val authenticationService by inject<AuthenticationService>()

    install(Sessions) {
        cookie<UserSession>("user_session") {
            cookie.path = "/"
            cookie.maxAgeInSeconds = 30 // 300
            logger.info("UserSession cookie configured mit ${cookie.path} und max. Zeit von ${cookie.maxAgeInSeconds}")
        }
    }

    install(Authentication) {
        form("auth-form") {
            userParamName = USER_PARAM_NAME
            passwordParamName = PASSWORD_PARAM_NAME
            validate { credential ->
                logger.debug("Validating user with username: ${credential.name}")
                authenticationService.authenticate(credential)
            }
        }
        session<UserSession>("auth-session") {
            validate { session ->
                logger.debug("Validating session for user: ${session.name}")
                session
            }
            challenge {
                logger.warn("Session validation failed. Redirecting to login page.")
                call.respondRedirect(LOGIN_URL)
            }
            logger.info("Session-based authentication installed.")
        }
    }


    data class LoginModel(
        val postUrl: String,
        val userParamName: String,
        val passwordParamName: String,
        var databaseStatus: String
    )

    routing {
        get(LOGIN_URL) {
            logger.info("Handling GET request to $LOGIN_URL. Clearing any existing session.")

            call.sessions.clear<UserSession>()
            val loginModel = LoginModel(LOGIN_URL, USER_PARAM_NAME, PASSWORD_PARAM_NAME, databaseStatus)
            call.respond(FreeMarkerContent("login.ftl", mapOf("login" to loginModel)))
            logger.debug("Antworte mit FreeMarkerContent Template: login.ftl .")
        }

        staticResources("/images", "images")
        logger.info("Bedienung von statischer Resource unter /images.")

        authenticate("auth-form") {
            post(LOGIN_URL) {
                logger.info(" POST anftage zu $LOGIN_URL für Benutzer Authentifikation.")

                val principal = call.principal<UserIdPrincipal>()
                val userName = principal?.name.toString()
                val userSession = UserSession(name = userName)
                call.sessions.set(userSession)
                logger.debug("Session created for user: $userName")

                if (databaseStatus == "Datenbank vorhanden") {
                    logger.info("Status Datenbank: ${databaseStatus} -> Weiterleitung an '/' WebPageApplication")
                    call.respondRedirect("/")
                } else {
                    logger.warn("Status Datenbank: ${databaseStatus} ->  lösche Session und rückkehr zu Fremmarkertemplate unter $LOGIN_URL.")
                    call.sessions.clear<UserSession>()
                    call.respondRedirect(LOGIN_URL)
                }
            }


            get("/logout") {
                logger.info("Get logout Anfrage. löscht Benutzer Session.")
                call.sessions.clear<UserSession>()
                call.respondRedirect(LOGIN_URL)
            }
        }
    }
}



