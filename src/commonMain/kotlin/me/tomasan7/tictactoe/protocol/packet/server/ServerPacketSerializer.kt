package me.tomasan7.tictactoe.protocol.packet.server

interface ServerPacketSerializer
{
    fun serializePacket(packet: ServerPacket): String
    fun deserializePacket(serializedPacket: String): ServerPacket
}
