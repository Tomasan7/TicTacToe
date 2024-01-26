package me.tomasan7.tictactoe.protocol.packet.client

interface ClientPacketDeserializer
{
    fun deserializePacket(serializedPacket: String): ClientPacket
}
