package me.tomasan7.tictactoe.web

import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.tomasan7.tictactoe.protocol.packet.InvalidPacketFormatException
import me.tomasan7.tictactoe.protocol.packet.InvalidPacketIdException
import me.tomasan7.tictactoe.protocol.packet.client.ClientPacketSerializer
import me.tomasan7.tictactoe.protocol.packet.client.JsonClientPacketSerializer
import me.tomasan7.tictactoe.protocol.packet.client.packet.ClientJoinGamePacket
import me.tomasan7.tictactoe.protocol.packet.server.JsonServerPacketSerializer
import me.tomasan7.tictactoe.protocol.packet.server.ServerPacket
import me.tomasan7.tictactoe.protocol.packet.server.ServerPacketSerializer
import me.tomasan7.tictactoe.web.page.HomePage
import me.tomasan7.tictactoe.web.page.Page
import me.tomasan7.tictactoe.web.page.PageChangeHandler
import org.w3c.dom.WebSocket

class Application : PageChangeHandler
{
    private lateinit var connection: Connection
    private val clientPacketSerializer: ClientPacketSerializer = JsonClientPacketSerializer()
    private val serverPacketSerializer: ServerPacketSerializer = JsonServerPacketSerializer()

    private val htmlMapper = HtmlIdMapper(document.body!!)

    private val elements = object {
        val homePage by htmlMapper
        val gamePage by htmlMapper
        val createGamePage by htmlMapper
    }

    private lateinit var page: Page

    fun init()
    {
        initSocket()
        elements.homePage.hide()
        elements.gamePage.hide()
        elements.createGamePage.hide()
    }

    fun start()
    {
        changeToPage(HomePage(this, elements.createGamePage, elements.gamePage, elements.homePage, connection))
    }

    private fun initSocket()
    {
        val socket = WebSocket(getSocketUrlFromRelativePath("tictactoe/ws"))
        connection = WsConnection(socket, serverPacketSerializer, clientPacketSerializer)
        CoroutineScope(Dispatchers.Default).launch {
            connection.incomingPackets.collect {
                println("Received packet: $it")
            }
        }
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

    override fun changeToPage(page: Page)
    {
        if (::page.isInitialized)
            this.page.exit()
        page.enter()
        this.page = page
    }
}
