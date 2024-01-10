package me.tomasan7.tictactoe.game.session

import me.tomasan7.tictactoe.game.Player
import java.util.UUID

class Game(private val admin: Player)
{
    private val id: UUID = UUID.randomUUID()
    private val players: MutableMap<Int, Player> = mutableMapOf()
    private val playerIdCounter = 1

    init
    {
        players
    }
}
