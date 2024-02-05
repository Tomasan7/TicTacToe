package me.tomasan7.tictactoe.web

import kotlinx.coroutines.flow.SharedFlow
import me.tomasan7.tictactoe.protocol.packet.client.ClientPacket
import me.tomasan7.tictactoe.protocol.packet.server.ServerPacket

interface Connection
{
    fun sendPacket(packet: ClientPacket)
    fun close(message: String = "")
    val incomingPackets: SharedFlow<ServerPacket>
}
