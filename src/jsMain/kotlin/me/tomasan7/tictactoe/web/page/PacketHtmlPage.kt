package me.tomasan7.tictactoe.web.page

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.tomasan7.tictactoe.protocol.packet.client.ClientPacket
import me.tomasan7.tictactoe.protocol.packet.server.ServerPacket
import me.tomasan7.tictactoe.web.Connection
import org.w3c.dom.HTMLElement

abstract class PacketHtmlPage(
    containerElement: HTMLElement,
    protected val connection: Connection
) : HtmlPage(containerElement)
{
    override fun enter()
    {
        super.enter()
        coroutineScope.launch {
            connection.incomingPackets.collect {
                withContext(Dispatchers.Main) {
                    onPacket(it)
                }
            }
        }
    }

    protected fun sendPacket(packet: ClientPacket)
    {
        connection.sendPacket(packet)
    }

    open fun onPacket(packet: ServerPacket) {}
}
