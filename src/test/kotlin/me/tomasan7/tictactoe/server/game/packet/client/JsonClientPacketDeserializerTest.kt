package me.tomasan7.tictactoe.server.game.packet.client

import me.tomasan7.tictactoe.server.network.packet.client.JsonClientPacketDeserializer
import me.tomasan7.tictactoe.server.network.packet.client.packet.ClientCreateGamePacket
import org.junit.jupiter.api.TestInstance
import kotlin.test.Test

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JsonClientPacketDeserializerTest
{
    val deserializer = JsonClientPacketDeserializer()

    @Test
    fun testDeserializePacket()
    {
        val packetIdDataSeparator = JsonClientPacketDeserializer.PACKET_ID_DATA_SEPARATOR

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

        val expectedPacket = ClientCreateGamePacket(
            width = 3,
            height = 3,
            winLength = 3,
            symbolSize = 5,
            maxPlayers = 2,
            public = true
        )

        val packet = deserializer.deserializePacket(serializedPacket)

        assert(packet.id == 0)
        assert(packet is ClientCreateGamePacket)
        assert(packet == expectedPacket)
    }
}
