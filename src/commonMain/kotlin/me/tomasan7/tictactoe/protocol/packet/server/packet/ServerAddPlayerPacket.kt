package me.tomasan7.tictactoe.protocol.packet.server.packet

import kotlinx.serialization.Serializable
import me.tomasan7.tictactoe.protocol.packet.server.ServerPacket

@Serializable
data class ServerAddPlayerPacket(
    val playerId: Int
) : ServerPacket
{
    override val id = PACKET_ID

    companion object
    {
        const val PACKET_ID = 4
    }
}
