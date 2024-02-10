package me.tomasan7.tictactoe.web.view

import me.tomasan7.tictactoe.web.Player

interface PlayersView
{
    fun update()
    fun updatePlayer(player: Player)
    fun addPlayer(player: Player)
    fun removePlayer(player: Player)
    fun setOrder(players: List<Player>)
}
