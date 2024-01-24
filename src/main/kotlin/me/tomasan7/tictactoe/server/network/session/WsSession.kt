package me.tomasan7.tictactoe.server.network.session

import io.ktor.server.plugins.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.isActive
import me.tomasan7.tictactoe.server.network.packet.InvalidPacketFormatException
import me.tomasan7.tictactoe.server.network.packet.InvalidPacketIdException
import me.tomasan7.tictactoe.server.network.packet.client.ClientPacket
import me.tomasan7.tictactoe.server.network.packet.client.JsonClientPacketDeserializer
import me.tomasan7.tictactoe.server.network.packet.server.ServerPacket
import me.tomasan7.tictactoe.server.network.packet.server.ServerPacketSerializer
import org.slf4j.LoggerFactory

class WsSession(
    private val wsSession: WebSocketServerSession,
    private val serverPacketSerializer: ServerPacketSerializer,
    private val clientPacketDeserializer: JsonClientPacketDeserializer
) : Session
{
    private val logger = LoggerFactory.getLogger(WsSession::class.java)
    private val remoteHost = wsSession.call.request.origin.remoteHost

    private val _incomingPacketsChannel = Channel<ClientPacket>()
    override val incomingPacketsChannel: ReceiveChannel<ClientPacket> = _incomingPacketsChannel

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
            catch (e: InvalidPacketFormatException)
            {
                return logger.warn("Received an invalid packet format from $remoteHost: \n'$frameText'")
            }
            catch (e: InvalidPacketIdException)
            {
                return logger.warn("Received a packet with an invalid id from $remoteHost: \n'$frameText'")
            }
            catch (e: Exception)
            {
                return logger.warn("Failed to parse packet from $remoteHost. ${e.message}: \n'$frameText'")
            }

            _incomingPacketsChannel.send(packet)
            logger.debug("Received a packet from $remoteHost: \n'$frameText'")
        }
        else
            logger.warn("Received non text frame of type ${incomingFrame.frameType} from $remoteHost")
    }

    override suspend fun sendPacket(packet: ServerPacket)
    {
        val serializedPacket = serverPacketSerializer.serializePacket(packet)
        val frame = Frame.Text(serializedPacket)
        wsSession.send(frame)

        logger.debug("Sending packet to $remoteHost: \n'$serializedPacket'")
    }

    override suspend fun close(message: String)
    {
        wsSession.close(CloseReason(CloseReason.Codes.NORMAL, message))
    }

    override fun isActive(): Boolean
    {
        return wsSession.isActive
    }
}
