package me.tomasan7.tictactoe.protocol.packet.server

import me.tomasan7.tictactoe.protocol.packet.JsonPacketSerializer

class JsonServerPacketSerializer(
    private val jsonPacketSerializer: JsonPacketSerializer = JsonPacketSerializer()
) : ServerPacketSerializer
{
    override fun serializePacket(packet: ServerPacket): String
    {
        return jsonPacketSerializer.serializePacket(packet)
    }

    override fun deserializePacket(serializedPacket: String): ServerPacket
    {
        return jsonPacketSerializer.deserializePacket(serializedPacket) as ServerPacket
    }
}
