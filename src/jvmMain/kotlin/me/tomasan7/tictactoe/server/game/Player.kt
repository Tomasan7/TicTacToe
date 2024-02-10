package me.tomasan7.tictactoe.server.game

import me.tomasan7.tictactoe.protocol.packet.server.ServerPacket
import me.tomasan7.tictactoe.util.Color

class Player(private val session: ClientSession, val id: Int)
{
    var name: String? = null
    var symbol: String? = null
    var color: Color? = null
    var ready: Boolean = false

    suspend fun sendPacket(packet: ServerPacket)
    {
        session.sendPacket(packet)
    }
}
