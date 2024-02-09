package me.tomasan7.tictactoe.web.view

import me.tomasan7.tictactoe.util.Color

interface BoardView
{
    fun drawSymbol(x: Int, y: Int, symbol: String, color: Color)
    fun drawWinningLine(x1: Int, y1: Int, x2: Int, y2: Int)
    fun clearSymbol(x: Int, y: Int)
    fun hide()
    fun show()
    fun clearBoard()
}
