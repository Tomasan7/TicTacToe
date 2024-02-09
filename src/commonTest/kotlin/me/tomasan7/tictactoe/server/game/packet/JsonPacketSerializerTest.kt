package me.tomasan7.tictactoe.server.game.packet

import me.tomasan7.tictactoe.protocol.packet.JsonPacketSerializer
import me.tomasan7.tictactoe.protocol.packet.client.packet.ClientCreateGamePacket
import me.tomasan7.tictactoe.protocol.packet.client.packet.ClientJoinRandomGamePacket
import me.tomasan7.tictactoe.protocol.packet.server.packet.ServerPlaceSymbolPacket
import kotlin.test.Test
import kotlin.test.assertEquals

class JsonPacketSerializerTest
{
    private val serializer = JsonPacketSerializer()
    private val packetIdDataSeparator = JsonPacketSerializer.PACKET_ID_DATA_SEPARATOR

    @Test
    fun testSerializePacket()
    {
        val serverPlaceSymbolPacket = ServerPlaceSymbolPacket(
            playerId = 23,
            x = 4,
            y = 12
        )

        val expected = """14$packetIdDataSeparator{"playerId":23,"x":4,"y":12}"""
        val actual = serializer.serializePacket(serverPlaceSymbolPacket)

        assertEquals(expected, actual)
    }

    @Test
    fun testSerializeFieldlessPacket()
    {
        val packet = ClientJoinRandomGamePacket

        val expected = "${packet.id}$packetIdDataSeparator{}"
        val actual = serializer.serializePacket(packet)

        assertEquals(expected, actual)
    }

    @Test
    fun testDeserializePacket()
    {
        val serializedPacket = """
            0$packetIdDataSeparator{
                "width": 3,
                "height": 3,
                "winLength": 3,
                "symbolSize": 5,
                "maxPlayers": 2,
                "public": true
            }
        """.trimIndent().trim()

        val expected = ClientCreateGamePacket(
            width = 3,
            height = 3,
            winLength = 3,
            symbolSize = 5,
            maxPlayers = 2,
            public = true
        )

        val actual = serializer.deserializePacket(serializedPacket)

        assertEquals(expected, actual)
    }

    @Test
    fun testDeserializeFieldlessPacket()
    {
        val expected = ClientJoinRandomGamePacket

        val actual = serializer.deserializePacket("${expected.id}$packetIdDataSeparator{}")

        assertEquals(expected, actual)
    }
}
