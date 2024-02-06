package me.tomasan7.tictactoe.web

import me.tomasan7.tictactoe.game.GameOptions
import me.tomasan7.tictactoe.protocol.packet.server.ServerPacket
import me.tomasan7.tictactoe.util.Color
import me.tomasan7.tictactoe.web.view.BoardView
import me.tomasan7.tictactoe.web.view.PlayersView

class Game(
    meId: Int,
    private val connection: Connection,
    private val gameOptions: GameOptions,
    private val boardView: BoardView,
    private val playersView: PlayersView
)
{
    private val me: Player = Player(meId)
    private val players = mutableMapOf<Int, Player>()
    private val playersMeIncluded: Map<Int, Player>
        get() = players + (me.id to me)
    private val board = Board(gameOptions.width, gameOptions.height) { x, y, playerId ->
        if (playerId == null)
        {
            boardView.clearSymbol(x, y)
            return@Board
        }
        val placer = players[playerId] ?: return@Board
        boardView.drawSymbol(x, y, placer.symbol!!, placer.color!!)
    }
    private var gameState = GameState.WAITING_FOR_PLAYERS

    init
    {
        playersView.addPlayer(me)
        // TODO: REMOVE TEST ONLY
        me.name = "Tomasan7"
        me.color = Color.BLUE
        me.symbol = "101010101"
        playersView.updatePlayer(me)
    }

    fun receivePacket(serverPacket: ServerPacket)
    {

    }

    fun addPlayer(player: Player)
    {
        players[player.id] = player
    }

    fun removePlayer(player: Player)
    {
        players.remove(player.id)
    }
}
