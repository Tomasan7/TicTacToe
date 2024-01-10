package me.tomasan7.tictactoe.game.join

sealed interface JoinState
{
    data object SelectName : JoinState
    data object SelectSymbol: JoinState
    data object SelectColor: JoinState
}
