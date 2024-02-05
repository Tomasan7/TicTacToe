package me.tomasan7.tictactoe.web

import org.w3c.dom.Element
import org.w3c.dom.HTMLElement

fun HTMLElement.hide()
{
    this.asDynamic().displayBeforeHidden = this.style.display
    this.style.display = "none"
}

fun HTMLElement.show()
{
    val displayBeforeHidden = this.asDynamic().displayBeforeHidden as? String ?: return
    this.style.display = displayBeforeHidden
}

fun <T : Element> HTMLElement.byId(id: String): T
{
    return querySelector("#$id") as? T ?: throw IllegalArgumentException("Element with id '$id' not found.")
}
