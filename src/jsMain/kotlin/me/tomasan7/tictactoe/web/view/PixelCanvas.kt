package me.tomasan7.tictactoe.web.view

import me.tomasan7.tictactoe.util.Color
import org.khronos.webgl.set
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement

class PixelCanvas(
    private val canvas: HTMLCanvasElement,
    val width: Int,
    val height: Int,
    val onPixelClick: ((x: Int, y: Int) -> Unit)? = null
)
{
    private val context = canvas.getContext("2d") as CanvasRenderingContext2D

    init
    {
        canvas.width = width
        canvas.height = height
        context.imageSmoothingEnabled = false
        canvas.style.imageRendering = "pixelated"
        canvas.onclick = {
            val (x, y) = getCanvasPixelFromOffset(it.offsetX, it.offsetY)
            onPixelClick?.invoke(x, y)
        }
    }

    private fun getCanvasPixelFromOffset(offsetX: Double, offsetY: Double): Pair<Int, Int> {
        // Ensure the coordinates are within the canvas bounds
        val x = offsetX.coerceIn(0.0, canvas.width.toDouble() - 1)
        val y = offsetY.coerceIn(0.0, canvas.height.toDouble() - 1)

        return Pair(x.toInt(), y.toInt())
    }

    fun setPixel(x: Int, y: Int, color: Color)
    {
        if (x < 0 || x >= width || y < 0 || y >= height)
            throw IllegalArgumentException("Pixel ($x, $y) is out of bounds for canvas ($width, $height)")

        val imageData = context.createImageData(1.0, 1.0)
        val pixelData = imageData.data
        pixelData[0] = color.red.toByte()
        pixelData[1] = color.green.toByte()
        pixelData[2] = color.blue.toByte()
        pixelData[3] = color.alpha.toByte()
        context.putImageData(imageData, x.toDouble(), y.toDouble())
    }

    fun clearPixel(x: Int, y: Int)
    {
        setPixel(x, y, Color.TRANSPARENT)
    }
}
