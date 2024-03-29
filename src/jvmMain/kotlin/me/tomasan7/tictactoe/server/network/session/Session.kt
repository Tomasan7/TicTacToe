package me.tomasan7.tictactoe.server.network.session

import kotlinx.coroutines.channels.ReceiveChannel
import me.tomasan7.tictactoe.protocol.packet.client.ClientPacket
import me.tomasan7.tictactoe.protocol.packet.server.ServerPacket


interface Session
{
    /* Useful resource https://medium.com/@mortitech/mastering-kotlin-channels-from-beginner-to-pro-part-2-3477255aee15 */
    val incomingPacketsChannel: ReceiveChannel<ClientPacket>
    val isActive: Boolean

    suspend fun sendPacket(packet: ServerPacket)
    suspend fun close(message: String = "")
}
