package me.tomasan7.tictactoe.protocol.packet.client.packet

import kotlinx.serialization.Serializable
import me.tomasan7.tictactoe.protocol.packet.client.ClientPacket

@Serializable
data class ClientPlaceSymbolPacket(
    val x: Int,
    val y: Int
) : ClientPacket
{
    override val id = PACKET_ID

    companion object
    {
        const val PACKET_ID = 13
    }
}
