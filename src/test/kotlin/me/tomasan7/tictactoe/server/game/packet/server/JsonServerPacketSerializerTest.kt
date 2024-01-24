package me.tomasan7.tictactoe.server.game.packet.server

import me.tomasan7.tictactoe.server.network.packet.server.JsonServerPacketSerializer
import me.tomasan7.tictactoe.server.network.packet.server.packet.ServerPlaceSymbolPacket
import org.junit.jupiter.api.TestInstance
import kotlin.test.Test
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class JsonServerPacketSerializerTest
{
    private val serializer = JsonServerPacketSerializer()

    @Test
    fun serializePacket()
    {
        val packetIdDataSeparator = JsonServerPacketSerializer.PACKET_ID_DATA_SEPARATOR

        val serverPlaceSymbolPacket = ServerPlaceSymbolPacket(
            playerId = 23,
            x = 4,
            y = 12
        )

        val expected = """14$packetIdDataSeparator{"playerId":23,"x":4,"y":12}"""
        val actual = serializer.serializePacket(serverPlaceSymbolPacket)

        assertEquals(expected, actual)
    }
}
