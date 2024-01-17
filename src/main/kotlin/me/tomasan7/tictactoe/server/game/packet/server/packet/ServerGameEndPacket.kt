package me.tomasan7.tictactoe.server.game.packet.server.packet

import kotlinx.serialization.Serializable
import me.tomasan7.tictactoe.server.game.packet.server.ServerPacket

@Serializable
data class ServerGameEndPacket(
    val winnerId: Int?
) : ServerPacket
{
    override val id = 15
}
