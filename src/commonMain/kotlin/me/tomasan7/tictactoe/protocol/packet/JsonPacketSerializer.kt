package me.tomasan7.tictactoe.protocol.packet

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import me.tomasan7.tictactoe.protocol.packet.client.packet.*
import me.tomasan7.tictactoe.protocol.packet.server.packet.*
import me.tomasan7.tictactoe.util.getSerializableClassSerializer
import kotlin.reflect.KClass

class JsonPacketSerializer
{
    private val packetDataParserMap = mutableMapOf<Int, (serializedPacketData: String) -> Packet>()
    private val json = Json

    init
    {
        registerPackets()
    }

    fun serializePacket(packet: Packet): String
    {
        val serializer = getSerializableClassSerializer(packet::class) as? KSerializer<Packet>
            ?: throw IllegalArgumentException("No serializer found for ${packet::class.simpleName}")

        return packet.id.toString() + PACKET_ID_DATA_SEPARATOR + json.encodeToString(serializer, packet)
    }

    /**
     * @throws InvalidPacketIdException
     * @throws InvalidPacketFormatException
     */
    fun deserializePacket(serializedPacket: String): Packet
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

        val packetFun = packetDataParserMap[id] ?:
            throw InvalidPacketIdException(id)

        return packetFun(split[1])
    }

    private fun registerPacket(packetId: Int, packetClass: KClass<out Packet>)
    {
        val parserFun = { serializedPacketData: String -> deserializePacketData(packetClass, serializedPacketData) }

        if (packetDataParserMap.containsKey(packetId))
            throw IllegalArgumentException("Packet id $packetId is already registered")

        packetDataParserMap[packetId] = parserFun
    }

    private fun deserializePacketData(packetClass: KClass<out Packet>, serializedPacketData: String): Packet
    {
        val serializer = getSerializableClassSerializer(packetClass)
            ?: throw IllegalArgumentException("No serializer found for ${packetClass.simpleName}")

        return json.decodeFromString(serializer, serializedPacketData)
    }

    private fun registerPackets()
    {
        /* Client packets (alphabetically) */
        registerPacket(ClientCreateGamePacket.PACKET_ID, ClientCreateGamePacket::class)
        registerPacket(ClientJoinGamePacket.PACKET_ID, ClientJoinGamePacket::class)
        registerPacket(ClientJoinRandomGamePacket.PACKET_ID, ClientJoinRandomGamePacket::class)
        registerPacket(ClientPlaceSymbolPacket.PACKET_ID, ClientPlaceSymbolPacket::class)
        registerPacket(ClientReadyPacket.PACKET_ID, ClientReadyPacket::class)
        registerPacket(ClientSetPlayerDataPacket.PACKET_ID, ClientSetPlayerDataPacket::class)

        /* Server packets (alphabetically) */
        registerPacket(ServerAddPlayerPacket.PACKET_ID, ServerAddPlayerPacket::class)
        registerPacket(ServerClientReadyAckPacket.PACKET_ID, ServerClientReadyAckPacket::class)
        registerPacket(ServerGameClosePacket.PACKET_ID, ServerGameClosePacket::class)
        registerPacket(ServerPlayerWinPacket.PACKET_ID, ServerPlayerWinPacket::class)
        registerPacket(ServerJoinGamePacket.PACKET_ID, ServerJoinGamePacket::class)
        registerPacket(ServerPlaceSymbolPacket.PACKET_ID, ServerPlaceSymbolPacket::class)
        registerPacket(ServerPlayerReadyPacket.PACKET_ID, ServerPlayerReadyPacket::class)
        registerPacket(ServerPlayerTurnPacket.PACKET_ID, ServerPlayerTurnPacket::class)
        registerPacket(ServerRemovePlayerPacket.PACKET_ID, ServerRemovePlayerPacket::class)
        registerPacket(ServerSetPlayerDataPacket.PACKET_ID, ServerSetPlayerDataPacket::class)
        registerPacket(ServerStartGamePacket.PACKET_ID, ServerStartGamePacket::class)
    }

    companion object
    {
        const val PACKET_ID_DATA_SEPARATOR = "="
    }
}
