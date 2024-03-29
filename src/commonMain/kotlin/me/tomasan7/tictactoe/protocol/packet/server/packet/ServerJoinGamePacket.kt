package me.tomasan7.tictactoe.protocol.packet.server.packet

import kotlinx.serialization.Serializable
import me.tomasan7.tictactoe.protocol.packet.server.ServerPacket

@Serializable
data class ServerJoinGamePacket(
    val gameCode: String,
    val width: Int,
    val height: Int,
    val winLength: Int,
    val symbolSize: Int,
    val maxPlayers: Int,
    val public: Boolean,
    val playerId: Int,
    val ownerId: Int
) : ServerPacket
{
    override val id = PACKET_ID

    companion object
    {
        const val PACKET_ID = 3
    }
}
