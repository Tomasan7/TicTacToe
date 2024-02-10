package me.tomasan7.tictactoe.web.page

import me.tomasan7.tictactoe.game.GameOptions
import me.tomasan7.tictactoe.protocol.packet.server.ServerPacket
import me.tomasan7.tictactoe.protocol.packet.server.packet.ServerJoinGamePacket
import me.tomasan7.tictactoe.util.Color
import me.tomasan7.tictactoe.web.Connection
import me.tomasan7.tictactoe.web.Game
import me.tomasan7.tictactoe.web.view.CanvasBoardView
import me.tomasan7.tictactoe.web.view.HtmlPlayersView
import me.tomasan7.tictactoe.web.view.PlayerDataHandler
import org.w3c.dom.HTMLElement

class GamePage(
    private val initiationPacket: ServerJoinGamePacket,
    pageElement: HTMLElement,
    connection: Connection,
    private val pageChangeHandler: PageChangeHandler
) : PacketHtmlPage(pageElement, connection)
{
    private val elements = object
    {
        val gameCanvas by htmlMapper.Canvas()
        val playerCardTemplate by htmlMapper.Template()
        val playerCardsContainer by htmlMapper
        val playerDataForm by htmlMapper
    }

    private val gameOptions = initiationPacket.gameOptions

    private val boardView =
        CanvasBoardView(
            elements.gameCanvas,
            Color.BLACK,
            gameOptions.width,
            gameOptions.height,
            gameOptions.symbolSize,
            1
        )
    private val playersView =
        HtmlPlayersView(initiationPacket.symbolSize, elements.playerCardTemplate, elements.playerCardsContainer)
    private val playerDataHandler = PlayerDataHandler(elements.playerDataForm, initiationPacket.symbolSize)

    private val game = Game(
        initiationPacket.playerId,
        connection,
        initiationPacket.gameOptions,
        boardView,
        playersView,
        playerDataHandler
    )

    override fun onPacket(packet: ServerPacket)
    {
        game.receivePacket(packet)
    }

    private val ServerJoinGamePacket.gameOptions
        get() = GameOptions(
            width,
            height,
            winLength,
            symbolSize,
            maxPlayers,
            public
        )
}
