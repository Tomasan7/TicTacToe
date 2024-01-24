package me.tomasan7.tttweb.plugins

import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.launch
import me.tomasan7.tictactoe.server.game.GameManager
import me.tomasan7.tictactoe.server.game.SessionManager
import me.tomasan7.tictactoe.server.network.packet.client.JsonClientPacketDeserializer
import me.tomasan7.tictactoe.server.network.packet.server.JsonServerPacketSerializer
import me.tomasan7.tictactoe.server.network.packet.server.packet.ServerClientReadyAckPacket

import me.tomasan7.tictactoe.server.network.session.Session
import me.tomasan7.tictactoe.server.network.session.WsSession
import java.time.Duration
import java.util.*
import kotlin.collections.LinkedHashSet

fun Application.configureSockets()
{
    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }

    val sessionManager = SessionManager(GameManager())

    routing {
        webSocket("/ws") {
            val session = WsSession(this, JsonServerPacketSerializer(), JsonClientPacketDeserializer())
            sessionManager.addSession(session)

            for (frame in incoming)
                session.receiveFrame(frame)
        }
    }
}
