package me.tomasan7.tictactoe

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import me.tomasan7.tictactoe.plugins.*

fun main()
{
    embeddedServer(Netty, port = 80, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module()
{
    configureHTTP()
    configureMonitoring()
    configureSockets()
    configureTemplating()
    configureSerialization()
    configureSecurity()
    configureRouting()
}
