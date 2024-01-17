package me.tomasan7.tictactoe.server.game.packet.client

interface ClientPacketDeserializer
{
    fun deserializePacket(serializedPacket: String): ClientPacket
}
