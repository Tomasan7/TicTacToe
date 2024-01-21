package me.tomasan7.tictactoe.server.network.packet.server.packet

import kotlinx.serialization.Serializable
import me.tomasan7.tictactoe.server.network.packet.server.ServerPacket

@Serializable
data class ServerRemovePlayerPacket(
    val playerId: Int
) : ServerPacket
{
    override val id = PACKET_ID

    companion object
    {
        const val PACKET_ID = 5
    }
}
