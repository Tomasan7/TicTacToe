package me.tomasan7.tttweb.plugins

import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting()
{
    routing {
        staticResources("/static/", "/pages/")
        staticResources("/frontend/", "/frontend/")

        get("/") {
            call.respondTemplate("index.ftlh")
        }
    }
}
