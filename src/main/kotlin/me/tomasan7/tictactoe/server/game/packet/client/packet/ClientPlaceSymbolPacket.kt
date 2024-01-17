package me.tomasan7.tictactoe.server.game.packet.client.packet

import kotlinx.serialization.Serializable
import me.tomasan7.tictactoe.server.game.packet.client.ClientPacket

@Serializable
data class ClientPlaceSymbolPacket(
    val x: Int,
    val y: Int
) : ClientPacket
{
    override val id = 13
}
