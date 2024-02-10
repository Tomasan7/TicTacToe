package me.tomasan7.tictactoe.web.view

import me.tomasan7.tictactoe.util.Color
import me.tomasan7.tictactoe.web.Player
import org.w3c.dom.*

class HtmlPlayerView(
    private val template: HTMLTemplateElement,
    private val player: Player,
    private val symbolSize: Int
) : PlayerView
{
    val element = template.content.cloneNode(true).asDynamic().children[0] as HTMLElement
    private val nameElement = element.querySelector(".player-name") as HTMLElement
    private val symbolElement = element.querySelector(".player-symbol-canvas") as HTMLCanvasElement
    private val symbolCanvas =
        MonoPixelCanvas(symbolElement, symbolSize, symbolSize, Color.TRANSPARENT, player.color?: Color.BLACK)

    init
    {
        update()
    }

    override fun update()
    {
        element.id = "player-card-${player.id}"
        nameElement.textContent = player.name ?: "Player ${player.id}"
        symbolCanvas.onColor = player.color ?: Color.BLACK
        player.symbol?.let { symbolCanvas.set(symbolToPixelData(it)) }
        if (player.ready)
            element.classList.add("player-ready")
        else
            element.classList.remove("player-ready")
        if (player.onTurn)
            element.classList.add("player-on-turn")
        else
            element.classList.remove("player-on-turn")
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
