package me.tomasan7.tictactoe.server.game

import kotlin.test.Test
import kotlin.test.assertEquals

class WinCheckerImplTest
{
    private val winChecker: WinChecker<Int> = WinCheckerImpl()

    @Test
    fun checkWinHorizontal()
    {
        val board = createBoard(
            """
            0 0 0
            1 1 1
            0 0 0
            """
        )

        val expected = WinChecker.CheckerResult(
            x1 = 0,
            y1 = 1,
            x2 = 2,
            y2 = 1,
            winner = 1
        )
        val actual = winChecker.checkWin(board, 1, 1, 3)

        assertEquals(expected, actual)
    }

    @Test
    fun checkWinVertical()
    {
        val board = createBoard(
            """
            0 1 0
            0 1 0
            0 1 0
            """
        )

        val expected = WinChecker.CheckerResult(
            x1 = 1,
            y1 = 0,
            x2 = 1,
            y2 = 2,
            winner = 1
        )
        val actual = winChecker.checkWin(board, 1, 1, 3)

        assertEquals(expected, actual)
    }

    @Test
    fun checkWinSWNEDiagonal()
    {
        val board = createBoard(
            """
            0 0 1
            0 1 0
            1 0 0
            """
        )

        val expected = WinChecker.CheckerResult(
            x1 = 0,
            y1 = 2,
            x2 = 2,
            y2 = 0,
            winner = 1
        )
        val actual = winChecker.checkWin(board, 1, 1, 3)

        assertEquals(expected, actual)
    }

    @Test
    fun checkWinNWSEDiagonal()
    {
        val board = createBoard(
            """
            1 0 0
            0 1 0
            0 0 1
            """
        )

        val expected = WinChecker.CheckerResult(
            x1 = 0,
            y1 = 0,
            x2 = 2,
            y2 = 2,
            winner = 1
        )
        val actual = winChecker.checkWin(board, 1, 1, 3)

        assertEquals(expected, actual)
    }

    @Test
    fun checkWinNoWin()
    {
        val board = createBoard(
            """
            0 1 0
            1 1 0
            1 0 1
            """
        )

        val expected = null
        val actual = winChecker.checkWin(board, 1, 1, 3)

        assertEquals(expected, actual)
    }

    private fun createBoard(string: String): Array<Array<Int?>>
    {
        val trimmed = string.trimIndent().trim()
        val noSpace = trimmed.replace("[^\\S\\n]".toRegex(), "")
        val width = noSpace.indexOf('\n')
        val height = noSpace.count { it == '\n' } + 1
        val transformed = string.replace("\\s".toRegex(), "")

        val board = Array(width) { Array<Int?>(height) { null } }

        for ((i, char) in transformed.withIndex())
        {
            val x = i % width
            val y = i / width

            val player = try
            {
                char.toString().toInt()
            }
            catch (e: NumberFormatException)
            {
                null
            }

            board[x][y] = player
        }

        return board
    }
}
