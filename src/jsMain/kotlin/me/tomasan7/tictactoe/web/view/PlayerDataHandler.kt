package me.tomasan7.tictactoe.web.view

import me.tomasan7.tictactoe.util.Color
import me.tomasan7.tictactoe.web.HtmlIdMapper
import me.tomasan7.tictactoe.web.hide
import me.tomasan7.tictactoe.web.show
import org.w3c.dom.HTMLElement
import kotlin.experimental.and

class PlayerDataHandler(
    private val playerDataForm: HTMLElement,
    symbolSize: Int,
)
{
    var onDataChange: (name: String?, color: Color?, symbol: String?) -> Unit = { _, _, _ -> }
    var onReady: () -> Unit = {}

    private val htmlMapper = HtmlIdMapper(playerDataForm)

    private val elements = object
    {
        val playerDataNameInput by htmlMapper.Input()
        val playerDataColorInput by htmlMapper.Input()
        val playerDataSymbolInput by htmlMapper.Canvas()
        val playerReadyButton by htmlMapper.Button()
        val playerReadyIndicator by htmlMapper
    }

    private var isReady = false
        set(value)
        {
            if (value)
            {
                elements.playerReadyButton.innerText = "Unready"
                elements.playerReadyIndicator.innerText = ""
            }
            else
                elements.playerReadyButton.innerText = "Ready"

            field = value
        }

    private val symbolCanvas = MonoPixelCanvas(
        canvas = elements.playerDataSymbolInput,
        width = symbolSize,
        height = symbolSize,
        offColor = Color.TRANSPARENT,
        onColor = Color.fromCssHexString(elements.playerDataColorInput.value)
    )

    val name: String
        get() = elements.playerDataNameInput.value

    val color: Color
        get() = Color.fromCssHexString(elements.playerDataColorInput.value)

    val symbol: String
        get() = pixelDataToSymbol(symbolCanvas.pixelData)

    init
    {
        initNameInput()
        initColorInput()
        initSymbolCanvas()
        initReadyButton()
    }

    private fun initReadyButton()
    {
        val readyButton = elements.playerReadyButton

        readyButton.onclick = { event ->
            onReady()
        }
    }

    fun setReady(error: String?)
    {
        val readyIndicator = elements.playerReadyIndicator
        if (error == null)
        {
            readyIndicator.innerText = "Ready"
            isReady = true
        }
        else
        {
            readyIndicator.innerText = error
            isReady = false
        }
    }

    fun hide()
    {
        playerDataForm.hide()
    }

    fun show()
    {
        playerDataForm.show()
    }

    private fun dataChanged(name: String?, color: Color?, symbol: String?)
    {
        if (isReady)
            return

        onDataChange(name, color, symbol)
    }

    private fun initNameInput()
    {
        elements.playerDataNameInput.oninput = { dataChanged(name, null, null) }
    }

    private fun initColorInput()
    {
        elements.playerDataColorInput.oninput = {
            symbolCanvas.onColor = color
            dataChanged(null, symbolCanvas.onColor, null)
        }
    }

    private fun initSymbolCanvas()
    {
        elements.playerDataSymbolInput.oncontextmenu = { event -> event.preventDefault(); false }

        fun symbolClick(x: Int, y: Int, left: Boolean, right: Boolean)
        {
            if (right && left)
                return
            if (!right && !left)
                return

            val previousValue = symbolCanvas.pixelData[x][y]
            val newValue = left

            if (previousValue == newValue)
                return

            symbolCanvas.setPixel(x, y, newValue)
            dataChanged(null, null, symbol)
        }
        symbolCanvas.onMouseMove = onmousemove@{ event ->
            val left = event.jsEvent.buttons and 1.toShort() == 1.toShort()
            val right = event.jsEvent.buttons and 2.toShort() == 2.toShort()
            symbolClick(event.pixelX, event.pixelY, left, right)
        }
        symbolCanvas.onMouseDown = onclick@{ event ->
            val left = event.jsEvent.button in 0.toShort()..1.toShort()
            val right = event.jsEvent.button == 2.toShort()
            symbolClick(event.pixelX, event.pixelY, left, right)
        }
    }

    private fun pixelDataToSymbol(pixelData: Array<Array<Boolean>>): String
    {
        val symbolWidth = pixelData.size
        val symbolHeight = pixelData[0].size

        return buildString {
            for (y in 0..<symbolHeight)
                for (x in 0..<symbolWidth)
                    append(if (pixelData[x][y]) '1' else '0')
        }
    }
}
