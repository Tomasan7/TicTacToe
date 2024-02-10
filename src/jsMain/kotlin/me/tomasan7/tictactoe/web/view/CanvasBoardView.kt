package me.tomasan7.tictactoe.web.view

import me.tomasan7.tictactoe.util.Color
import me.tomasan7.tictactoe.web.hide
import me.tomasan7.tictactoe.web.show
import org.w3c.dom.HTMLCanvasElement

class CanvasBoardView(
    private val canvas: HTMLCanvasElement,
    val gridColor: Color,
    val width: Int,
    val height: Int,
    val symbolSize: Int,
    val gridWidth: Int
) : BoardView
{
    private val pixelCanvas = PixelCanvas(
        canvas = canvas,
        width = width * symbolSize + (width - 1) * gridWidth,
        height = height * symbolSize + (height - 1) * gridWidth
    )

    init
    {
        drawGrid()
        pixelCanvas.onMouseClick = { event ->
            val spotX = event.pixelX / (symbolSize + gridWidth)
            val spotY = event.pixelY / (symbolSize + gridWidth)
            onSpotClick?.invoke(spotX, spotY)
        }
    }

    private fun drawGrid()
    {
        for (i in 1 until width)
        {
            val x = i * symbolSize + (i - 1) * gridWidth
            pixelCanvas.setRectangle(x, 0, gridWidth, pixelCanvas.height, gridColor)
        }

        for (i in 1 until height)
        {
            val y = i * symbolSize + (i - 1) * gridWidth
            pixelCanvas.setRectangle(0, y, pixelCanvas.width, gridWidth, gridColor)
        }
    }

    override var onSpotClick: ((x: Int, y: Int) -> Unit)? = null

    override fun drawSymbol(x: Int, y: Int, symbol: String, color: Color)
    {
        val pixels = Array(symbolSize) { Array(symbolSize) { Color.TRANSPARENT } }

        for ((i, char) in symbol.withIndex())
        {
            val sX = i % symbolSize
            val sY = i / symbolSize
            pixels[sX][sY] = if (char == '1') color else Color.TRANSPARENT
        }

        pixelCanvas.setRectangle(x * (symbolSize + gridWidth), y * (symbolSize + gridWidth), pixels)
    }

    override fun drawWinningLine(x1: Int, y1: Int, x2: Int, y2: Int)
    {
        TODO("Not yet implemented")
    }

    override fun clearSymbol(x: Int, y: Int)
    {
        /* Color doesn't matter, since the symbol is all empty, resulting in clearing the spot. */
        drawSymbol(x, y, "0".repeat(symbolSize*symbolSize), Color(0u, 0u, 0u))
    }

    override fun hide()
    {
        canvas.hide()
    }

    override fun show()
    {
        canvas.show()
    }

    override fun clearBoard()
    {
        for (x in 0 until width)
            for (y in 0 until height)
                clearSymbol(x, y)
    }
}
