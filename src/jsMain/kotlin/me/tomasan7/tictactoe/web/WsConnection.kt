package me.tomasan7.tictactoe.web

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import me.tomasan7.tictactoe.protocol.packet.InvalidPacketFormatException
import me.tomasan7.tictactoe.protocol.packet.InvalidPacketIdException
import me.tomasan7.tictactoe.protocol.packet.client.ClientPacket
import me.tomasan7.tictactoe.protocol.packet.client.ClientPacketSerializer
import me.tomasan7.tictactoe.protocol.packet.server.ServerPacket
import me.tomasan7.tictactoe.protocol.packet.server.ServerPacketSerializer
import org.w3c.dom.WebSocket

class WsConnection(
    private val websocket: WebSocket,
    private val serverPacketSerializer: ServerPacketSerializer,
    private val clientPacketSerializer: ClientPacketSerializer
) : Connection
{
    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private val _incomingPackets = MutableSharedFlow<ServerPacket>()
    override val incomingPackets = _incomingPackets.asSharedFlow()

    init
    {
        websocket.onmessage = { event ->
            val frameText = event.data as String
            try
            {
                val packet = serverPacketSerializer.deserializePacket(frameText)
                coroutineScope.launch {
                    _incomingPackets.emit(packet)
                }
            }
            catch (e: InvalidPacketFormatException)
            {
                println("Invalid packet format: $frameText")
            }
            catch (e: InvalidPacketIdException)
            {
                println("Invalid packet id: $frameText")
            }
        }
    }

    override fun sendPacket(packet: ClientPacket)
    {
        coroutineScope.launch {
            websocket.send(clientPacketSerializer.serializePacket(packet))
        }
    }

    override fun close(message: String)
    {
        websocket.close(code = 1000, reason = message)
        coroutineScope.cancel(message)
    }
}
