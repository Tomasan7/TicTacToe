package me.tomasan7.tictactoe.web

import me.tomasan7.tictactoe.util.Color

data class Player(
    val id: Int,
    var name: String? = null,
    var symbol: String? = null,
    var color: Color? = null,
    var ready: Boolean = false,
    var onTurn: Boolean = false
)
{
    override fun hashCode() = id

    override fun equals(other: Any?): Boolean
    {
        if (this === other) return true
        if (other == null || this::class.js != other::class.js) return false

        other as Player

        return id == other.id
    }
}
