package me.tomasan7.tictactoe.protocol.packet.client

interface ClientPacketSerializer
{
    fun serializePacket(packet: ClientPacket): String
    fun deserializePacket(serializedPacket: String): ClientPacket
}
