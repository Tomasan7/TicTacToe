package me.tomasan7.tictactoe.server.game

interface WinChecker<T>
{
    fun checkWin(board: Array<Array<T?>>, x: Int, y: Int, winLength: Int): CheckerResult<T>?
    fun checkWin(board: Array<Array<T?>>, winLength: Int): CheckerResult<T>?
    {
        for (x in board.indices)
            for (y in board[x].indices)
            {
                val winningLine = checkWin(board, x, y, winLength)
                if (winningLine != null)
                    return winningLine
            }

        return null
    }

    data class CheckerResult<T>(val x1: Int, val y1: Int, val x2: Int, val y2: Int, val winner: T)
}
