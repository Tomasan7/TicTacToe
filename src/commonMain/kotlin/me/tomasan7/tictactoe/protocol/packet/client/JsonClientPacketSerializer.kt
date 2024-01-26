package me.tomasan7.tictactoe.protocol.packet.client

import me.tomasan7.tictactoe.protocol.packet.JsonPacketSerializer

class JsonClientPacketSerializer(
    private val jsonPacketSerializer: JsonPacketSerializer = JsonPacketSerializer()
) : ClientPacketSerializer
{
    override fun serializePacket(packet: ClientPacket): String
    {
        return jsonPacketSerializer.serializePacket(packet)
    }

    override fun deserializePacket(serializedPacket: String): ClientPacket
    {
        return jsonPacketSerializer.deserializePacket(serializedPacket) as ClientPacket
    }
}
