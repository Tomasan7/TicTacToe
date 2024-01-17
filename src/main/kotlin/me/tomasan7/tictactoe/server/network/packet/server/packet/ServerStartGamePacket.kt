package me.tomasan7.tictactoe.server.network.packet.server.packet

import kotlinx.serialization.Serializable
import me.tomasan7.tictactoe.server.network.packet.server.ServerPacket

@Serializable
data class ServerStartGamePacket(
    val playerOrder: Array<Int>
) : ServerPacket
{
    override val id = PACKET_ID

    override fun equals(other: Any?): Boolean
    {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ServerStartGamePacket

        if (!playerOrder.contentEquals(other.playerOrder)) return false
        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int
    {
        var result = playerOrder.contentHashCode()
        result = 31 * result + id
        return result
    }

    companion object
    {
        const val PACKET_ID = 11
    }
}
