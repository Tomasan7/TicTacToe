package me.tomasan7.tictactoe.server.network.packet.server

interface ServerPacketSerializer
{
    fun serializePacket(packet: ServerPacket): String
}
