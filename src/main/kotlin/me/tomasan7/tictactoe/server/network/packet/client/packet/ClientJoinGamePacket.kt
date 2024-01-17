package me.tomasan7.tictactoe.server.network.packet.client.packet

import kotlinx.serialization.Serializable
import me.tomasan7.tictactoe.server.network.packet.client.ClientPacket

@Serializable
data class ClientJoinGamePacket(
    val gameCode: String
) : ClientPacket
{
    override val id = PACKET_ID

    companion object
    {
        const val PACKET_ID = 1
    }
}
