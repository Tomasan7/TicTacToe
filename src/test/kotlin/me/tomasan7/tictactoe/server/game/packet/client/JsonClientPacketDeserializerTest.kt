package me.tomasan7.tictactoe.server.game.packet.client

import me.tomasan7.tictactoe.server.network.packet.client.packet.ClientCreateGamePacket
import org.junit.jupiter.api.TestInstance
import kotlin.test.Test

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JsonClientPacketDeserializerTest
{
    val deserializer = me.tomasan7.tictactoe.server.network.packet.client.JsonClientPacketDeserializer()

    @Test
    fun testDeserializePacket()
    {
        val serializedPacket = """
            0
            {
                "width": 3,
                "height": 3,
                "winLength": 3,
                "maxPlayers": 2,
                "public": true
            }
        """.trimIndent().trim()

        val expectedPacket = ClientCreateGamePacket(
            width = 3,
            height = 3,
            winLength = 3,
            maxPlayers = 2,
            public = true
        )

        val packet = deserializer.deserializePacket(serializedPacket)

        assert(packet.id == 0)
        assert(packet is ClientCreateGamePacket)
        assert(packet == expectedPacket)
    }
}
