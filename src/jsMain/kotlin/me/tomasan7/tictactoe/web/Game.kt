package me.tomasan7.tictactoe.web

import me.tomasan7.tictactoe.game.GameOptions
import me.tomasan7.tictactoe.protocol.packet.client.packet.ClientReadyPacket
import me.tomasan7.tictactoe.protocol.packet.client.packet.ClientSetPlayerDataPacket
import me.tomasan7.tictactoe.protocol.packet.server.ServerPacket
import me.tomasan7.tictactoe.protocol.packet.server.packet.ServerAddPlayerPacket
import me.tomasan7.tictactoe.protocol.packet.server.packet.ServerClientReadyAckPacket
import me.tomasan7.tictactoe.protocol.packet.server.packet.ServerSetPlayerDataPacket
import me.tomasan7.tictactoe.util.Color
import me.tomasan7.tictactoe.web.view.BoardView
import me.tomasan7.tictactoe.web.view.PlayerDataHandler
import me.tomasan7.tictactoe.web.view.PlayersView

class Game(
    meId: Int,
    private val connection: Connection,
    private val gameOptions: GameOptions,
    private val boardView: BoardView,
    private val playersView: PlayersView,
    private val playerDataHandler: PlayerDataHandler
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
        boardView.hide()
        playerDataHandler.show()
        playersView.addPlayer(me)
        initPlayerDataHandler()
    }

    private fun initPlayerDataHandler()
    {
        val onDataChange: (String?, Color?, String?) -> Unit = { name, color, symbol ->
            name?.let { me.name = it }
            color?.let { me.color = it }
            symbol?.let { me.symbol = it }
            playersView.updatePlayer(me)
            connection.sendPacket(ClientSetPlayerDataPacket(name, color, symbol))
        }
        playerDataHandler.onDataChange = onDataChange

        onDataChange(playerDataHandler.name, playerDataHandler.color, playerDataHandler.symbol)

        playerDataHandler.onReady = {
            connection.sendPacket(ClientReadyPacket(true))
        }
    }

    fun receivePacket(serverPacket: ServerPacket)
    {
        when (serverPacket)
        {
            is ServerClientReadyAckPacket -> handleServerClientReadyAckPacket(serverPacket)
            is ServerAddPlayerPacket -> handleServerAddPlayerPacket(serverPacket)
            is ServerSetPlayerDataPacket -> handleServerSetPlayerDataPacket(serverPacket)
        }
    }

    private fun handleServerSetPlayerDataPacket(packet: ServerSetPlayerDataPacket)
    {
        val player = players[packet.playerId] ?: return
        packet.name?.let { player.name = it }
        packet.color?.let { player.color = it }
        packet.symbol?.let { player.symbol = it }
        playersView.updatePlayer(player)
    }

    private fun handleServerAddPlayerPacket(packet: ServerAddPlayerPacket)
    {
        val player = Player(packet.playerId, null, null, null)
        addPlayer(player)
    }

    private fun handleServerClientReadyAckPacket(packet: ServerClientReadyAckPacket)
    {
        playerDataHandler.setReady(if (packet.value) null else packet.reason)
    }

    fun addPlayer(player: Player)
    {
        players[player.id] = player
        playersView.addPlayer(player)
    }

    fun removePlayer(player: Player)
    {
        players.remove(player.id)
    }
}
