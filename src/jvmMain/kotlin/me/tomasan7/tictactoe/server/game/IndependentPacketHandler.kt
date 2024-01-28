package me.tomasan7.tictactoe.server.game

import me.tomasan7.tictactoe.game.GameOptions
import me.tomasan7.tictactoe.protocol.packet.client.ClientPacket
import me.tomasan7.tictactoe.protocol.packet.client.packet.ClientCreateGamePacket
import me.tomasan7.tictactoe.protocol.packet.client.packet.ClientJoinGamePacket
import me.tomasan7.tictactoe.protocol.packet.client.packet.ClientJoinRandomGamePacket

/** Handles packets in the **Independent** category */
class IndependentPacketHandler(
    private val gameManager: GameManager
)
{
    fun handlePacket(packet: ClientPacket, session: ClientSession)
    {
        when (packet)
        {
            is ClientCreateGamePacket -> handleCreateGamePacket(packet, session)
            is ClientJoinGamePacket -> handleJoinGamePacket(packet, session)
            is ClientJoinRandomGamePacket -> handleJoinRandomGamePacket(packet, session)
        }
    }

    private fun handleJoinRandomGamePacket(packet: ClientJoinRandomGamePacket, session: ClientSession)
    {
        val success = gameManager.joinToRandomPublicGame(session)
    }

    private fun handleJoinGamePacket(packet: ClientJoinGamePacket, session: ClientSession)
    {
        val success = gameManager.joinToGame(session, packet.gameCode)
    }

    private fun handleCreateGamePacket(packet: ClientCreateGamePacket, session: ClientSession)
    {
        val options = GameOptions(
            width = packet.width,
            height = packet.height,
            winLength = packet.winLength,
            symbolSize = packet.symbolSize,
            public = packet.public,
            maxPlayers = packet.maxPlayers
        )

        val game = gameManager.createGame(options)
        gameManager.joinToGame(session, game)
    }
}
