package me.tomasan7.tictactoe.game

import me.tomasan7.tictactoe.game.packet.ClientPacket
import me.tomasan7.tictactoe.game.session.Session

class Player(val name: String, val char: Char, private val session: Session, val id: Int)
{
    suspend fun sendPacket(packet: ClientPacket)
    {
        session.sendPacket(packet)
    }
}
