package me.tomasan7.tictactoe.server.util

import kotlinx.serialization.KSerializer
import me.tomasan7.tictactoe.server.game.packet.client.ClientPacket
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.functions

/** Returns a [KSerializer] of a [@Serializable] annotated class, or `null` when the serializer is not found. */
fun <T> getSerializableClassSerializer(clazz: Class<out T>): KSerializer<T>?
{
    return clazz.kotlin.companionObject?.functions?.find { it.name == "serializer" }?.call(clazz.kotlin.companionObjectInstance) as? KSerializer<T>
}
