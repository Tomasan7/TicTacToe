package me.tomasan7.tictactoe.game.session

import io.ktor.server.websocket.*
import io.ktor.websocket.*
import me.tomasan7.tictactoe.game.packet.ClientPacket

class WsSession(private val wsSession: DefaultWebSocketServerSession) : Session
{
    override suspend fun sendPacket(packet: ClientPacket)
    {
        wsSession.send("${packet.id}\n${packet.serialize()}")
    }

    override suspend fun close(message: String)
    {
        wsSession.close(CloseReason(CloseReason.Codes.NORMAL, message))
    }
}
