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
    private val context = canvas.getContext("2d") as CanvasRenderingContext2D
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

        val imageData = context.createImageData(1.0, 1.0)
        /* Using dynamic, because javascript doesn't properly understand types and signedness */
        /* Without dynamic, it only accepts Byte, which it doesn't properly understand */
        /* So we convert the value to int, because it just has to look like a positive 0-255 integer */
        val pixelData = imageData.data.asDynamic()
        pixelData[0] = color.red.toInt()
        pixelData[1] = color.green.toInt()
        pixelData[2] = color.blue.toInt()
        pixelData[3] = color.alpha.toInt()
        context.putImageData(imageData, x.toDouble(), y.toDouble())
    }

    fun clearPixel(x: Int, y: Int)
    {
        setPixel(x, y, Color.TRANSPARENT)
    }

    /** Pixel canvas mouse event. Contains the clicked pixel. */
    data class MouseEvent(val pixelX: Int, val pixelY: Int, val jsEvent: org.w3c.dom.events.MouseEvent)
}
