package me.tomasan7.tictactoe.game

data class GameOptions(
    val width: Int,
    val height: Int,
    val winLength: Int,
    val symbolSize: Int,
    val maxPlayers: Int,
    val public: Boolean
)
