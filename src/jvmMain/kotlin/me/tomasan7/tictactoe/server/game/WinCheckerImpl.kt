package me.tomasan7.tictactoe.server.game

import kotlin.math.min

class WinCheckerImpl<T> : WinChecker<T>
{
    override fun checkWin(board: Array<Array<T?>>, x: Int, y: Int, winLength: Int): WinChecker.CheckerResult<T>?
    {
        val horizontal = checkHorizontal(board, x, y, winLength)
        if (horizontal != null)
            return horizontal

        val vertical = checkVertical(board, x, y, winLength)
        if (vertical != null)
            return vertical

        val swneDiagonal = checkSWNEDiagonal(board, x, y, winLength)
        if (swneDiagonal != null)
            return swneDiagonal

        val nwseDiagonal = checkNWSEDiagonal(board, x, y, winLength)
        if (nwseDiagonal != null)
            return nwseDiagonal

        return null
    }

    private fun checkHorizontal(board: Array<Array<T?>>, x: Int, y: Int, winLength: Int): WinChecker.CheckerResult<T>?
    {
        // TODO: This is not very efficient. We are checking the entire row, instead of going from the point

        val width = board.size

        /* Going from left to right */
        var lineStart = 0 to y
        var lastPlayer = board[lineStart.first][lineStart.second]

        for (bX in 0..<width)
        {
            val player = board[bX][y]

            if (player == null || player != lastPlayer)
            {
                lineStart = bX to y
                lastPlayer = player
            }

            val lineLength = bX - lineStart.first + 1

            if (lineLength >= winLength)
                return WinChecker.CheckerResult(lineStart.first, lineStart.second, bX, y, lastPlayer!!)
        }

        return null
    }

    private fun checkVertical(board: Array<Array<T?>>, x: Int, y: Int, winLength: Int): WinChecker.CheckerResult<T>?
    {
        // TODO: This is not very efficient. We are checking the entire column, instead of going from the point

        val height = board[0].size

        /* Going from top to bottom */
        var lineStart = x to 0
        var lastPlayer = board[lineStart.first][lineStart.second]

        for (bY in 0..<height)
        {
            val player = board[x][bY]

            if (player == null || player != lastPlayer)
            {
                lineStart = x to bY
                lastPlayer = player
            }

            val lineLength = bY - lineStart.second + 1

            if (lineLength >= winLength)
                return WinChecker.CheckerResult(lineStart.first, lineStart.second, x, bY, lastPlayer!!)
        }

        return null
    }

    private fun checkSWNEDiagonal(board: Array<Array<T?>>, x: Int, y: Int, winLength: Int): WinChecker.CheckerResult<T>?
    {
        // TODO: This is not very efficient. We are checking the entire diagonal, instead of going from the point
        /* Going from  */

        val width = board.size
        val height = board[0].size

        /* Going from bottom-left to top-right */
        val diagonalStart = x - min(x, y) to y - min(x, y)
        val diagonalEnd = x + min(width - x - 1, height - y - 1) to y + min(width - x - 1, height - y - 1)

        var lineStart = diagonalStart
        var lastPlayer = board[lineStart.first][lineStart.second]

        for (bX in diagonalStart.first..diagonalEnd.first)
        {
            val bY = bX - diagonalStart.first + diagonalStart.second
            val player = board[bX][bY]

            if (player == null || player != lastPlayer)
            {
                lineStart = bX to bY
                lastPlayer = player
            }

            val lineLength = bX - lineStart.first + 1

            if (lineLength >= winLength)
                return WinChecker.CheckerResult(lineStart.first, lineStart.second, bX, bY, lastPlayer!!)
        }

        return null
    }

    private fun checkNWSEDiagonal(board: Array<Array<T?>>, x: Int, y: Int, winLength: Int): WinChecker.CheckerResult<T>?
    {
        // TODO: This is not very efficient. We are checking the entire diagonal, instead of going from the point
        /* Going from  */

        val width = board.size
        val height = board[0].size

        /* Going from top-left to bottom-right */
        val diagonalStart = x - min(x, height - y - 1) to y + min(x, height - y - 1)
        val diagonalEnd = x + min(width - x - 1, y) to y - min(width - x - 1, y)

        var lineStart = diagonalStart
        var lastPlayer = board[lineStart.first][lineStart.second]

        for (bX in diagonalStart.first..diagonalEnd.first)
        {
            val bY = diagonalStart.second - bX + diagonalStart.first
            val player = board[bX][bY]

            if (player == null || player != lastPlayer)
            {
                lineStart = bX to bY
                lastPlayer = player
            }

            val lineLength = bX - lineStart.first + 1

            if (lineLength >= winLength)
                return WinChecker.CheckerResult(lineStart.first, lineStart.second, bX, bY, lastPlayer!!)
        }

        return null
    }
}
