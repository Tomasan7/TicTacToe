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

        return packet.id.toString() + PACKET_ID_DATA_SEPARATOR + json.encodeToString(serializer, packet)
    }

    companion object
    {
        const val PACKET_ID_DATA_SEPARATOR = "="
    }
}
