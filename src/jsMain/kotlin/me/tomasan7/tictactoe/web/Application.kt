package me.tomasan7.tictactoe.web

import kotlinx.browser.document
import kotlinx.browser.window
import me.tomasan7.tictactoe.protocol.packet.InvalidPacketFormatException
import me.tomasan7.tictactoe.protocol.packet.InvalidPacketIdException
import me.tomasan7.tictactoe.protocol.packet.client.ClientPacket
import me.tomasan7.tictactoe.protocol.packet.client.ClientPacketSerializer
import me.tomasan7.tictactoe.protocol.packet.client.JsonClientPacketSerializer
import me.tomasan7.tictactoe.protocol.packet.client.packet.ClientJoinGamePacket
import me.tomasan7.tictactoe.protocol.packet.server.JsonServerPacketSerializer
import me.tomasan7.tictactoe.protocol.packet.server.ServerPacket
import me.tomasan7.tictactoe.protocol.packet.server.ServerPacketSerializer
import org.w3c.dom.WebSocket

class Application
{
    private var currentGame: Game? = null
    private var state: AppState = AppState.HOMEPAGE
    private lateinit var socket: WebSocket
    private val clientPacketSerializer: ClientPacketSerializer = JsonClientPacketSerializer()
    private val serverPacketSerializer: ServerPacketSerializer = JsonServerPacketSerializer()
    private val dom = Dom(document)
    private val currentPageEle = dom.homePageContainer

    fun init()
    {
        initSocket()
        initElements()
    }

    private fun initElements()
    {
        dom.homePageContainer.show()
        dom.gamePageContainer.hide()

        dom.homePage.joinGameButton.disabled = true
        dom.homePage.gameCodeInput.oninput = {
            if (dom.homePage.gameCodeInput.value.length == 6)
                dom.homePage.joinGameButton.disabled = false
            else
                dom.homePage.joinGameButton.disabled = true
        }

        dom.homePage.joinGameButton.onclick = {
            val packet = ClientJoinGamePacket(dom.homePage.gameCodeInput.value.uppercase())
            sendPacket(packet)
            null
        }
    }

    private fun initSocket()
    {
        socket = WebSocket(getSocketUrlFromRelativePath("ws"))
        socket.onmessage = { event ->
            val frameText = event.data as String
            try
            {
                val packet = serverPacketSerializer.deserializePacket(frameText)
                receivePacket(packet)
            }
            catch (e: InvalidPacketFormatException)
            {
                println("Received an invalid packet format: '$frameText'")
            }
            catch (e: InvalidPacketIdException)
            {
                println("Received a packet with an invalid id: '$frameText'")
            }
            catch (e: Exception)
            {
                println("Failed to parse packet. ${e.message}: '$frameText'")
            }
        }
    }

    private fun receivePacket(packet: ServerPacket)
    {
        TODO()
    }

    private fun sendPacket(packet: ClientPacket)
    {
        socket.send(clientPacketSerializer.serializePacket(packet))
    }

    fun changeState(newState: AppState)
    {
        currentPageEle.hide()

        when (newState)
        {
            AppState.HOMEPAGE -> dom.homePageContainer.show()
            AppState.IN_GAME -> dom.gamePageContainer.show()
        }

        state = newState
    }

    private fun getSocketUrlFromRelativePath(path: String): String
    {
        val protocol = if (window.location.protocol == "https:") "wss:" else "ws:"
        val host = window.location.hostname
        val port = if (window.location.port.isNotBlank())
            window.location.port.toInt()
        else if (protocol == "wss:")
            443
        else
            80

        return "$protocol//$host:$port/${path.removePrefix("/")}"
    }
}
