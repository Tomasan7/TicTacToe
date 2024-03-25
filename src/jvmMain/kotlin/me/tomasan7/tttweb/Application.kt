package me.tomasan7.tttweb

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import me.tomasan7.tttweb.plugins.*

fun main()
{
    val host = System.getenv("TTT_HOST") ?: "0.0.0.0"
    val port = System.getenv("TTT_PORT")?.toInt() ?: 8000

    embeddedServer(Netty, port = port, host = host, module = Application::module)
        .start(wait = true)
}

fun Application.module()
{
    configureHTTP()
    configureCallLogging()
    configureSockets()
    configureRouting()
    configureTemplating()
    install(IgnoreTrailingSlash)
}
