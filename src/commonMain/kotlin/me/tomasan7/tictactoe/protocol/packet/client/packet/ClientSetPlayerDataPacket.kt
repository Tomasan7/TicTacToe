package me.tomasan7.tictactoe.protocol.packet.client.packet

import kotlinx.serialization.Serializable
import me.tomasan7.tictactoe.protocol.packet.client.ClientPacket
import me.tomasan7.tictactoe.util.Color

@Serializable
data class ClientSetPlayerDataPacket(
    val name: String? = null,
    val color: Color? = null,
    val symbol: String?
) : ClientPacket
{
    override val id = PACKET_ID

    companion object
    {
        const val PACKET_ID = 6
    }
}
