package me.tomasan7.tictactoe.web.view

import me.tomasan7.tictactoe.web.Player
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLTemplateElement

class HtmlPlayersView(
    private val symbolSize: Int,
    private val playerViewTemplateElement:  HTMLTemplateElement,
    private val playersContainer: HTMLElement
) : PlayersView
{
    private val playerViews = mutableMapOf<Player, HtmlPlayerView>()

    override fun update()
    {
        playerViews.values.forEach { it.update() }
    }

    override fun updatePlayer(player: Player)
    {
        playerViews[player]?.update()
    }

    override fun addPlayer(player: Player)
    {
        val playerView = HtmlPlayerView(playerViewTemplateElement, player, symbolSize)
        playerViews[player] = playerView
        playersContainer.appendChild(playerView.element)
    }

    override fun removePlayer(player: Player)
    {
        playerViews.remove(player)?.element?.remove()
    }
}
