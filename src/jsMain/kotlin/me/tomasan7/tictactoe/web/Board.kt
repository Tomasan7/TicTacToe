package me.tomasan7.tictactoe.web

class Board(
    val width: Int,
    val height: Int,
    val updateCallback: ((x: Int, y: Int, playerId: Int?) -> Unit)? = null
)
{
    /** Two-dimensional array of player ids, whose symbols are at the spots, or `null` if there is nothing. */
    private val board: Array<Array<Int?>> = Array(width) { Array(height) { null } }

    fun getSpot(x: Int, y: Int): Int?
    {
        return board[x][y]
    }

    fun setSpot(x: Int, y: Int, playerId: Int)
    {
        board[x][y] = playerId
        updateCallback?.invoke(x, y, playerId)
    }

    fun clearSpot(x: Int, y: Int)
    {
        board[x][y] = null
        updateCallback?.invoke(x, y, null)
    }

    fun clearBoard()
    {
        for (x in 0 until width)
            for (y in 0 until height)
            {
                clearSpot(x, y)
                updateCallback?.invoke(x, y, null)
            }
    }
}
