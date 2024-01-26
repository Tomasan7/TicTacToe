package me.tomasan7.tictactoe.server.game

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import me.tomasan7.tictactoe.protocol.packet.server.ServerPacket
import me.tomasan7.tictactoe.server.network.session.Session

/**
 * A wrapper around a [Session] that makes it possible to collect incoming packets as a [SharedFlow][kotlinx.coroutines.flow.SharedFlow].
 */
class ClientSession(val underlyingSession: Session)
{
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO)

    private val _incomingPackets = MutableSharedFlow<me.tomasan7.tictactoe.protocol.packet.client.ClientPacket>()
    val incomingPackets = _incomingPackets.asSharedFlow()

    val isActive: Boolean
        get() = underlyingSession.isActive

    init
    {
        coroutineScope.launch {
            for (packet in underlyingSession.incomingPacketsChannel)
                _incomingPackets.emit(packet)
        }
    }

    fun sendPacket(packet: ServerPacket)
    {
        coroutineScope.launch {
            underlyingSession.sendPacket(packet)
        }
    }

    fun close()
    {
        coroutineScope.launch {
            underlyingSession.close()
            coroutineScope.cancel() // Is this possible? (to cancel a scope from inside it)
        }
    }
}