package me.tomasan7.tictactoe.web.page

import me.tomasan7.tictactoe.protocol.packet.client.packet.ClientJoinGamePacket
import me.tomasan7.tictactoe.protocol.packet.server.ServerPacket
import me.tomasan7.tictactoe.protocol.packet.server.packet.ServerJoinGamePacket
import me.tomasan7.tictactoe.web.Connection
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.InputEvent

class HomePage(
    private val pageChangeHandler: PageChangeHandler,
    private val createGamePageElement: HTMLElement,
    private val gamePageElement: HTMLElement,
    pageElement: HTMLElement,
    connection: Connection
) : PacketHtmlPage(pageElement, connection)
{
    private val elements = object {
        val createGameButton by htmlMapper.Button()
        val joinGameButton by htmlMapper.Button()
        val gameCodeInput by htmlMapper.Input()
    }

    private val gameCodeLength = elements.gameCodeInput.getAttribute("size")?.toInt() ?: 6

    init
    {
        elements.createGameButton.onclick = { onCreateGameButtonClick() }
        elements.joinGameButton.onclick = { onJoinGameButtonClick() }
        elements.gameCodeInput.oninput = { onGameCodeInput(it) }
    }

    override fun onPacket(packet: ServerPacket)
    {
        // TODO
        when (packet)
        {
            is ServerJoinGamePacket -> pageChangeHandler.changeToPage(GamePage(packet, gamePageElement, connection, pageChangeHandler))
        }
    }

    private fun onCreateGameButtonClick()
    {
        pageChangeHandler.changeToPage(CreateGamePage(createGamePageElement, connection, gamePageElement, pageChangeHandler))
    }

    private fun onJoinGameButtonClick()
    {
        val gameCode = elements.gameCodeInput.value.uppercase()
        if (gameCode.length != gameCodeLength)
            return

        connection.sendPacket(ClientJoinGamePacket(gameCode))
    }

    private fun onGameCodeInput(event: InputEvent)
    {
        updateJoinGameDisabledState()
    }

    private fun updateJoinGameDisabledState()
    {
        elements.joinGameButton.disabled = elements.gameCodeInput.value.length != gameCodeLength
    }
}
