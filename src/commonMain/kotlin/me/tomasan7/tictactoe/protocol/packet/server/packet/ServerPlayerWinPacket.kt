package me.tomasan7.tictactoe.protocol.packet.server.packet

import kotlinx.serialization.Serializable
import me.tomasan7.tictactoe.protocol.packet.server.ServerPacket

@Serializable
data class ServerPlayerWinPacket(
    val winnerId: Int,
    val winningLineX1: Int,
    val winningLineY1: Int,
    val winningLineX2: Int,
    val winningLineY2: Int
) : ServerPacket
{
    override val id = PACKET_ID

    companion object
    {
        const val PACKET_ID = 15
    }
}
