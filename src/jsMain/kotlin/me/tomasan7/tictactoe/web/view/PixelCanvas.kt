package me.tomasan7.tictactoe.web.view

import me.tomasan7.tictactoe.util.Color
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import kotlin.math.floor
import org.w3c.dom.events.MouseEvent as JsMouseEvent

class PixelCanvas(
    private val canvas: HTMLCanvasElement,
    val width: Int,
    val height: Int
)
{
    val context = canvas.getContext("2d") as CanvasRenderingContext2D
    var onMouseClick: ((MouseEvent) -> Unit)? = null
    var onMouseMove: ((MouseEvent) -> Unit)? = null
    var onMouseDown: ((MouseEvent) -> Unit)? = null
    var onMouseUp: ((MouseEvent) -> Unit)? = null
    var onMouseEnter: ((MouseEvent) -> Unit)? = null
    var onMouseLeave: ((MouseEvent) -> Unit)? = null

    init
    {
        canvas.width = width
        canvas.height = height
        context.imageSmoothingEnabled = false
        canvas.style.imageRendering = "pixelated"
        canvas.onclick = { onMouseClick?.invoke(it.mouseEvent) }
        canvas.onmousemove = { onMouseMove?.invoke(it.mouseEvent) }
        canvas.onmousedown = { onMouseDown?.invoke(it.mouseEvent) }
        canvas.onmouseup = { onMouseUp?.invoke(it.mouseEvent) }
        canvas.onmouseenter = { onMouseEnter?.invoke(it.mouseEvent) }
        canvas.onmouseleave = { onMouseLeave?.invoke(it.mouseEvent) }
    }

    private val JsMouseEvent.pixelX: Int
        get() =
            canvas.getBoundingClientRect().let { rect -> floor((clientX - rect.left) / rect.width * width).toInt() }

    private val JsMouseEvent.pixelY: Int
        get() =
            canvas.getBoundingClientRect().let { rect -> floor((clientY - rect.top) / rect.height * height).toInt() }

    private val JsMouseEvent.mouseEvent: MouseEvent
        get() = MouseEvent(pixelX, pixelY, this)

    fun setPixel(x: Int, y: Int, color: Color)
    {
        require(x in 0 ..< width && y in 0 ..< height) {
            "Pixel ($x, $y) is out of bounds for canvas ($width, $height)"
        }

        setRectangle(x, y, arrayOf(arrayOf(color)))
    }

    fun setRectangle(rectX: Int, rectY: Int, pixels: Array<Array<Color>>)
    {
        val rectWidth = pixels.size
        val rectHeight = pixels[0].size

        require(rectX + rectWidth <= width && rectY + rectHeight <= height) {
            "Rectangle ($rectX, $rectY) with size ($rectWidth, $rectHeight) is out of bounds for canvas  size (${width}, ${height})"
        }

        val imageData = context.createImageData(rectWidth.toDouble(), rectHeight.toDouble())
        val pixelData = imageData.data.asDynamic()

        for (y in 0 until rectHeight)
            for (x in 0 until rectWidth)
            {
                val color = pixels[x][y]
                val index = (y * rectWidth + x) * 4
                pixelData[index] = color.red.toInt()
                pixelData[index + 1] = color.green.toInt()
                pixelData[index + 2] = color.blue.toInt()
                pixelData[index + 3] = color.alpha.toInt()
            }

        context.putImageData(imageData, rectX.toDouble(), rectY.toDouble())
    }

    fun clearPixel(x: Int, y: Int)
    {
        setPixel(x, y, Color.TRANSPARENT)
    }

    fun clearRectangle(rectX: Int, rectY: Int, rectWidth: Int, rectHeight: Int)
    {
        setRectangle(rectX, rectY, Array(rectWidth) { Array(rectHeight) { Color.TRANSPARENT } })
    }

    /** Pixel canvas mouse event. Contains the clicked pixel. */
    data class MouseEvent(val pixelX: Int, val pixelY: Int, val jsEvent: org.w3c.dom.events.MouseEvent)
}
