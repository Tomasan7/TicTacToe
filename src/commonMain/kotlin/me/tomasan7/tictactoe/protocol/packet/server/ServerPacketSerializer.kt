package me.tomasan7.tictactoe.protocol.packet.server

interface ServerPacketSerializer
{
    fun serializePacket(packet: ServerPacket): String
}
