package me.tomasan7.tictactoe.server.network.packet.server.packet

import kotlinx.serialization.Serializable
import me.tomasan7.tictactoe.server.network.packet.server.ServerPacket

@Serializable
data class ServerClientReadyAckPacket(
    val value: Boolean,
    val reason: String?
) : ServerPacket
{
    override val id = PACKET_ID

    companion object
    {
        const val PACKET_ID = 9
    }
}
