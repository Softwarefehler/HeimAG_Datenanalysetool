package ch.heimag.datenanalysetool.plugins

import ch.heimag.datenanalysetool.routes.Datenanalyse
import ch.heimag.datenanalysetool.routes.SinglePageApplication
import io.ktor.server.application.*

fun Application.configureRouting() {
   SinglePageApplication()
    Datenanalyse()




  /*  routing {
        singlePageApplication {vue("src/main/vue-project/dist")}

        get("/") { call.respond(FreeMarkerContent("index.ftl", null)) }

        get("/user") {
            val sampleUser = User(1, "Garfield")
            call.respond(FreeMarkerContent("user.ftl", mapOf("user" to sampleUser)))
        }

        get("/all-users") {
            val garfield = User(1, "Garfield")
            val john = User(2, "John")
            val odie = User(3, "Odie")
            val users = listOf(garfield, john, odie)
            val templateModel = mapOf("allUsers" to users)
            val template = FreeMarkerContent("all-users.ftl", templateModel)
            call.respond(template)
        }

        get("/simple-form") { call.respond(FreeMarkerContent("simple-form.ftl", null)) }
        post("/simple-form-action") {
            // transmit data via POST
            // Content-Type: application/x-www-form-urlencoded
            val formParameters = call.receiveParameters()
            val firstName = formParameters["fname"].toString()
            val lastName = formParameters["lname"].toString()
            val formUser = SimpleFormUser(firstName, lastName)
            val templateModel = mapOf("formUser" to formUser)
            val template = FreeMarkerContent("simple-form-action.ftl", templateModel)
            call.respond(template)
        }

        get("/multipart-form-data") {
            call.respond(FreeMarkerContent("multipart-form-data.ftl", null))
        }
        post("/multipart-form-data-action") {
            // transmit data via POST
            // Content-Type: multipart/form-data
            val multipartData = call.receiveMultipart()
            var firstName = ""
            var fileName = ""

            multipartData.forEachPart { part ->
                when (part) {
                    is PartData.FormItem -> {
                        firstName = part.value
                    }
                    is PartData.FileItem -> {
                        fileName = part.originalFileName as String
                        val fileBytes = part.streamProvider().readBytes()
                        val file = File(".uploads/$fileName")
                        // Ensure the parent directory exists
                        Files.createDirectories(file.toPath().parent)
                        Files.write(file.toPath(), fileBytes, StandardOpenOption.CREATE)
                    }
                    else -> {}
                }
                part.dispose()
            }

            val multipart = MultipartFormData(firstName, fileName)
            call.respond(
                    FreeMarkerContent(
                            "multipart-form-data-action.ftl",
                            mapOf("multipart" to multipart)
                    )
            )
        }

        get("/get-upload/{imageName}") {
            val imageName = call.parameters["imageName"]
            call.respondFile(File(".uploads/$imageName"))
        }

        get("/fotoalbum") {
            val directory = File(".uploads")
            val images =
                    directory.listFiles()?.filter { it.isFile and it.name.endsWith(".jpg") }
                            ?: emptyList()
            val fotos = mutableListOf<Foto>()

            for (image in images) {
                fotos.add(Foto(image.nameWithoutExtension, image.name))
            }

            val model = Fotoalbum(fotos, "/get-upload")

            call.respond(FreeMarkerContent("fotoalbum.ftl", mapOf("model" to model)))
        }
    }*/
}
