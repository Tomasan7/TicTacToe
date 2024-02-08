package me.tomasan7.tictactoe.web.view

import me.tomasan7.tictactoe.util.Color
import org.w3c.dom.HTMLCanvasElement

class MonoPixelCanvas(
    private val canvas: HTMLCanvasElement,
    val width: Int,
    val height: Int,
    offColor: Color,
    onColor: Color
)
{
    var offColor = offColor
        set(value)
        {
            if (field == value)
                return
            field = value
            redraw()
        }
    var onColor = onColor
        set(value)
        {
            if (field == value)
                return
            field = value
            redraw()
        }

    private val pixelCanvas = PixelCanvas(canvas, width, height)

    var onMouseClick by pixelCanvas::onMouseClick
    var onMouseMove by pixelCanvas::onMouseMove
    var onMouseDown by pixelCanvas::onMouseDown
    var onMouseUp by pixelCanvas::onMouseUp
    var onMouseEnter by pixelCanvas::onMouseEnter
    var onMouseLeave by pixelCanvas::onMouseLeave

    /** Two-dimensional boolean array indicating off and on pixels. */
    private val pixelData = Array(width) { Array(height) { false } }

    init
    {
        redraw()
    }

    private fun checkForBounds(x: Int, y: Int)
    {
        require(x in pixelData.indices || y in pixelData[0].indices) {
            "Pixel ($x, $y) is out of bounds for canvas (${pixelData.size}, ${pixelData[0].size})"
        }
    }

    fun setPixel(x: Int, y: Int, value: Boolean)
    {
        checkForBounds(x, y)
        pixelData[x][y] = value
        pixelCanvas.setPixel(x, y, if (value) onColor else offColor)
    }

    operator fun set(x: Int, y: Int, value: Boolean) = setPixel(x, y, value)

    fun getPixel(x: Int, y: Int): Boolean
    {
        checkForBounds(x, y)
        return pixelData[x][y]
    }

    operator fun get(x: Int, y: Int) = getPixel(x, y)

    fun set(pixelData: Array<Array<Boolean>>)
    {
        require(pixelData.size == width && pixelData[0].size == height) {
            "pixelData must be of size ($width, $height)"
        }

        for (x in 0 until width)
            for (y in 0 until height)
                setPixel(x, y, pixelData[x][y])
    }

    fun setRectangle(rectX: Int, rectY: Int, pixels: Array<Array<Boolean>>)
    {
        val rectWidth = pixels.size
        val rectHeight = pixels[0].size

        require(rectX + rectWidth <= width && rectY + rectHeight <= height) {
            "Rectangle ($rectX, $rectY) with size ($rectWidth, $rectHeight) is out of bounds for canvas  size (${width}, ${height})"
        }

        val colorPixels = Array(rectWidth) { Array(rectHeight) { offColor } }

        for (x in 0 until rectWidth)
            for (y in 0 until rectHeight)
            {
                pixelData[x][y] = pixels[x][y]
                colorPixels[x][y] = if (pixels[x][y]) onColor else offColor
            }

        pixelCanvas.setRectangle(rectX, rectY, colorPixels)
    }

    private fun redraw()
    {
        for (x in 0 until width)
            for (y in 0 until height)
                pixelCanvas.setPixel(x, y, if (pixelData[x][y]) onColor else offColor)
    }
}
