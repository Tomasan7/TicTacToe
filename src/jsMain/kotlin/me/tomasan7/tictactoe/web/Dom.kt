package me.tomasan7.tictactoe.web

import org.w3c.dom.*

class Dom(private val document: Document)
{
    val homePageContainer = byId<HTMLElement>("homepage")
    val gamePageContainer = byId<HTMLElement>("gamepage")
    val homePage = HomePage()
    val gamePage = GamePage()

    inner class HomePage
    {
        val createGameButton = byId<HTMLButtonElement>("create-game-button")
        val joinGameButton = byId<HTMLButtonElement>("join-game-button")
        val gameCodeInput = byId<HTMLButtonElement>("game-code-input")
    }

    inner class GamePage
    {
        val playersContainer = byId<HTMLElement>("players-container")
    }

    private fun <T : Element> byId(id: String): T
    {
        return document.getElementById(id) as? T ?: throw IllegalArgumentException("Element with id '$id' not found.")
    }
}
