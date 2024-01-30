package me.tomasan7.tictactoe.web.view

import me.tomasan7.tictactoe.util.Color
import me.tomasan7.tictactoe.web.Player
import org.w3c.dom.*

class HtmlPlayerView(
    private val template: HTMLTemplateElement,
    private val player: Player,
    private val symbolSize: Int,
    override var onTurn: Boolean = false,
    override var disconneted: Boolean = false
) : PlayerView
{
    val element = template.content.cloneNode(true) as HTMLDivElement
    private val nameElement = element.querySelector(".name") as HTMLElement
    private val symbolElement = element.querySelector(".symbol") as HTMLCanvasElement
    private val symbolCanvas =
        MonoPixelCanvas(symbolElement, symbolSize, symbolSize, Color.TRANSPARENT, player.color?: Color.BLACK)

    override fun update()
    {
        nameElement.textContent = player.name
        symbolCanvas.onColor = player.color?: Color.BLACK
        player.symbol?.let { symbolCanvas.set(symbolToPixelData(it)) }
        // TODO: On turn and disconnected not yet implemented
    }

    private fun symbolToPixelData(symbol: String): Array<Array<Boolean>>
    {
        val pixelData = Array(symbolSize) { Array(symbolSize) { false } }

        for ((i, char) in symbol.withIndex())
        {
            val x = i % symbolSize
            val y = i / symbolSize
            pixelData[x][y] = char == '1'
        }

        return pixelData
    }
}
