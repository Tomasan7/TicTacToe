package me.tomasan7.tictactoe.server.game.packet.client.packet

import kotlinx.serialization.Serializable
import me.tomasan7.tictactoe.server.game.packet.client.ClientPacket

@Serializable
data class ClientReadyPacket(
    val value: Boolean
) : ClientPacket
{
    override val id = 8
}
