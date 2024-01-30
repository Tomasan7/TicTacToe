package me.tomasan7.tictactoe.web.view

interface PlayerView
{
    var onTurn: Boolean
    var disconneted: Boolean

    fun update()
}
