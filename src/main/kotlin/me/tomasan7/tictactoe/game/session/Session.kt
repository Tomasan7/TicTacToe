package me.tomasan7.tictactoe.game.session

import me.tomasan7.tictactoe.game.packet.ClientPacket

interface Session
{
    suspend fun sendPacket(packet: ClientPacket)
    suspend fun close(message: String = "")
}
