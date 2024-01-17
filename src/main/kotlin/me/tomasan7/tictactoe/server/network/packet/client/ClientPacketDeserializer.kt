package me.tomasan7.tictactoe.server.network.packet.client

interface ClientPacketDeserializer
{
    fun deserializePacket(serializedPacket: String): ClientPacket
}
