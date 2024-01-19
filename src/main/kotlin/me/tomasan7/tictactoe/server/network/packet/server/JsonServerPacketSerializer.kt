package me.tomasan7.tictactoe.server.network.packet.server

import kotlinx.serialization.json.Json
import me.tomasan7.tictactoe.server.util.getSerializableClassSerializer

class JsonServerPacketSerializer : ServerPacketSerializer
{
    private val json = Json {}

    override fun serializePacket(packet: ServerPacket): String
    {
        val serializer = getSerializableClassSerializer(packet::class)
            ?: throw IllegalArgumentException("No serializer found for ${packet::class.java.name}")

        return packet.id.toString() + "\n" + json.encodeToString(serializer, packet)
    }
}
