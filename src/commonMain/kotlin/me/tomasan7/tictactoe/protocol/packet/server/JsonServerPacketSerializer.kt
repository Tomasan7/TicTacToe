package me.tomasan7.tictactoe.protocol.packet.server

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import me.tomasan7.tictactoe.util.getSerializableClassSerializer

class JsonServerPacketSerializer : ServerPacketSerializer
{
    private val json = Json {}

    override fun serializePacket(packet: ServerPacket): String
    {
        val serializer = getSerializableClassSerializer(packet::class) as? KSerializer<ServerPacket>
            ?: throw IllegalArgumentException("No serializer found for ${packet::class.simpleName}")

        return packet.id.toString() + PACKET_ID_DATA_SEPARATOR + json.encodeToString(serializer, packet)
    }

    companion object
    {
        const val PACKET_ID_DATA_SEPARATOR = "="
    }
}
