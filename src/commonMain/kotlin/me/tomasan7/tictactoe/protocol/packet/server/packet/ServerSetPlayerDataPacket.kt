package me.tomasan7.tictactoe.protocol.packet.server.packet

import kotlinx.serialization.Serializable
import me.tomasan7.tictactoe.protocol.packet.server.ServerPacket
import me.tomasan7.tictactoe.util.Color

@Serializable
data class ServerSetPlayerDataPacket(
    val playerId: Int,
    val name: String? = null,
    val color: Color? = null,
    val symbol: String? = null
) : ServerPacket
{
    override val id = PACKET_ID

    companion object
    {
        const val PACKET_ID = 7
    }
}
