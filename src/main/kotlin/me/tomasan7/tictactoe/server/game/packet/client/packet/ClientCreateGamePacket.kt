package me.tomasan7.tictactoe.server.game.packet.client.packet

import kotlinx.serialization.Serializable
import me.tomasan7.tictactoe.server.game.packet.client.ClientPacket

@Serializable
data class ClientCreateGamePacket(
    val width: Int,
    val height: Int,
    val winLength: Int,
    val maxPlayers: Int,
    val public: Boolean
) : ClientPacket
{
    override val id = PACKET_ID

    companion object
    {
        const val PACKET_ID = 0
    }
}
