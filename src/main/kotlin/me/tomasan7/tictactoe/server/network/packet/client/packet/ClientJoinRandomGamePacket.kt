package me.tomasan7.tictactoe.server.network.packet.client.packet

import kotlinx.serialization.Serializable
import me.tomasan7.tictactoe.server.network.packet.client.ClientPacket

@Serializable
data object ClientJoinRandomGamePacket: ClientPacket
{
    const val PACKET_ID = 2
    override val id = PACKET_ID
}
