package me.tomasan7.tictactoe.server.game.packet.server.packet

import kotlinx.serialization.Serializable
import me.tomasan7.tictactoe.server.game.packet.server.ServerPacket

@Serializable
data class ServerGameClosePacket(
    val reason: String
) : ServerPacket
{
    override val id = 16
}
