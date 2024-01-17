package me.tomasan7.tictactoe.server.game.packet.server.packet

import kotlinx.serialization.Serializable
import me.tomasan7.tictactoe.server.game.packet.client.ClientPacket
import me.tomasan7.tictactoe.server.game.packet.server.ServerPacket

@Serializable
data class ServerPlaceSymbolPacket(
    val playerId: Int,
    val x: Int,
    val y: Int
) : ServerPacket
{
    override val id = 14
}
