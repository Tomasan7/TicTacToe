package me.tomasan7.tictactoe.game.packet

/** Packet sent to clients */
interface ClientPacket: Packet
{
    fun serialize(): String
}
