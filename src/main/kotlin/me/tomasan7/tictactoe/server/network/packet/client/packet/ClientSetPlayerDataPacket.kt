package me.tomasan7.tictactoe.server.network.packet.client.packet

import kotlinx.serialization.Serializable
import me.tomasan7.tictactoe.server.network.packet.client.ClientPacket

@Serializable
data class ClientSetPlayerDataPacket(
    val name: String?,
    val color: Int?,
    val symbol: String?
) : ClientPacket
{
    override val id = PACKET_ID

    companion object
    {
        const val PACKET_ID = 6
    }
}
