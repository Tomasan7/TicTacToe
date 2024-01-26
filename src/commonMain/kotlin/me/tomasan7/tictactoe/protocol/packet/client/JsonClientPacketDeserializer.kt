package me.tomasan7.tictactoe.protocol.packet.client

import kotlinx.serialization.json.Json
import me.tomasan7.tictactoe.protocol.packet.InvalidPacketFormatException
import me.tomasan7.tictactoe.protocol.packet.InvalidPacketIdException
import me.tomasan7.tictactoe.protocol.packet.client.packet.*
import me.tomasan7.tictactoe.util.getSerializableClassSerializer
import kotlin.reflect.KClass

class JsonClientPacketDeserializer : ClientPacketDeserializer
{
    private val packetMap = mutableMapOf<Int, (serializedPacketData: String) -> ClientPacket>()
    private val json = Json {}

    init
    {
        registerPackets()
    }

    /**
     * @throws InvalidPacketIdException
     * @throws InvalidPacketFormatException
     */
    override fun deserializePacket(serializedPacket: String): ClientPacket
    {
        val split = serializedPacket.split(PACKET_ID_DATA_SEPARATOR, limit = 2) /* limit 2, so it is only divided into packet id and packet data */

        if (split.size != 2)
            throw InvalidPacketFormatException(
                "Expected '$PACKET_ID_DATA_SEPARATOR' between id and data",
                serializedPacket
            )

        val id = split[0].toIntOrNull()
            ?: throw InvalidPacketFormatException(
                "Expected an integer id",
                serializedPacket
            )

        val packetFun = packetMap[id] ?:
            throw InvalidPacketIdException(id)

        return packetFun(split[1])
    }


    private fun registerPacket(packetId: Int, packetClass: KClass<out ClientPacket>)
    {
        val parserFun = { serializedPacketData: String -> deserializePacketData(packetClass, serializedPacketData) }

        if (packetMap.containsKey(packetId))
            throw IllegalArgumentException("Packet id $packetId is already registered")

        packetMap[packetId] = parserFun
    }

    private fun deserializePacketData(packetClass: KClass<out ClientPacket>, serializedPacket: String): ClientPacket
    {
        val serializer = getSerializableClassSerializer(packetClass)
            ?: throw IllegalArgumentException("No serializer found for ${packetClass.simpleName}")

        return json.decodeFromString(serializer, serializedPacket)
    }

    private fun registerPackets()
    {
        registerPacket(ClientCreateGamePacket.PACKET_ID, ClientCreateGamePacket::class)
        registerPacket(ClientJoinGamePacket.PACKET_ID, ClientJoinGamePacket::class)
        registerPacket(ClientJoinRandomGamePacket.PACKET_ID, ClientJoinRandomGamePacket::class)
        registerPacket(ClientPlaceSymbolPacket.PACKET_ID, ClientPlaceSymbolPacket::class)
        registerPacket(ClientReadyPacket.PACKET_ID, ClientReadyPacket::class)
        registerPacket(ClientSetPlayerDataPacket.PACKET_ID, ClientSetPlayerDataPacket::class)
    }

    companion object
    {
        const val PACKET_ID_DATA_SEPARATOR = "="
    }
}
