package me.tomasan7.tictactoe.web

import org.w3c.dom.*
import kotlin.reflect.KProperty

class HtmlIdMapper(private val container: HTMLElement)
{
    operator fun getValue(thisRef: Any?, property: KProperty<*>): HTMLElement
    {
        return container.byId(propertyNameToId(property.name))
    }

    private fun propertyNameToId(propertyName: String): String
    {
        return propertyName.replace(Regex("([A-Z])"), "-$1").lowercase()
    }

    inner class Button
    {
        operator fun getValue(thisRef: Any?, property: KProperty<*>): HTMLButtonElement =
            this@HtmlIdMapper.getValue(thisRef, property) as? HTMLButtonElement
                ?: throw IllegalArgumentException("Element is not a button")
    }

    inner class Input
    {
        operator fun getValue(thisRef: Any?, property: KProperty<*>): HTMLInputElement =
            this@HtmlIdMapper.getValue(thisRef, property) as? HTMLInputElement
                ?: throw IllegalArgumentException("Element is not an input")
    }

    inner class Template
    {
        operator fun getValue(thisRef: Any?, property: KProperty<*>): HTMLTemplateElement =
            this@HtmlIdMapper.getValue(thisRef, property) as? HTMLTemplateElement
                ?: throw IllegalArgumentException("Element is not a template")
    }

    inner class Canvas
    {
        operator fun getValue(thisRef: Any?, property: KProperty<*>): HTMLCanvasElement =
            this@HtmlIdMapper.getValue(thisRef, property) as? HTMLCanvasElement
                ?: throw IllegalArgumentException("Element is not a canvas")
    }
}
