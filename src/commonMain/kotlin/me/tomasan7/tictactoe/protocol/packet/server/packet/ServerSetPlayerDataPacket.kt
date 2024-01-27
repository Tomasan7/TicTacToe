package me.tomasan7.tictactoe.protocol.packet.server.packet

import kotlinx.serialization.Serializable
import me.tomasan7.tictactoe.protocol.packet.server.ServerPacket

@Serializable
data class ServerSetPlayerDataPacket(
    val playerId: Int,
    val name: String? = null,
    val color: Int? = null,
    val symbol: String? = null
) : ServerPacket
{
    override val id = PACKET_ID

    companion object
    {
        const val PACKET_ID = 7
    }
}
