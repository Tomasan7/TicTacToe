package me.tomasan7.tictactoe.protocol.packet.server.packet

import kotlinx.serialization.Serializable
import me.tomasan7.tictactoe.protocol.packet.server.ServerPacket

@Serializable
data class ServerPlayerTurnPacket(
    val playerId: Int
) : ServerPacket
{
    override val id = PACKET_ID

    companion object
    {
        const val PACKET_ID = 12
    }
}
