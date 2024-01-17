package me.tomasan7.tictactoe.server.game.packet.server

interface ServerPacketSerializer
{
    fun serializePacket(packet: ServerPacket): String
}
