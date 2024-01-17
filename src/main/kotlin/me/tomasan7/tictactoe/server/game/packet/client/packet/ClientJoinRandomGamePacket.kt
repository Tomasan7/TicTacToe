package me.tomasan7.tictactoe.server.game.packet.client.packet

import kotlinx.serialization.Serializable
import me.tomasan7.tictactoe.server.game.packet.client.ClientPacket

@Serializable
data object ClientJoinRandomGamePacket: ClientPacket
{
    override val id = 2
}
