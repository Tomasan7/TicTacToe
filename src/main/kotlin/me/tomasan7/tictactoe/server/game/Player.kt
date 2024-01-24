package me.tomasan7.tictactoe.server.game

import me.tomasan7.tictactoe.server.network.packet.server.ServerPacket
import me.tomasan7.tictactoe.server.util.Color

class Player(private val session: ClientSession, val id: Int)
{
    var name: String? = ""
    var symbol: String? = null
    var color: Color? = null
    var ready: Boolean = false

    suspend fun sendPacket(packet: ServerPacket)
    {
        session.sendPacket(packet)
    }
}
