package me.tomasan7.tictactoe.server.game

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.tomasan7.tictactoe.game.GameOptions
import me.tomasan7.tictactoe.protocol.packet.client.ClientPacket
import me.tomasan7.tictactoe.protocol.packet.client.packet.ClientPlaceSymbolPacket
import me.tomasan7.tictactoe.protocol.packet.client.packet.ClientReadyPacket
import me.tomasan7.tictactoe.protocol.packet.client.packet.ClientSetPlayerDataPacket
import me.tomasan7.tictactoe.protocol.packet.server.ServerPacket
import me.tomasan7.tictactoe.protocol.packet.server.packet.*

class Game(val code: String, val options: GameOptions)
{
    private val playerIdMap: MutableMap<Int, Player> = mutableMapOf()
    private val players: Set<Player>
        get() = playerIdMap.values.toSet()

    private lateinit var orderedPlayers: List<Player>

    /** The first connected player is the owner */
    private val ownerPlayer: Player?
        get() = playerIdMap[0]
    private var playerIdCounter = 0

    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private val clientPacketHandler = ClientPacketHandler()
    private var playerOnTurnIndex: Int = -1
    private val playerOnTurn: Player
        get() = orderedPlayers[playerOnTurnIndex]

    var state: GameState = GameState.WAITING_FOR_PLAYERS
        private set

    private val board = Array(options.width) { Array<Player?>(options.height) { null } }
    private val winChecker: WinChecker<Player> = WinCheckerImpl()

    fun addNewPlayer(session: ClientSession): Boolean
    {
        if (players.size >= options.maxPlayers)
            return false

        if (state != GameState.WAITING_FOR_PLAYERS)
            return false

        val playerId = playerIdCounter++
        val player = Player(session, playerId)
        playerIdMap[playerId] = player
        coroutineScope.launch {
            val serverJoinGamePacket = constructServerJoinGamePacket(player)
            player.sendPacket(serverJoinGamePacket)
            broadcastPacketExcept(ServerAddPlayerPacket(player.id), player)
            for (otherPlayer in playersExcept(player))
            {
                player.sendPacket(ServerAddPlayerPacket(otherPlayer.id))
                player.sendPacket(constructServerSetPlayerDataPacket(otherPlayer))
                if (otherPlayer.ready)
                    player.sendPacket(ServerPlayerReadyPacket(otherPlayer.id, true))
            }
            session.incomingPackets.collect { packet ->
                if (packet !is ClientSession.TerminationPacket)
                    clientPacketHandler.handle(packet, player)
                else
                {
                    removePlayer(player)
                    return@collect
                }
            }
        }

        return true
    }

    private fun constructServerJoinGamePacket(player: Player) = ServerJoinGamePacket(
        code,
        options.width,
        options.height,
        options.winLength,
        options.symbolSize,
        options.maxPlayers,
        options.public,
        player.id,
        ownerPlayer!!.id
    )

    private fun constructServerSetPlayerDataPacket(player: Player) = ServerSetPlayerDataPacket(
        player.id,
        player.name,
        player.color,
        player.symbol
    )

    private fun Player.sendServerPacket(packet: ServerPacket)
    {
        coroutineScope.launch {
            sendPacket(packet)
        }
    }

    fun removePlayer(player: Player)
    {
        playerIdMap.remove(player.id)
        coroutineScope.launch {
            broadcastPacket(ServerRemovePlayerPacket(player.id))
        }
    }

    fun checkWin(x: Int, y: Int, player: Player)
    {
        val winResult = winChecker.checkWin(board, x, y, options.winLength)
        if (winResult != null && winResult.winner === player)
        {
            state = GameState.ENDING
            broadcastPacket(ServerGameEndPacket(player.id))
        }
    }

    private fun playersExcept(player: Player): Set<Player>
    {
        return players.filter { it !== player }.toSet()
    }

    fun broadcastPacket(packet: ServerPacket)
    {
        coroutineScope.launch {
            players.forEach { it.sendPacket(packet) }
        }
    }

    fun broadcastPacketExcept(packet: ServerPacket, except: Player)
    {
        coroutineScope.launch {
            players.forEach { if (it != except) it.sendPacket(packet) }
        }
    }

