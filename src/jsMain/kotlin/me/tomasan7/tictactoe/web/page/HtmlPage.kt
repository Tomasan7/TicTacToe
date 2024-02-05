package me.tomasan7.tictactoe.web.page

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import me.tomasan7.tictactoe.web.HtmlIdMapper
import me.tomasan7.tictactoe.web.hide
import me.tomasan7.tictactoe.web.show
import org.w3c.dom.HTMLElement

abstract class HtmlPage(
    private val containerElement: HTMLElement
) : Page
{
    protected val htmlMapper = HtmlIdMapper(containerElement)
    protected val coroutineScope = CoroutineScope(Dispatchers.Default)

    override fun enter()
    {
        containerElement.show()
    }

    override fun exit()
    {
        coroutineScope.coroutineContext.cancelChildren()
        containerElement.hide()
    }
}
