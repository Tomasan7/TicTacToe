package me.tomasan7.tictactoe.protocol.packet.server.packet

import kotlinx.serialization.Serializable
import me.tomasan7.tictactoe.protocol.packet.server.ServerPacket

@Serializable
data class ServerGameClosePacket(
    val reason: String
) : ServerPacket
{
    override val id = PACKET_ID

    companion object
    {
        const val PACKET_ID = 16
    }
}
