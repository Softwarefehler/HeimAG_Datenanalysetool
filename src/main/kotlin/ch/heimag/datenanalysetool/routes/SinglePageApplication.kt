package ch.heimag.datenanalysetool.routes

import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*

fun Application.SinglePageApplication() {
  routing {
    singlePageApplication { vue("src/main/vue-project/dist") }
  }
}