    fun broadcastPacketExcept(packet: ServerPacket, except: Int)
    {
        coroutineScope.launch {
            players.forEach { if (it.id != except) it.sendPacket(packet) }
        }
    }

    fun getPlayerById(id: Int): Player?
    {
        return playerIdMap[id]
    }

    private fun tryStartGame()
    {
        if (players.size < 2)
            return

        val allPlayersReady = players.all { it.ready }

        if (!allPlayersReady)
            return

        state = GameState.PLAYING
        orderedPlayers = players.shuffled()
        broadcastPacket(ServerStartGamePacket(orderedPlayers.map { it.id }.toTypedArray()))
        playerOnTurnIndex = 0
        broadcastPacket(ServerPlayerTurnPacket(playerOnTurn.id))
    }

    private inner class ClientPacketHandler
    {
        fun handle(packet: ClientPacket, player: Player)
        {
            when (packet)
            {
                is ClientSetPlayerDataPacket -> handleSetPlayerData(packet, player)
                is ClientReadyPacket -> handleSetReady(packet, player)
                is ClientPlaceSymbolPacket -> handlePlaceSymbol(packet, player)
            }
        }

        private fun handlePlaceSymbol(packet: ClientPlaceSymbolPacket, player: Player)
        {
            if (player !== playerOnTurn)
                return

            if (board[packet.x][packet.y] != null)
                return

            if (packet.x !in 0..<options.width || packet.y !in 0..<options.height)
                return

            board[packet.x][packet.y] = player
            broadcastPacket(ServerPlaceSymbolPacket(player.id, packet.x, packet.y))
            playerOnTurnIndex = (playerOnTurnIndex + 1) % orderedPlayers.size
            broadcastPacket(ServerPlayerTurnPacket(playerOnTurn.id))
            checkWin(packet.x, packet.y, player)
        }

        private fun handleSetPlayerData(packet: ClientSetPlayerDataPacket, player: Player)
        {
            /* If these are null, ith means they haven't changed */
            val newName = packet.name
            val newColor = packet.color
            val newSymbol = packet.symbol

            if (newName != null)
                player.name = newName
            if (newColor != null)
                player.color = newColor
            if (newSymbol != null && newSymbol.length == options.symbolSize*options.symbolSize)
                player.symbol = newSymbol

            broadcastPacketExcept(ServerSetPlayerDataPacket(player.id, newName, newColor, newSymbol), player)
        }

        private fun handleSetReady(packet: ClientReadyPacket, player: Player)
        {
            if (player.ready == packet.value)
                return

            if (packet.value)
            {
                val nameSymbolOrColorSameAsAnotherReadyPlayer = playersExcept(player)
                    .filter { it.ready }
                    .any { nameSymbolOrColorSame(it, player) }

                val nameSymbolOrColorNotSet = player.name.isNullOrBlank()
                        || player.symbol.isNullOrBlank()
                        || player.symbol == "0".repeat(options.symbolSize*options.symbolSize) /* Symbol is empty */
                        || player.color == null

                if (nameSymbolOrColorSameAsAnotherReadyPlayer)
                    player.sendServerPacket(
                        ServerClientReadyAckPacket(
                            false,
                            "Name, symbol, or color is the same as another player's"
                        )
                    )
                else if (nameSymbolOrColorNotSet)
                    player.sendServerPacket(ServerClientReadyAckPacket(false, "Name, symbol, or color is not set"))
                else
                {
                    player.ready = true
                    player.sendServerPacket(ServerClientReadyAckPacket(true, null))
                    broadcastPacketExcept(ServerPlayerReadyPacket(player.id, true), player)
                        tryStartGame()
                }
            }
            else
            {
                player.ready = false
                player.sendServerPacket(ServerClientReadyAckPacket(false, null))
                broadcastPacketExcept(ServerPlayerReadyPacket(player.id, false), player)
            }
        }

        private fun nameSymbolOrColorSame(player1: Player, player2: Player): Boolean
        {
            return player1.name == player2.name || player1.symbol == player2.symbol || player1.color == player2.color
        }
    }
}
