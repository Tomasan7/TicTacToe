package me.tomasan7.tictactoe.server.game

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.tomasan7.tictactoe.protocol.packet.client.packet.ClientReadyPacket
import me.tomasan7.tictactoe.protocol.packet.client.packet.ClientSetPlayerDataPacket
import me.tomasan7.tictactoe.protocol.packet.server.ServerPacket
import me.tomasan7.tictactoe.protocol.packet.server.packet.*
import me.tomasan7.tictactoe.util.Color

class Game(val code: String, val options: GameOptions)
{
    private val playerIdMap: MutableMap<Int, Player> = mutableMapOf()
    private val players: Set<Player>
        get() = playerIdMap.values.toSet()

    /** The first connected player is the owner */
    private val ownerPlayer: Player?
        get() = playerIdMap[0]
    private var playerIdCounter = 0

    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private val clientPacketHandler = ClientPacketHandler()

    var state: GameState = GameState.WAITING_FOR_PLAYERS
        private set

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
                player.sendPacket(constructServerSetPlayerDataPacket(otherPlayer))
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
        player.id,
        ownerPlayer!!.id
    )

    private fun constructServerSetPlayerDataPacket(player: Player) = ServerSetPlayerDataPacket(
        player.id,
        player.name,
        player.color?.value,
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

    private inner class ClientPacketHandler
    {
        fun handle(packet: me.tomasan7.tictactoe.protocol.packet.client.ClientPacket, player: Player)
        {
            when (packet)
            {
                is ClientSetPlayerDataPacket -> handleSetPlayerData(packet, player)
                is ClientReadyPacket -> handleSetReady(packet, player)
            }
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
                player.color = Color(newColor)
            if (newSymbol != null)
                player.symbol = newSymbol

            broadcastPacketExcept(ServerSetPlayerDataPacket(player.id, newName, newColor, newSymbol), player)
        }

        private fun handleSetReady(packet: ClientReadyPacket, player: Player)
        {
            if (packet.value)
            {
                val nameSymbolOrColorSameAsAnotherPlayer = players
                    .filter { it !== player }
                    .any { nameSymbolOrColorSame(it, player) }

                if (nameSymbolOrColorSameAsAnotherPlayer)
                    player.sendServerPacket(
                        ServerClientReadyAckPacket(
                            false,
                            "Name, symbol, or color is the same as another player's"
                        )
                    )
                else
                {
                    player.ready = true
                    player.sendServerPacket(ServerClientReadyAckPacket(true, null))
                    broadcastPacket(ServerPlayerReadyPacket(player.id, true))
                }
            }
            else
            {
                player.ready = false
                player.sendServerPacket(ServerClientReadyAckPacket(false, null))
                broadcastPacket(ServerPlayerReadyPacket(player.id, false))
            }
        }

        private fun nameSymbolOrColorSame(player1: Player, player2: Player): Boolean
        {
            return player1.name == player2.name || player1.symbol == player2.symbol || player1.color == player2.color
        }
    }
}
