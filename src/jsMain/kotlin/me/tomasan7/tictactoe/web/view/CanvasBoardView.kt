package me.tomasan7.tictactoe.web.view

import me.tomasan7.tictactoe.util.Color
import org.khronos.webgl.set
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.ImageData

class CanvasBoardView(
    private val canvas: HTMLCanvasElement,
    val gridColor: String,
    val width: Int,
    val height: Int,
    val symbolSize: Int,
    val gridWidth: Int
) : BoardView
{
    private val context = canvas.getContext("2d") as CanvasRenderingContext2D

    init
    {
        canvas.width = width * symbolSize + (width - 1) * gridWidth
        canvas.height = height * symbolSize + (height - 1) * gridWidth
        context.imageSmoothingEnabled = false
        canvas.style.imageRendering = "pixelated"
        drawGrid()
    }

    private fun drawGrid()
    {
        context.strokeStyle = gridColor
        context.lineWidth = gridWidth.toDouble()

        for (i in 1 until width)
        {
            val x = i * (symbolSize + gridWidth) - gridWidth / 2
            context.beginPath()
            context.moveTo(x.toDouble(), 0.0)
            context.lineTo(x.toDouble(), canvas.height.toDouble())
            context.stroke()
        }

        for (i in 1 until height)
        {
            val y = i * (symbolSize + gridWidth) - gridWidth / 2
            context.beginPath()
            context.moveTo(0.0, y.toDouble())
            context.lineTo(canvas.width.toDouble(), y.toDouble())
            context.stroke()
        }
    }

    fun symbolToImageData(symbol: String, color: Color): ImageData
    {
        val imageData = context.createImageData(symbolSize.toDouble(), symbolSize.toDouble())
        val pixelData = imageData.data

        for ((i, char) in symbol.withIndex())
        {
            val pixelIndex = i * 4 // Each pixel has 4 values (R, G, B, A)
            if (char == '1')
            {
                pixelData[0] = color.red.toByte()
                pixelData[1] = color.green.toByte()
                pixelData[2] = color.blue.toByte()
                pixelData[3] = 255.toByte() // Alpha component (fully opaque)
            }
            else
                pixelData[pixelIndex + 3] = 0 // Alpha component (fully transparent)
        }

        return imageData
    }

    private fun putImageData(imageData: ImageData, boardX: Int, boardY: Int)
    {
        context.putImageData(
            imagedata = imageData,
            dx = (boardX * (symbolSize + gridWidth)).toDouble(),
            dy = (boardY * (symbolSize + gridWidth)).toDouble()
        )
    }

    override fun drawSymbol(x: Int, y: Int, symbol: String, color: Color)
    {
        val imageData = symbolToImageData(symbol, color)
        putImageData(imageData, x, y)
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

    override fun clearBoard()
    {
        for (x in 0 until width)
            for (y in 0 until height)
                clearSymbol(x, y)
    }
}
