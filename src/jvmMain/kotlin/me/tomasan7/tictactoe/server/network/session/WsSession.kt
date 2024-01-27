package me.tomasan7.tictactoe.server.network.session

import io.ktor.server.plugins.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.isActive
import me.tomasan7.tictactoe.protocol.packet.JsonPacketSerializer
import me.tomasan7.tictactoe.protocol.packet.client.JsonClientPacketSerializer
import me.tomasan7.tictactoe.protocol.packet.server.ServerPacket
import me.tomasan7.tictactoe.protocol.packet.server.ServerPacketSerializer
import org.slf4j.LoggerFactory

class WsSession(
    private val wsSession: WebSocketServerSession,
    private val serverPacketSerializer: ServerPacketSerializer,
    private val clientPacketDeserializer: JsonClientPacketSerializer
) : Session
{
    private val logger = LoggerFactory.getLogger(WsSession::class.java)
    private val remoteHost = wsSession.call.request.origin.remoteHost

    private val _incomingPacketsChannel = Channel<me.tomasan7.tictactoe.protocol.packet.client.ClientPacket>()
    override val incomingPacketsChannel: ReceiveChannel<me.tomasan7.tictactoe.protocol.packet.client.ClientPacket> = _incomingPacketsChannel

    override val isActive: Boolean
        get() = wsSession.isActive

    /**
     * Intended to only be called by the websockets handler.
     */
    suspend fun receiveFrame(incomingFrame: Frame)
    {
        if (incomingFrame is Frame.Text)
        {
            val frameText = incomingFrame.readText()
            val packet = try
            {
                clientPacketDeserializer.deserializePacket(frameText)
            }
            catch (e: me.tomasan7.tictactoe.protocol.packet.InvalidPacketFormatException)
            {
                return logger.warn("Received an invalid packet format from $remoteHost: '$frameText'")
            }
            catch (e: me.tomasan7.tictactoe.protocol.packet.InvalidPacketIdException)
            {
                return logger.warn("Received a packet with an invalid id from $remoteHost: '$frameText'")
            }
            catch (e: Exception)
            {
                return logger.warn("Failed to parse packet from $remoteHost. ${e.message}: '$frameText'")
            }

            _incomingPacketsChannel.send(packet)
            logger.debug("Received a packet from $remoteHost: '$frameText'")
        }
        else
            logger.warn("Received non text frame of type ${incomingFrame.frameType} from $remoteHost")
    }

    override suspend fun sendPacket(packet: ServerPacket)
    {
        val serializedPacket = serverPacketSerializer.serializePacket(packet)
        val frame = Frame.Text(serializedPacket)
        wsSession.send(frame)

        logger.debug("Sending packet to $remoteHost: '$serializedPacket'")
    }

    override suspend fun close(message: String)
    {
        wsSession.close(CloseReason(CloseReason.Codes.NORMAL, message))
    }
}
