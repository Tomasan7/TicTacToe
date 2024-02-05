package me.tomasan7.tictactoe.web.page

import me.tomasan7.tictactoe.protocol.packet.client.packet.ClientCreateGamePacket
import me.tomasan7.tictactoe.protocol.packet.server.ServerPacket
import me.tomasan7.tictactoe.protocol.packet.server.packet.ServerJoinGamePacket
import me.tomasan7.tictactoe.web.Connection
import org.w3c.dom.HTMLElement

class CreateGamePage(
    pageElement: HTMLElement,
    connection: Connection,
    private val gamePageElement: HTMLElement,
    private val pageChangeHandler: PageChangeHandler
) : PacketHtmlPage(pageElement, connection)
{
    private val elements = object
    {
        val widthInput by htmlMapper.Input()
        val heightInput by htmlMapper.Input()
        val symbolSizeInput by htmlMapper.Input()
        val winLengthInput by htmlMapper.Input()
        val maxPlayersInput by htmlMapper.Input()
        val publicCheckbox by htmlMapper.Input()
        val createGameButton by htmlMapper.Button()
    }

    init
    {
        elements.createGameButton.onclick = { onCreateGameButtonClick() }
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
        // TODO: Show a feedback to the user on why the game couldn't be created
        val width = elements.widthInput.value.toIntOrNull() ?: return
        val height = elements.heightInput.value.toIntOrNull() ?: return
        val symbolSize = elements.symbolSizeInput.value.toIntOrNull() ?: return
        val winLength = elements.winLengthInput.value.toIntOrNull() ?: return
        val maxPlayers = elements.maxPlayersInput.value.toIntOrNull() ?: return
        val isPublic = elements.publicCheckbox.checked

        /* Some basic checks */

        val minSize = 3 + maxPlayers
        val minWinLength = 3

        if (width < minSize || height < minSize)
            return println("Game area too small")

        if (winLength < minWinLength)
            return println("Win length too small")

        if (maxPlayers < 2)
            return println("Too few players")

        connection.sendPacket(ClientCreateGamePacket(width, height, winLength, symbolSize, maxPlayers, isPublic))
    }
}
